package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MyProxy {

    private MyProxy() {
    }

    public static CalculatorInterface createCalculator() {
        InvocationHandler handler = new MyInvocationHandler(new CalculatorImp());

        return (CalculatorInterface) java.lang.reflect.Proxy.newProxyInstance(MyProxy.class.getClassLoader()
        , new Class<?>[]{CalculatorInterface.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
        private final CalculatorInterface calculatorInterface;

        MyInvocationHandler(CalculatorInterface calculatorInterface) {
            this.calculatorInterface = calculatorInterface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Arrays.stream(calculatorInterface.getClass().getMethods())
                    .filter(e -> e.getName().equals(method.getName())
                    && Arrays.equals(e.getParameterTypes(), method.getParameterTypes()))
                    .flatMap(e -> Arrays.stream(e.getDeclaredAnnotations()))
                    .filter(e -> e.annotationType().equals(Log.class))
                    .findFirst()
                    .ifPresent(e -> System.out.printf("============>The method name: %s, param types : (%s)  \n",

                            method.getName(),

                            Arrays.stream(method.getParameterTypes())
                                    .map(Class::toString)
                                    .collect(Collectors.joining(", ")))
                    );


            return method.invoke(calculatorInterface, args);
        }

        @Override
        public String toString() {
            return "MyInvocationHandler{" +
                    "calculatorInterface=" + calculatorInterface +'}';
        }
    }

}
