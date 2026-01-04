package ru.lyutaya_zhest.flowstateapi;

import java.math.BigDecimal;

public record MoneyTransferEvent(
        String transactionId, // UUID для идемпотентности
        String senderId,
        String receiverId,
        BigDecimal amount,
        String currency,
        String type,          // PAY, TRANSFER, CASHBACK
        long timestamp
) {
}
