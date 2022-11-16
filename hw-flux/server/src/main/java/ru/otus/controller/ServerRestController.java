package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.ClientService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ServerRestController {

    private static final Logger log = LoggerFactory.getLogger(ServerRestController.class);
    private final ClientService clientService;
    private final ExecutorService executor = Executors.newScheduledThreadPool(4);

    public ServerRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Client>> getAllClients() {

        var clientCompletableFuture =
                CompletableFuture.supplyAsync(clientService::findAll);

        return Mono.fromFuture(clientCompletableFuture)
                .doOnNext(val -> log.info("clients: {}",val));
    }

    @GetMapping(value = "/api/client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Client> getClientById(@PathVariable(name = "id") long id) {
        log.info("request for string data, id: {}", id);

        var clientCompletableFuture =
                CompletableFuture.supplyAsync(() -> clientService.findById(id).orElse(null), executor);

        return Mono.fromFuture(clientCompletableFuture)
                .doOnNext(val -> log.info("client: {}, getClientById: {} ", val , val.getId()));
    }

    @PostMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Client> saveClient(@RequestBody Client client) {

        var clientCompletableFuture =
                CompletableFuture.supplyAsync(() -> clientService.saveClient(client));

        return Mono.fromFuture(clientCompletableFuture);
    }
}