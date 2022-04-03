package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.ProcessorChangeField11ToField12;
import ru.otus.listener.homework.ProcessorEvenSecThrowException;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {

        var processorEvenSecThrowException = new ProcessorEvenSecThrowException(() -> LocalTime.of(11, 11, 11));

        var processorChangeField11ToField12 = new ProcessorChangeField11ToField12();

        var processors = List.of(processorEvenSecThrowException,
        processorChangeField11ToField12);

        var complexProcessor = new ComplexProcessor(processors, ex -> { });
        var historyListener = new HistoryListener();

        var field12 = "field12";
        var field11 = "field11";
        var id = 100L;
        var data = "33";

        var field13 = new ObjectForMessage(new ArrayList<>());
        var field13Data = new ArrayList<String>();

        field13Data.add(data);
        field13.setData(field13Data);

        var message = new Message.Builder(id)
                .field11(field12)
                .field12(field11)
                .field13(field13)
                .build();



        complexProcessor.addListener(historyListener);

        var result = complexProcessor.handle(message);

        processorChangeField11ToField12.process(message);


        System.out.println("result: " + result);


        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
    }
}
