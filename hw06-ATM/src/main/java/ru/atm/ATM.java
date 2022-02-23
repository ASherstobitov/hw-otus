package ru.atm;

import java.math.BigDecimal;
import java.util.List;

public interface ATM {

    void takeMoney(String accountNumber, List<Banknote> amount);

    List<Banknote> giveMoney(String accountNumber, BigDecimal amount);
}
