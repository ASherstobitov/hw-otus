package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MeasurementMixIn {

    public MeasurementMixIn(@JsonProperty("name")String name,
                            @JsonProperty("value")double value) {
    }
}
