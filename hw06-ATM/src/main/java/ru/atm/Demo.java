package ru.atm;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Demo {

    private static final Logger logger = Logger.getLogger(Demo.class.getName());

    public static void main(String[] args) {

        List<Account> accounts = Arrays.asList(
                new CardAccount("11111", new BigDecimal(50000), "rub"),
                new CardAccount("22222", new BigDecimal(77500), "rub"),
                new CardAccount("33333", new BigDecimal(1000), "rub"),
                new CardAccount("44444", new BigDecimal(500), "rub")
        );

        var slots = new TreeMap<Banknote, Integer>(
                Comparator.comparing(Banknote::getCurrency)
                .thenComparing(Banknote::getNote).reversed());

        slots.put(new Banknote(100, "rub"), 100);
        slots.put(new Banknote(500, "rub"), 100);
        slots.put(new Banknote(1000, "rub"), 100);
        slots.put(new Banknote(2000, "rub"), 100);
        slots.put(new Banknote(5000, "rub"), 100);


        Safe safe = new SafeAtm(slots);

        SimpleAtm atm = new SimpleAtm(safe, accounts);

        atm.takeMoney("11111", Arrays.asList(
                new Banknote(5000, "rub"),
                new Banknote(500, "rub"))
        );

        var banknotes = atm.giveMoney("11111", new BigDecimal(11500));

        banknotes.forEach(e -> logger.info(e.toString()));

        atm.showBalance("11111");
    }
}
