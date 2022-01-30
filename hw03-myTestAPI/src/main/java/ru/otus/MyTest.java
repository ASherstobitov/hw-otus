package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class MyTest {

    // Подготовительные мероприятия. Метод выполнится перед каждым тестом
    @Before
    public void setUp() {
        System.out.println("@BeforeEach. \n" +  "Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    // Сам тест
    @Test
    public void anyTest1() {
        throw new RuntimeException("@Test: anyTest1() \n" +  "Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    // Еще тест
    @Test
    public void anyTest2() {
        System.out.println("@Test: anyTest2. \n" + "Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    // Завершающие мероприятия. Метод выполнится после каждого теста
    @After
    public void tearDown() {
        System.out.println("@AfterEach. \n" + "Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

}
