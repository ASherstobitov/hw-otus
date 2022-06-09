package ru.otus.jdbc.mapper;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> tClass;

    private final HwCache<String, Field> vtMyCache = new MyCache<>();

    public EntityClassMetaDataImpl(Class<T> tClass) {
       this.tClass = tClass;
    }


    @Override
    public String getName() {
        return getGenericType().getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {

        Class<?>[] fieldType = getAllFields().stream()
                .map(e -> e.getType())
                .toArray(Class<?>[]::new);

        return tClass.getDeclaredConstructor(fieldType);
    }

    @Override
    public Field getIdField() {
        return getAllFields()
                .stream()
                .filter(e -> e.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(getGenericType().getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields()
                .stream()
                .filter(e -> !e.isAnnotationPresent(Id.class))
                .toList();
    }

    @Override
    public Class<T> getGenericType() {
        return tClass;
    }
}
