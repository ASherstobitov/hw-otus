package ru.otus.proxy;

public class CalculatorImp implements CalculatorInterface {

    @Log
    @Override
    public void sum(int param1, int param2) {
        System.out.println("sum result: " + (param1 + param2));
    }


    public void distinct(int param1, int param2) {
        System.out.println("distinct result: " + (param1 + param2));
    }

    @Override
    public String toString() {
        return "TestLoggingImp{}";
    }
}
