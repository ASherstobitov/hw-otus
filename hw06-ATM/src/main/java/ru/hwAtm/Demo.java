package ru.hwAtm;


import ru.hwAtm.account.Account;
import ru.hwAtm.safe.Safe;
import ru.hwAtm.banknote.Banknote;
import ru.hwAtm.atm.atmImpl.SafeAtm;
import ru.hwAtm.safe.safeImpl.SimpleAtm;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Demo {

    private static final Logger logger = Logger.getLogger(Demo.class.getName());

    public static void main(String[] args) {

        List<Account> accounts = Arrays.asList(
                new Account("11111", new BigDecimal(50000), "rub"),
                new Account("22222", new BigDecimal(77500), "rub"),
                new Account("33333", new BigDecimal(1000), "rub"),
                new Account("44444", new BigDecimal(500), "rub")
        );


        Safe safe = new SafeAtm();

        SimpleAtm atm = new SimpleAtm(safe, accounts);

        safe.uploadBanknotes(new Banknote(100, "rub"), 100);
        safe.uploadBanknotes(new Banknote(500, "rub"), 100);
        safe.uploadBanknotes(new Banknote(1000, "rub"), 100);
        safe.uploadBanknotes(new Banknote(2000, "rub"), 100);
        safe.uploadBanknotes(new Banknote(5000, "rub"), 100);

        atm.takeMoney("11111", Arrays.asList(
                new Banknote(5000, "rub"),
                new Banknote(500, "rub"))
        );

        var banknotes = atm.giveMoney("11111", new BigDecimal(11500));

        banknotes.forEach(e -> logger.info(e.toString()));

        atm.showBalance("11111");
    }
}
