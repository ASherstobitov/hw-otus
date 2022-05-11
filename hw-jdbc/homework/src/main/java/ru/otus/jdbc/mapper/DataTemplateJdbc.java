package ru.otus.jdbc.mapper;

import lombok.SneakyThrows;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

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

                List <Field>allFields = entitySQLMetaData.getEntityClassMetaData().getAllFields();
                Object[] args = new Object[allFields.size()];
                T instance = null;


                if (rs.next()) {

                for (int i = 0; i < allFields.size(); i++) {

                    if (allFields.get(i).getType().equals(Long.class)) {
                        args[i] = rs.getLong(allFields.get(i).getName());
                    } else {
                        args[i] = rs.getString(allFields.get(i).getName());
                    }

                }
                    instance = constructor.newInstance(args);
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

        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public long insert(Connection connection, T client) {
        try {
            List<Field> fieldsWithoutId = entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId();

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

            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), list);


        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
//            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
//                    List.of(client.getName(), client.getId()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
