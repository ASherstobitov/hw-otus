package ru.hwAtm.atm;

import ru.hwAtm.banknote.Banknote;

import java.math.BigDecimal;
import java.util.List;

public interface Giveable {
    List<Banknote> giveMoney(String accountNumber, BigDecimal amount);
}
