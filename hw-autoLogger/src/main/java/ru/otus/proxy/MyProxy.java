package ru.otus.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MyProxy {


    private static List<Method> methods;
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


            methods = Arrays.stream(calculatorInterface.getClass().getMethods())
                    .filter(e -> Arrays.stream(e.getDeclaredAnnotations()).anyMatch(a -> a.annotationType().equals(Log.class)))
                    .toList();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


            methods.stream()
                    .filter(e -> e.getName().equals(method.getName())
                            && Arrays.equals(e.getParameterTypes(), method.getParameterTypes()))
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
