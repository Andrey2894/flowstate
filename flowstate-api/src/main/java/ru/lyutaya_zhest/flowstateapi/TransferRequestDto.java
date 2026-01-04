package ru.lyutaya_zhest.flowstateapi;

import java.math.BigDecimal;

public record TransferRequestDto(
        String fromAccountId,
        String toAccountId,
        BigDecimal amount,
        String currency
) {}