package ru.otus.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProcessorChangeField11ToField12Test {

    @Test
    @DisplayName("Тестируем процесс по смене значений полей Field11 и Field12")
    void processorChangeField11ToField12Test() {

        var field12 = "field12";
        var field11 = "field11";
        var id = 100L;

        var message = new Message.Builder(id)
                .field11(field12)
                .field12(field11)
                .build();

        var processorChangeField11ToField12 = new ProcessorChangeField11ToField12();

        var process = processorChangeField11ToField12.process(message);

        assertThat(process.getField11()).isEqualTo(field11);
        assertThat(process.getField12()).isEqualTo(field12);

    }
}