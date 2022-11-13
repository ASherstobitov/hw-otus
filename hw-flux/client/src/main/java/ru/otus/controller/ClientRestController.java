package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.model.Client;

import java.util.List;

@RestController
public class ClientRestController {

    private static final Logger log = LoggerFactory.getLogger(ClientRestController.class);
    private final WebClient webClient;

    public ClientRestController(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @GetMapping("/api/client")
    public List<Client> getAllClients() {

        Mono<List<Client>> clientMono = webClient.get()
                .uri("/api/client")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Client>>() {})
                .doOnNext(val -> log.info("val:{}", val));

        return clientMono.block();
    }

    @GetMapping(value = "/api/client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Client> getClientById(@PathVariable(name = "id") long id) {

        return webClient.get().uri(String.format("/api/client/%d", id))
                .retrieve()
                .bodyToMono(Client.class)
                .doOnNext(val -> log.info("val:{}", val));
    }

    @PostMapping(value ="/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Client> saveClient(@RequestBody Client client) {
        Mono<Client> clientMono = webClient.post()
                .uri("/api/client")
                .body(Mono.just(client), Client.class)
                .retrieve()
                .bodyToMono(Client.class)
                .doOnNext(val -> log.info("val:{}", val));

        return clientMono;
    }
}