package org.vitaly.model.bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by vitaly on 2017-04-08.
 */
public class Bill {
    private long id;
    private boolean isPaid;
    private String description;
    private BigDecimal cashAmount;
    private LocalDateTime creationDateTime;

    private Bill(Builder builder) {
        this.id = builder.id;
        this.isPaid = builder.isPaid;
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

    public String getDescription() {
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

        if (isPaid != bill.isPaid) {
            return false;
        }
        if (!description.equals(bill.description)) {
            return false;
        }
        if (!cashAmount.stripTrailingZeros().equals(bill.cashAmount.stripTrailingZeros())) {
            return false;
        }
        return creationDateTime.until(bill.creationDateTime, ChronoUnit.SECONDS) == 0;
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
        private String description;
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

        public Builder setDescription(String description) {
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
}
