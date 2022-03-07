package ru.hwAtm.atm.atmImpl;


import ru.hwAtm.atm.Checkable;
import ru.hwAtm.atm.Giveable;
import ru.hwAtm.atm.Takeable;
import ru.hwAtm.bank.bankImpl.Bank;
import ru.hwAtm.banknote.Banknote;
import ru.hwAtm.exception.MyRuntimeException;
import ru.hwAtm.safe.Safe;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SimpleAtm implements Giveable, Takeable, Checkable {

    private final Safe safe;

    private final Bank bank;

    private final Logger logger;

    public SimpleAtm(Safe safe, Bank bank) {
        this.safe = safe;
        this.bank = bank;
        this.logger = Logger.getLogger(SimpleAtm.class.getName());
    }

    @Override
    public void takeMoney(String accountNumber, List<Banknote> banknotes) {

        if (isInvalidAddBanknotes(safe, banknotes)) {
            throw new MyRuntimeException("Unacceptable type of banknotes");
        }

        var sum = banknotes.stream().mapToInt(Banknote::getNote).sum();

        bank.addMoney(accountNumber, sum);

        banknotes.forEach(safe::add);
    }


    @Override
    public List<Banknote> giveMoney(String accountNumber, BigDecimal amount) {

        if (isUnableAccountExtract(accountNumber, amount)) {
            throw new MyRuntimeException("The required amount more then the sum on the account");
        }
        if (isNotEnoughMoneyInAtm(safe, amount)) {
            throw new MyRuntimeException("The required amount more then the ATM has");
        }
        if (!isMultipleAmount(amount)) {
            throw new MyRuntimeException("The amount should be multiple to 100");
        }

        bank.withDrawMoney(accountNumber, amount);

        return safe.giveMoney(accountNumber, amount);
    }

    @Override
    public void showBalance(String accountNumber) {
        bank.getAccounts().stream().filter(e ->
                        accountNumber.equals(e.getAccountNumber()))
                .forEach(e -> logger.info(e.toString()));
    }

    private static boolean isNotEnoughMoneyInAtm(Safe safe, BigDecimal amount) {
        var sum = safe.getSlots().entrySet()
                .stream()
                .mapToInt(e -> e.getKey().getNote() * e.getValue())
                .sum();
        return new BigDecimal(sum).compareTo(amount) < 0;
    }

    private static boolean isMultipleAmount(BigDecimal amount) {
        return amount.intValue() % 100 == 0;
    }

    private boolean isInvalidAddBanknotes(Safe safe, List<Banknote> banknotes) {
        if (safe == null || banknotes == null || safe.getSlots() == null ||
                banknotes.isEmpty() || safe.getSlots().isEmpty()) {
            throw new NullPointerException("The safe or banknotes are null or empty");
        }
        return !banknotes.stream().allMatch(e -> safe.getSlots().containsKey(e));
    }

    private boolean isUnableAccountExtract(String accountNumber, BigDecimal amount) {
        AtomicInteger result = new AtomicInteger();
        bank.findAccount(accountNumber)
                .ifPresent(e -> result.set(e.getAmount().compareTo(amount)));
        var boolRes = result.get() >= 0;
        return !boolRes;
    }
}