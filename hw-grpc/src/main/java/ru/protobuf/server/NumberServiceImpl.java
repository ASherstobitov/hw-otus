package ru.protobuf.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.protobuf.NumberRequest;
import ru.protobuf.NumberResponse;
import ru.protobuf.NumbersServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NumberServiceImpl.class);


    @Override
    public void number(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("request for the new sequence of numbers, firstValue:{}, lastValue:{}",
                request.getFirstValue(), request.getLastValue());

        var currentValue = new AtomicLong(request.getFirstValue());

        var executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            var value = currentValue.incrementAndGet();
            var response = NumberResponse.newBuilder().setNumber(value).build();
            responseObserver.onNext(response);
            if (value == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("sequence of numbers finished");
            }
        };
        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }
}
