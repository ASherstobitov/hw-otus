package ru.otus.crm.service.api;

import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    Optional<User> getAdmin(String login);
}