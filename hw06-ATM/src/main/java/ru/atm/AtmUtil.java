package ru.atm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class AtmUtil {

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

    public static boolean isInvalidAddBanknotes(Safe safe, List<Banknote> banknotes) {
        if (safe == null || banknotes == null || safe.getSlots() == null ||
                banknotes.isEmpty() || safe.getSlots().isEmpty()) {
            throw new NullPointerException("The safe or banknotes are null or empty");
        }
        return !banknotes.stream().allMatch(e -> safe.getSlots().containsKey(e));
    }

    public static boolean isUnableAccountExtract(String accountNumber, BigDecimal amount, List<Account> accounts) {
        AtomicInteger result = new AtomicInteger();
        findAccount(accountNumber, accounts)
                .ifPresent(e -> result.set(e.getAmount().compareTo(amount)));
        var boolRes = result.get() >= 0;
        return !boolRes;
    }

    public static Optional<Account> findAccount(String accountNumber, List<Account> accounts) {
        return accounts.stream()
                .filter(e -> accountNumber.equals(e.getAccountNumber()))
                .findFirst();
    }
}
