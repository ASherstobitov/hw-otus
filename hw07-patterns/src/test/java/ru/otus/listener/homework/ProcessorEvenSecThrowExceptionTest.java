package ru.otus.listener.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorEvenSecThrowExceptionTest {

    @Test
    @DisplayName("Тестируем процесс по выбрасыванию исключения")
    void processorEvenSecThrowExceptionTest() {

        var processorEvenSecThrowException = new ProcessorEvenSecThrowException(() -> LocalTime.of(1, 1, 1));

        var field1 = "field1";

        var id = 100L;
        var exceptionMessage = "This second is even!";

        var message = new Message.Builder(id)
                .field1(field1)
                .build();

        var runtimeException = assertThrows(RuntimeException.class, () ->
                processorEvenSecThrowException.process(message));

        assertEquals(exceptionMessage, runtimeException.getMessage());

    }

}