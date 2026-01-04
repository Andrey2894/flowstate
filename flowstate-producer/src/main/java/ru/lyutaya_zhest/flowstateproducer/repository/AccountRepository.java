package ru.lyutaya_zhest.flowstateproducer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.lyutaya_zhest.flowstateproducer.model.Account;

public interface AccountRepository extends R2dbcRepository<Account, String> {
    Mono<Account> findByUserId(String userId);
}