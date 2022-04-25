package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementMixIn;

import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    private final ObjectMapper objectMapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        objectMapper = new ObjectMapper()
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .addMixIn(Measurement.class, MeasurementMixIn.class);
    }

    @Override
    public List<Measurement> load() throws IOException {

        var resource = Measurement.class.getClassLoader().getResource(fileName);


        var measurements = objectMapper
                .readValue(resource,
                        new TypeReference<List<Measurement>>() {});
        //читает файл, парсит и возвращает результат

        return measurements;
    }
}
