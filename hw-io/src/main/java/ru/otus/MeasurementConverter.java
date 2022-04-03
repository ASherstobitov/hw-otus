package ru.otus;

import ru.otus.model.Measurement;
import ru.otus.model.MeasurementWrapper;

public final class MeasurementConverter {

    public static Measurement wrapperMeasurementToMeasurement(MeasurementWrapper source) {

        return new Measurement(source.getName(), source.getValue());
    }

}
