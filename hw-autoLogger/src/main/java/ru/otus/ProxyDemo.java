package ru.otus;

import ru.otus.proxy.CalculatorInterface;
import ru.otus.proxy.MyProxy;


public class ProxyDemo {
    public static void main(String[] args) {

        CalculatorInterface calculator = MyProxy.createCalculator();
        calculator.sum(10, 20);
        calculator.distinct(20, 10);

    }
}
