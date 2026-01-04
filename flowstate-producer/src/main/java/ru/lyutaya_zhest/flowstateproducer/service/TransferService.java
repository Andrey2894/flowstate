package ru.lyutaya_zhest.flowstateproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import ru.lyutaya_zhest.flowstateapi.MoneyTransferEvent;
import ru.lyutaya_zhest.flowstateapi.TransferRequestDto;
import ru.lyutaya_zhest.flowstateproducer.model.Account;
import ru.lyutaya_zhest.flowstateproducer.model.Transaction;
import ru.lyutaya_zhest.flowstateproducer.repository.AccountRepository;
import ru.lyutaya_zhest.flowstateproducer.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final StreamBridge streamBridge;
    private final TransactionalOperator transactionalOperator;

    public Mono<String> transfer(TransferRequestDto dto) {
        String txId = UUID.randomUUID().toString();

        // 1. Находим счета отправителя и получателя одновременно
        return Mono.zip(
                        accountRepository.findById(dto.fromAccountId()),
                        accountRepository.findById(dto.toAccountId())
                )
                // 2. Проверяем баланс и бизнес-правила
                .flatMap(tuple -> {
                    Account sender = tuple.getT1();
                    Account receiver = tuple.getT2();

                    if (sender.getBalance().compareTo(dto.amount()) < 0) {
                        return Mono.error(new RuntimeException("Insufficient funds"));
                    }

                    // 3. Обновляем балансы (Immutability style)
                    Account updatedSender = sender.withBalance(sender.getBalance().subtract(dto.amount()));
                    Account updatedReceiver = receiver.withBalance(receiver.getBalance().add(dto.amount()));

                    // 4. Создаем запись о транзакции
                    Transaction tx = new Transaction(txId, dto.fromAccountId(), dto.toAccountId(),
                            dto.amount(), "INITIATED", LocalDateTime.now(), true);

                    // 5. Сохраняем всё в БД
                    return accountRepository.saveAll(List.of(updatedSender, updatedReceiver))
                            .then(transactionRepository.save(tx))
                            // 6. Отправляем событие в Kafka ТОЛЬКО после успеха в БД
                            .doOnSuccess(savedTx -> sendToKafka(savedTx, dto))
                            .thenReturn(txId);
                })
                // Оборачиваем всю цепочку в транзакцию БД
                .as(transactionalOperator::transactional);
    }

    private void sendToKafka(Transaction tx, TransferRequestDto dto) {
        MoneyTransferEvent event = new MoneyTransferEvent(
                tx.getId(),
                dto.fromAccountId(),
                dto.toAccountId(),
                dto.amount(),
                dto.currency(),
                "TRANSFER",
                System.currentTimeMillis()
        );
        streamBridge.send("produceMessage-out-0", event);
    }
}
