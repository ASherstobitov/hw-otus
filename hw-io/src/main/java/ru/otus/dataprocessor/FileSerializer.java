package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class FileSerializer implements Serializer {

    private ObjectMapper objectMapper;

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {

        objectMapper.writeValue(new File(fileName), new TreeMap<>(data));

        //формирует результирующий json и сохраняет его в файл
    }
}
