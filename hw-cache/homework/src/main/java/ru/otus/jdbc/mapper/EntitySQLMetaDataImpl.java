package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?", entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        var format = String.format("insert into %s(", entityClassMetaData.getName().toLowerCase());

        var fieldNames = getFiledNames(entityClassMetaData.getFieldsWithoutId());

        String symbols = getSymbols(entityClassMetaData.getFieldsWithoutId().size(), "?");

        return format.concat(fieldNames).concat(") values(").concat(symbols).concat(")");
    }

    @Override
    public String getUpdateSql() {
        var format = String.format("update %s set ", entityClassMetaData.getName().toLowerCase());

        List<Field> fields = entityClassMetaData.getFieldsWithoutId();

        String symbols = fields.stream()
                .map(e -> String.format("%s = ?", e.getName()))
                .collect(Collectors.joining(", "));

        String whereState = String.format(" where %s = ?", entityClassMetaData.getIdField()
                .getName().toLowerCase());

        return format.concat(symbols).concat(whereState);
    }

    private String getFiledNames(List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));
    }

    @Override
    public EntityClassMetaData getEntityClassMetaData() {
        return this.entityClassMetaData;
    }

    private String getSymbols(int size, String value) {
        return Stream.generate(() -> value)
                .limit(size)
                .collect(Collectors.joining(", "));
    }
}
