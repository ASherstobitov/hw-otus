package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.model.Client;

import java.util.List;


@Controller
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final WebClient webClient;

    public ClientController(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @GetMapping({"/", "/clients"})
    public String clientsListView(Model model) {

        Mono<List<Client>> clientMono = webClient.get()
                .uri("/api/client")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Client>>() {})
                .doOnNext(val -> log.info("val:{}", val));

        List<Client> clients = clientMono.block();
        model.addAttribute("clients", clients);
        return "clients";
    }
}
