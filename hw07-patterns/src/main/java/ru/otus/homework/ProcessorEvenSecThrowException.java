package ru.otus.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class ProcessorEvenSecThrowException implements Processor {

    @Override
    public Message process(Message message) {

        var date = LocalDateTime.now();

        var second = date.getSecond();
        if (second % 2 != 0) {
            throw new RuntimeException("This second is even!");
        }
        return message.toBuilder().field1(message.getField1()).build();

    }

}
