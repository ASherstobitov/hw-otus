package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {

    private List<String> data;

    public ObjectForMessage(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage(ObjectForMessage objectForMessage) {
        this(objectForMessage.getData());
    }

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data) {
        this.data = new ArrayList<>(data);
    }
}
