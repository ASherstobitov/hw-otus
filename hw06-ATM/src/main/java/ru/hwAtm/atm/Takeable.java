package ru.hwAtm.atm;

import ru.hwAtm.banknote.Banknote;

import java.util.List;

public interface Takeable {
    void takeMoney(String accountNumber, List<Banknote> amount);
}
