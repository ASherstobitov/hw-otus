package ru.otus.listener.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenSecThrowException implements Processor {

    private final TimeProvider timeProvider;

    public ProcessorEvenSecThrowException(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {

        var time = timeProvider.getTime();

        var second = time.getSecond();
        if (second % 2 != 0) {
            throw new RuntimeException("This second is even!");
        }
        return message.toBuilder().field1(message.getField1()).build();

    }

}
