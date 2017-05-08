package org.vitaly.service.impl.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by vitaly on 2017-04-20.
 */
public class BillDto {
    private long id;
    private boolean isPaid;
    private boolean isConfirmed;
    private String description;
    private BigDecimal cashAmount;
    private LocalDateTime creationDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillDto billDto = (BillDto) o;

        return id == billDto.id
                && isPaid == billDto.isPaid
                && isConfirmed == billDto.isConfirmed
                && (Objects.equals(description, billDto.description))
                && (Objects.equals(cashAmount, billDto.cashAmount))
                && (Objects.equals(creationDateTime, billDto.creationDateTime));
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (isPaid ? 1 : 0);
        result = 31 * result + (isConfirmed ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cashAmount != null ? cashAmount.hashCode() : 0);
        result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
        return result;
    }
}
