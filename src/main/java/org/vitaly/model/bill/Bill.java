package org.vitaly.model.bill;

import org.vitaly.model.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by vitaly on 2017-04-08.
 */
public class Bill implements Entity {
    private long id;
    private boolean isPaid;
    private boolean isConfirmed;
    private BillDescriptionEnum description;
    private BigDecimal cashAmount;
    private LocalDateTime creationDateTime;

    private Bill(Builder builder) {
        this.id = builder.id;
        this.isPaid = builder.isPaid;
        this.isConfirmed = builder.isConfirmed;
        this.description = builder.description;
        this.cashAmount = builder.cashAmount;
        this.creationDateTime = builder.creationDateTime;
    }

    public long getId() {
        return id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public BillDescriptionEnum getDescription() {
        return description;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bill bill = (Bill) o;

        return isPaid == bill.isPaid
                && description.equals(bill.description)
                && cashAmount.stripTrailingZeros().equals(bill.cashAmount.stripTrailingZeros())
                && creationDateTime.until(bill.creationDateTime, ChronoUnit.SECONDS) == 0;
    }

    @Override
    public int hashCode() {
        int result = (isPaid ? 1 : 0);
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", isPaid=" + isPaid +
                ", description='" + description + '\'' +
                ", cashAmount=" + cashAmount +
                ", creationDateTime=" + creationDateTime +
                '}';
    }

    public static class Builder {
        private long id;
        private boolean isPaid;
        private boolean isConfirmed;
        private BillDescriptionEnum description;
        private BigDecimal cashAmount;
        private LocalDateTime creationDateTime;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setPaid(boolean paid) {
            this.isPaid = paid;
            return this;
        }

        public Builder setConfirmed(boolean confirmed) {
            this.isConfirmed = confirmed;
            return this;
        }

        public Builder setDescription(BillDescriptionEnum description) {
            this.description = description;
            return this;
        }

        public Builder setCashAmount(BigDecimal cashAmount) {
            this.cashAmount = cashAmount;
            return this;
        }

        public Builder setCreationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        public Bill build() {
            return new Bill(this);
        }
    }

    public static Bill forService(BigDecimal cashAmount) {
        return new Bill.Builder()
                .setCashAmount(cashAmount)
                .setDescription(BillDescriptionEnum.SERVICE)
                .setCreationDateTime(LocalDateTime.now())
                .setPaid(false)
                .setConfirmed(false)
                .build();
    }

    public static Bill forDamage(BigDecimal cashAmount) {
        return new Bill.Builder()
                .setCashAmount(cashAmount)
                .setDescription(BillDescriptionEnum.DAMAGE)
                .setCreationDateTime(LocalDateTime.now())
                .setPaid(false)
                .setConfirmed(false)
                .build();
    }
}
