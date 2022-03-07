package ru.hwAtm.bank;

import java.math.BigDecimal;

public interface Drawable {
    void withDrawMoney(String accountNumber, BigDecimal amount);
}
