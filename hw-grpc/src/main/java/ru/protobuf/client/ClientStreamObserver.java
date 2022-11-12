package ru.protobuf.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.protobuf.NumberResponse;
import ru.protobuf.server.NumberServer;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {

    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private long lastValue = 0;
    public synchronized long getLastValueAndReset() {
        var lastValuePrev = this.lastValue;
        this.lastValue = 0;
        return lastValuePrev;
    }

    @Override
    public void onNext(NumberResponse value) {
        log.info("new value:{}", value.getNumber());
        setLastValue(value.getNumber());
    }

    @Override
    public void onError(Throwable e) {
        log.error("got error", e);
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
    }

    public synchronized void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }
}
