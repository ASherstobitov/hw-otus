package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementWrapper {

    private final String name;
    private final double value;

    @JsonCreator
    public MeasurementWrapper(@JsonProperty("name")String name, @JsonProperty("value") double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MeasurementWrapper{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

}
