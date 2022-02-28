package ru.hwAtm.safe.safeImpl;

import ru.hwAtm.account.Account;
import ru.hwAtm.banknote.Banknote;
import ru.hwAtm.safe.Checkable;
import ru.hwAtm.exception.MyRuntimeException;
import ru.hwAtm.atm.ATM;
import ru.hwAtm.safe.Safe;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SimpleAtm implements ATM, Checkable {

    private final Safe safe;

    private final List<Account> accounts;

    private final Logger logger;

    public SimpleAtm(Safe safe, List<Account> accounts) {
        this.safe = safe;
        this.accounts = accounts;
        this.logger = Logger.getLogger(SimpleAtm.class.getName());
    }

    @Override
    public void takeMoney(String accountNumber, List<Banknote> banknotes) {

        if (isInvalidAddBanknotes(safe, banknotes)) {
            throw new MyRuntimeException("Unacceptable type of banknotes");
        }

        var sum = banknotes.stream().mapToInt(Banknote::getNote).sum();

        addMoney(accountNumber, sum);

        banknotes.forEach(safe::add);
    }


    @Override
    public List<Banknote> giveMoney(String accountNumber, BigDecimal amount) {

        if (isUnableAccountExtract(accountNumber, amount, accounts)) {
            throw new MyRuntimeException("The required amount more then the sum on the account");
        }
        if (isNotEnoughMoneyInAtm(safe, amount)) {
            throw new MyRuntimeException("The required amount more then the ATM has");
        }
        if (!isMultipleAmount(amount)) {
            throw new MyRuntimeException("The amount should be multiple to 100");
        }

        withDrawMoney(accountNumber, amount);

        return safe.giveMoney(accountNumber, amount);
    }

    @Override
    public void showBalance(String accountNumber) {
        accounts.stream().filter(e ->
                        accountNumber.equals(e.getAccountNumber()))
                .forEach(e -> logger.info(e.toString()));

    }


    public static boolean isNotEnoughMoneyInAtm(Safe safe, BigDecimal amount) {
        var sum = safe.getSlots().entrySet()
                .stream()
                .mapToInt(e -> e.getKey().getNote() * e.getValue())
                .sum();
        return new BigDecimal(sum).compareTo(amount) < 0;
    }

    public static boolean isMultipleAmount(BigDecimal amount) {
        return amount.intValue() % 100 == 0;
    }

    private boolean isInvalidAddBanknotes(Safe safe, List<Banknote> banknotes) {
        if (safe == null || banknotes == null || safe.getSlots() == null ||
                banknotes.isEmpty() || safe.getSlots().isEmpty()) {
            throw new NullPointerException("The safe or banknotes are null or empty");
        }
        return !banknotes.stream().allMatch(e -> safe.getSlots().containsKey(e));
    }

    private boolean isUnableAccountExtract(String accountNumber, BigDecimal amount, List<Account> accounts) {
        AtomicInteger result = new AtomicInteger();
        findAccount(accountNumber, accounts)
                .ifPresent(e -> result.set(e.getAmount().compareTo(amount)));
        var boolRes = result.get() >= 0;
        return !boolRes;
    }

    private Optional<Account> findAccount(String accountNumber, List<Account> accounts) {
        return accounts.stream()
                .filter(e -> accountNumber.equals(e.getAccountNumber()))
                .findFirst();
    }

    private void addMoney(String accountNumber, int sum) {
        findAccount(accountNumber, accounts)
                .ifPresent(en -> en.setAmount(en.getAmount().add(new BigDecimal(sum))));
    }

    private void withDrawMoney(String accountNumber, BigDecimal amount) {
        findAccount(accountNumber, accounts)
                .ifPresent(en -> en.setAmount(en.getAmount().subtract(amount)));
    }

}