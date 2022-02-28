package ru.atm.account;

import java.math.BigDecimal;

public class Account {

    private String accountNumber;

    private BigDecimal amount;

    private String currency;

    public Account(String accountNumber, BigDecimal amount, String currency) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
    }


    public String getAccountNumber() {
        return accountNumber;
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

    @Override
    public String toString() {
        return "CardAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

}
