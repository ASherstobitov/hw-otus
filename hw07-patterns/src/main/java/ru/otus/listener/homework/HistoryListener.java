package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

   private final Set<Message> messageSet;

    public HistoryListener(Set<Message> messageSet) {
        this.messageSet = messageSet;
    }

    @Override
    public void onUpdated(Message msg) {
        messageSet.add(msg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {

        return messageSet.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }
}
