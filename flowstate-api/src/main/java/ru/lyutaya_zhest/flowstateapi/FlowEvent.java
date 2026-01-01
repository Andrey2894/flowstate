package ru.lyutaya_zhest.flowstateapi;

public record FlowEvent(
        String id,
        String payload,
        long createdAt
) {}