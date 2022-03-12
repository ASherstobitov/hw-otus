package ru.hwAtm.bank.bankImpl;

import ru.hwAtm.account.Account;
import ru.hwAtm.atm.Checkable;
import ru.hwAtm.atm.atmImpl.SimpleAtm;
import ru.hwAtm.bank.Drawable;
import ru.hwAtm.bank.Replenished;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Bank implements Replenished, Drawable {

    private final List<Account> accounts;
    private final Logger logger;

    public Bank(List<Account> accounts) {
        this.logger = Logger.getLogger(SimpleAtm.class.getName());
        this.accounts = accounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public void withDrawMoney(String accountNumber, BigDecimal amount) {
        findAccount(accountNumber)
                .ifPresent(en -> en.setAmount(en.getAmount().subtract(amount)));
    }

    @Override
    public void addMoney(String accountNumber, int sum) {
        findAccount(accountNumber)
                .ifPresent(en -> en.setAmount(en.getAmount().add(new BigDecimal(sum))));
    }

    public Optional<Account> findAccount(String accountNumber) {
        return accounts.stream()
                .filter(e -> accountNumber.equals(e.getAccountNumber()))
                .findFirst();
    }

}
