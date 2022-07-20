package ru.otus.services;

import ru.otus.crm.service.api.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser DBServiceUser;

    public UserAuthServiceImpl(DBServiceUser DBServiceUser) {
        this.DBServiceUser = DBServiceUser;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return DBServiceUser.getAdmin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
