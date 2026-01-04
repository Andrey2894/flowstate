package ru.lyutaya_zhest.flowstateproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Getter // Геттеры нужны для маппинга
@AllArgsConstructor
@Table("accounts")
public class Account implements Persistable<String> {
    @Id
    private final String id;
    private final String userId;

    @With // Генерирует метод withBalance(BigDecimal balance)
    private final BigDecimal balance;

    private final String currency;

    @Version
    private final Long version;

    @Transient
    @With // Позволит удобно создавать новые объекты с флагом isNew
    private final boolean isNew;

    @PersistenceCreator
    public Account(String id, String userId, BigDecimal balance, String currency, Long version) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
        this.version = version;
        this.isNew = false; // Раз из базы, значит не новый
    }

    @Override
    public String getId() { return id; }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    // Вручную добавим конструктор для создания "с нуля" (удобно для тестов)
    public static Account createNew(String userId, BigDecimal initialBalance, String currency) {
        return new Account(UUID.randomUUID().toString(), userId, initialBalance, currency, null, true);
    }
}