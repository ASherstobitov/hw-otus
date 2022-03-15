package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorEvenSecThrowException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecThrowException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {

        var date = dateTimeProvider.getDate();

        var second = date.getSecond();
        if (second % 2 != 0) {
            throw new RuntimeException("This second is even!");
        }
        return message.toBuilder().field1(message.getField1()).build();

    }

}
