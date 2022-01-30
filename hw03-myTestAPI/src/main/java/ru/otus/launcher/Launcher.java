package ru.otus.launcher;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.otus.utils.ReflectionHelper.callMethod;

public class Launcher {

    public static void launch(String clazzName) {

        AtomicInteger countFailedTest = new AtomicInteger();
        var mapMethods = generateMap(clazzName);

        try {
            Class<?> forName = Class.forName(clazzName);
            Constructor<?> constructor = forName.getConstructor();

            mapMethods.get("ru.otus.annotations.Test").forEach(test -> {

                Object instance = null;

                try {
                    instance = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                Runnable r = getRunnable(mapMethods, test, instance);
                Thread thread = new Thread(r);

                thread.setUncaughtExceptionHandler((th, ex) -> {
                    countFailedTest.incrementAndGet();
                    var stackTrace = ex.getStackTrace();
                    Arrays.stream(stackTrace).forEach(System.out::println);
                });
                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            var countTest = Optional.ofNullable(mapMethods.get("ru.otus.annotations.Test"))
                    .map(List::size).orElse(0);
            System.out.println("Count of tests: " + countTest);
            System.out.println("Successfully finished tests: " +  (countTest - countFailedTest.get()));
            System.out.println("Failed tests:" +  countFailedTest);


        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private static Runnable getRunnable(Map<String, List<Method>> mapMethods, Method test, Object finalInstance) {
        return () -> {

            try {

                for (Method before : mapMethods.get("ru.otus.annotations.Before")) {
                    callMethod(finalInstance, before.getName());
                }

                callMethod(finalInstance, test.getName());

            } finally {
                for (Method after : mapMethods.get("ru.otus.annotations.After")) {
                    callMethod(finalInstance, after.getName());
                }
            }

        };
    }

    private static Map<String, List<Method>> generateMap(String clazzName) {
        Map<String, List<Method>> mapMethods = new HashMap<>();
        try {
            Class<?> forName = Class.forName(clazzName);
            var methods = forName.getMethods();
            for (Method tempMethod : methods) {
                Arrays.asList(tempMethod.getDeclaredAnnotations()).forEach(e -> {
                    if (e instanceof ru.otus.annotations.After) {
                        updateMap(mapMethods, tempMethod, "ru.otus.annotations.After");
                    } else if (e instanceof ru.otus.annotations.Test) {
                        updateMap(mapMethods, tempMethod, "ru.otus.annotations.Test");
                    } else if (e instanceof ru.otus.annotations.Before) {
                        updateMap(mapMethods, tempMethod, "ru.otus.annotations.Before");
                    }
                });
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mapMethods;
    }

    private static void updateMap(Map<String, List<Method>> mapMethods, Method tempMethod, String keyString) {
        if (mapMethods.containsKey(keyString)) {
            mapMethods.get(keyString).add(tempMethod);
        } else {
            var methods = new ArrayList<Method>();
            methods.add(tempMethod);
            mapMethods.put(keyString, methods);
        }
    }
}
