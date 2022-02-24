package com.client.controllerTest;

import com.client.Data;
import com.client.controllers.ClientController;
import com.client.models.Client;
import com.client.service.ClientServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    private static WebTestClient webTestClient;
    @Mock
    private static ClientServiceImpl clientService;

    @BeforeAll
    public static void setUp(){
        clientService = mock(ClientServiceImpl.class);
        webTestClient = WebTestClient.bindToController(new ClientController(clientService))
                .configureClient()
                .baseUrl("/clients")
                .build();
    }

    @Test
    void getClientTest() {

        Flux<Client> cli = Flux.just(Data.getList());

        when(clientService.findAll()).thenReturn(cli);

        Flux<Client> respBody = webTestClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() //200
                .returnResult(Client.class)
                .getResponseBody();

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNext(Data.getList())
                .verifyComplete();
    }

    @Test
    void saveClientTest(){

        Client cli = Data.getList();

        when(clientService.save(cli)).thenReturn(Mono.just(cli));

        webTestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(cli), Client.class)
                .exchange()
                .expectStatus().isCreated(); //201
    }

    @Test
    void deleteClientTest() throws Exception{

        when(clientService.delete("12233d")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/12233d")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void findByIdClientTest() throws Exception{

        Mono<Client> cli = Mono.just(Data.getList());
        when(clientService.findById(any())).thenReturn(cli);

        Flux<Client> respBody = webTestClient.get().uri("/getById/12233d")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Client.class)
                .getResponseBody();

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getId().equals("12233d"))
                .verifyComplete();
    }


    @Test
    void searchTypeClientTest() throws Exception{
        Flux<Client> cli = Flux.just(Data.getList());
        when(clientService.findByTypeClient(any())).thenReturn(cli);

        Flux<Client> respBody = webTestClient.get().uri("/search/Empresarial")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Client.class)
                .getResponseBody();

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getTypeClient().equals("Empresarial"))
                .expectComplete();
    }
}
