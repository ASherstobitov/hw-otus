package ru.atm.safe;

import ru.atm.banknote.Banknote;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Safe {

    void add(Banknote banknote);

    List<Banknote> giveMoney(String accountNumber, BigDecimal amount);

    void uploadBanknotes(Banknote banknote, Integer value);

    Map<Banknote, Integer> getSlots();
}
