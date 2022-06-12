package ru.otus.jdbc.mapper;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> tClass;

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
                .map(Field::getType)
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
        var fields = Arrays.asList(getGenericType().getDeclaredFields());
        return fields;
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
        return this.tClass;
    }
}
