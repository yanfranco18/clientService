package com.client.serviceTest;

import com.client.Data;
import com.client.models.Client;
import com.client.repository.ClientDao;
import com.client.service.ClientServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    @Mock
    private static ClientDao clientDao;
    private static ClientServiceImpl clientService;

    @BeforeAll
    public static void setUp(){
        clientDao = mock(ClientDao.class);
        clientService = new ClientServiceImpl(clientDao);
    }

    @Test
    void getClientTest() {

        Flux<Client> cli = Flux.just(Data.getList());

        when(clientService.findAll()).thenReturn(cli);

        Flux<Client> respBody = clientService.findAll();

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNext(Data.getList())
                .verifyComplete();
    }

    @Test
    void findByIdClientTest() throws Exception{

        Mono<Client> cli = Mono.just(Data.getList());
        when(clientService.findById(any())).thenReturn(cli);

        Mono<Client> respBody = clientService.findById(any());

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getId().equals("12233d"))
                .verifyComplete();
    }

    @Test
    void searchTypeClientTest() throws Exception{
        Flux<Client> cli = Flux.just(Data.getList());
        when(clientService.findByTypeClient(any())).thenReturn(cli);

        Flux<Client> respBody = clientService.findByTypeClient(any());

        StepVerifier.create(respBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getTypeClient().equals("Empresarial"))
                .expectComplete();
    }

    @Test
    void saveClientTest(){

        Client cli = Data.getList();

        when(clientService.save(cli)).thenReturn(Mono.just(cli));

        Mono<Client> respBody = clientService.save(cli);

        StepVerifier.create(respBody)
                .expectSubscription()
                //.expectNext(Data.getList())
                .expectComplete();
    }
}
