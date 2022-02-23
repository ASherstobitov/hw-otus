package ru.atm;

import java.math.BigDecimal;

public interface Account {

    String getAccountNumber();

    void setAccountNumber(String accountNumber);

     BigDecimal getAmount();

    void setAmount(BigDecimal amount);

    String getCurrency();

    void setCurrency(String currency);
}
