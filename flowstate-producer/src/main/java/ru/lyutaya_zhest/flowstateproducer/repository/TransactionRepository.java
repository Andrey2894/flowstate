package ru.lyutaya_zhest.flowstateproducer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.lyutaya_zhest.flowstateproducer.model.Transaction;

public interface TransactionRepository extends R2dbcRepository<Transaction, String> {
    Flux<Transaction> findAllByFromAccountId(String fromAccountId);
}
