package ru.protobuf.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.protobuf.NumberRequest;
import ru.protobuf.NumbersServiceGrpc;

public class NumbersClient {

    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int LOOP_LIMIT = 50;

    private long value = 0;

    public static void main(String[] args) throws InterruptedException {

        log.info("numbers Client is starting...");


        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var asyncClient = NumbersServiceGrpc.newStub(channel);

        new NumbersClient().clientAction(asyncClient);

        log.info("numbers Client is shut downed");
        channel.shutdownNow();
    }

    private void clientAction(NumbersServiceGrpc.NumbersServiceStub asyncClient) {
        var numberRequest = makeNumberRequest();
        var clientStreamObserver = new ClientStreamObserver();
        asyncClient.number(numberRequest, clientStreamObserver);

        for (var idx = 0; idx < LOOP_LIMIT; idx++) {
           var valForPrint = getNextValue(clientStreamObserver);
           log.info("currentValue:{}", valForPrint);
           sleep();
        }
    }

    private NumberRequest makeNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(1)
                .setLastValue(10)
                .build();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long getNextValue(ClientStreamObserver clientStreamObserver) {
        value = value + clientStreamObserver.getLastValueAndReset() + 1;
        return value;
    }
}
