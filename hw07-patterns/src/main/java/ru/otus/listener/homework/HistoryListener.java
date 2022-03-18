package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class HistoryListener implements Listener, HistoryReader {

   private final Map<Long, Message> messageMap;

   private final AtomicLong idGenerator;

    public HistoryListener() {
        this.messageMap = new HashMap<>();
        this.idGenerator = new AtomicLong();
    }

    @Override
    public void onUpdated(Message msg) {
        messageMap.put(msg.getId(), new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {

        return messageMap.entrySet()
                .stream()
                .filter(e -> e.getKey() == id)
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
