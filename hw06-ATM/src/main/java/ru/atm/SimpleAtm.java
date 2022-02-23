package ru.atm;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

import static ru.atm.AtmUtil.*;

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

    private void addMoney(String accountNumber, int sum) {
        findAccount(accountNumber, accounts)
                .ifPresent(en -> en.setAmount(en.getAmount().add(new BigDecimal(sum))));
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


    private void withDrawMoney(String accountNumber, BigDecimal amount) {
        findAccount(accountNumber, accounts)
                .ifPresent(en -> en.setAmount(en.getAmount().subtract(amount)));
    }

    @Override
    public void showBalance(String accountNumber) {
        accounts.stream().filter(e ->
                        accountNumber.equals(e.getAccountNumber()))
                .forEach(e -> logger.info(e.toString()));

    }

}