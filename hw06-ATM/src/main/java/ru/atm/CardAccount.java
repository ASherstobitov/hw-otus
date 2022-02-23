package ru.atm;

import java.math.BigDecimal;

public class CardAccount implements Account {

    private String accountNumber;

    private BigDecimal amount;

    private String currency;

    public CardAccount(String accountNumber, BigDecimal amount, String currency) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
    }

    private CardAccount(Builder builder) {
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.currency = builder.currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CardAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

    public static class Builder {

        private String accountNumber;

        private BigDecimal amount;

        private String currency;

        public Builder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public CardAccount build() {
            return new CardAccount(this);
        }
    }
}
