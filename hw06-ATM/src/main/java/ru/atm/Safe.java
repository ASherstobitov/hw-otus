package ru.atm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Safe {

    void add(Banknote banknote);

    List<Banknote> giveMoney(String accountNumber, BigDecimal amount);

    void showBalance();

    Map<Banknote, Integer> getSlots();
}
