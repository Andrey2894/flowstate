package ru.lyutaya_zhest.flowstateproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Table("transactions")
public class Transaction implements Persistable<String> {

    @Id
    private String id;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private String status; // INITIATED, COMPLETED, FAILED
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = false;

    @Override
    public String getId() { return id; }

    @Override
    public boolean isNew() { return isNew || id == null; }

    public void setAsNew() { this.isNew = true; }

}