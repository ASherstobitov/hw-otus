package ru.otus.jdbc.mapper;

import lombok.SneakyThrows;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {

            try {
                Constructor<T> constructor = entitySQLMetaData.getEntityClassMetaData().getConstructor();

                List<Field> allFields = entitySQLMetaData.getEntityClassMetaData().getAllFields();

                T instance = null;

                if (rs.next()) {
                    instance = constructor.newInstance(getArgs(rs, allFields));
                }

                return instance;

            } catch (SQLException | NoSuchMethodException | InstantiationException |
                    InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("Ошибка при создании объекта!", e);
            }

        });
    }

    @Override
    public List<T> findAll(Connection connection) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();


            try {
                Constructor<T> constructor = entitySQLMetaData.getEntityClassMetaData().getConstructor();
                List<Field> allFields = entitySQLMetaData.getEntityClassMetaData().getAllFields();

                while (rs.next()) {

                    T instance = constructor.newInstance(getArgs(rs, allFields));

                    clientList.add(instance);
                }
                return clientList;
            } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private Object[] getArgs(java.sql.ResultSet rs, List<Field> allFields) throws SQLException {
        Object[] args = new Object[allFields.size()];
        for (int i = 0; i < allFields.size(); i++) {

            if (allFields.get(i).getType().equals(Long.class)) {
                args[i] = rs.getLong(allFields.get(i).getName());
            } else {
                args[i] = rs.getString(allFields.get(i).getName());
            }

        }
        return args;
    }

    @Override
    @SneakyThrows
    public long insert(Connection connection, T client) {
        try {
            List<Field> fieldsWithoutId = entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId();

            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldValues(client, fieldsWithoutId));


        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValues(T client, List<Field> fieldsWithoutId) {
        List<Object> list = new ArrayList<>();

        fieldsWithoutId.forEach(e -> {

            try {

                Field field = client.getClass().getDeclaredField(e.getName());
                field.setAccessible(true);
                Object o1 = field.get(client);

                list.add(o1);
            } catch (Exception illegalAccessException) {
                illegalAccessException.printStackTrace();
            }


        });
        return list;
    }

    @Override
    public void update(Connection connection, T client) {
        try {

            List<Field> fieldsWithoutId = entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId();

            List<Object> fieldValues = getFieldValues(client, fieldsWithoutId);

            Field idField = client.getClass().getDeclaredField(entitySQLMetaData.getEntityClassMetaData().getIdField().getName());

            idField.setAccessible(true);
            Object o1 = idField.get(client);
            fieldValues.add(o1);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldValues);


        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
