package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorEvenSecThrowExceptionTest {

    @Test
    @DisplayName("Тестируем процесс по выбрасыванию исключения")
    void processorEvenSecThrowExceptionTest() {

        var processorEvenSecThrowException = new ProcessorEvenSecThrowException(LocalDateTime::now);

        var field1 = "field1";

        var id = 100L;
        var exceptionMessage = "This second is even!";

        var message = new Message.Builder(id)
                .field1(field1)
                .build();
        try {

            Message tempMessage = processorEvenSecThrowException.process(message);

            assertEquals(field1, tempMessage.getField1());

        } catch (RuntimeException e) {

            assertEquals(exceptionMessage, e.getMessage());

            System.out.println(exceptionMessage);

        }
    }
}