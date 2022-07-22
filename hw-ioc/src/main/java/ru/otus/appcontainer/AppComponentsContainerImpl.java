package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.ConfigException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes, Scanners.TypesAnnotated);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        processConfigs(configClasses);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        List<Class<?>> classList = List.of(initialConfigClass);

        classList.forEach(this::checkConfigClass);
        processConfigs(classList);
    }

    private void processConfigs(Collection<Class<?>> configClasses) {
        configClasses.stream()
                .sorted(Comparator.comparingInt(e -> e.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(e -> e.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(e -> e.getAnnotation(AppComponent.class).order()))
                .toList();

        if (!methods.isEmpty()) {
            processMethodConfig(configClass, methods);
        }

        // You code here...
    }

    private void processMethodConfig(Class<?> configClass, List<Method> methods) {

        try {
            Object cofInstance = configClass.getDeclaredConstructor().newInstance();
            methods.forEach(e -> invokeMethodAndPutComponent(cofInstance, e));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new ConfigException(
                    String.format("Can't create bean %s with constructor.", configClass.getName()), e);
        }
    }

    private void invokeMethodAndPutComponent(Object cofInstance, Method method) {

        try {
            Class<?>[] types = method.getParameterTypes();
            Object[] methodArguments = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                methodArguments[i] = getAppComponent(types[i]);
            }

            Object component = method.invoke(cofInstance, methodArguments);
            String methodName = method.getAnnotation(AppComponent.class).name();

            if (!appComponentsByName.containsKey(methodName)) {
                appComponents.add(component);
                appComponentsByName.put(methodName, component);
            }


        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ConfigException(
                    String.format("Can't invoke config %s method  %s", cofInstance.getClass().getName(), method.getName()), e);
        }

    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("The class isn't config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findAny()
                .orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
