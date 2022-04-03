package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.MeasurementConverter;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    private final ObjectMapper objectMapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        objectMapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() throws IOException {

        var measurements = objectMapper
                .readValue(new File("C:\\Users\\iStore\\IdeaProjects\\otus_java_2021_12\\L16-io\\homework\\src\\test\\resources\\" + fileName),
                        new TypeReference<List<MeasurementWrapper>>() {});
        //читает файл, парсит и возвращает результат

        return measurements.stream()
                .map(MeasurementConverter::wrapperMeasurementToMeasurement)
                .toList();
    }
}
