package ru.hwAtm.bank;

import java.math.BigDecimal;

public interface Replenished {
    void addMoney(String accountNumber, int sum);
}