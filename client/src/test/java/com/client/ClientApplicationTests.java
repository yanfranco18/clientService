package com.client;

import com.client.controllers.ClientController;
import com.client.models.Client;
import com.client.service.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ClientController.class)
class ClientApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ClientServiceImpl service;

	@Test
	void getClientTest() {

		Flux<Client> cli = Flux.just(new Client("12233d", "Empresa Agp", "Empresarial", "24353456765", "4564567",new Date(2022-02-16)),
				new Client("12245f", "Yan Loayza", "Personal", "45061936","987654324",new Date(2022-02-16)));
		when(service.findAll()).thenReturn(cli);

		Flux<Client> respBody = webTestClient.get().uri("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Client.class)
				.getResponseBody();

		StepVerifier.create(respBody)
				.expectSubscription()
				.expectNext(new Client("12233d", "Empresa Agp", "Empresarial", "24353456765", "4564567",new Date(2022-02-16)))
				.expectNext(new Client("12245f", "Yan Loayza", "Personal", "45061936","987654324",new Date(2022-02-16)))
				.verifyComplete();
	}

	@Test
	public void saveClientTest(){

		Client cli = new Client("12245f", "Junit - Mockito", "Empresarial", "45888886","900654324",new Date());

		when(service.save(cli)).thenReturn(Mono.just(cli));

		webTestClient.post().uri("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(cli), Client.class)
				.exchange()
				.expectStatus().isCreated();
	}

	@Test
	public void deleteClientTest() throws Exception{

		when(service.delete("12233d")).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/clients/12233d")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void findByIdClientTest() throws Exception{

		Mono<Client> products = Mono.just(new Client("12233d", "Empresa Agp", "Empresarial", "24353456765", "4564567",new Date()));
		when(service.findById(any())).thenReturn(products);

		Flux<Client> respBody = webTestClient.get().uri("/clients/getById/12233d")
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
	public void searchTypeProductTest() throws Exception{
		Flux<Client> cli = Flux.just(new Client("12233d", "Empresa Agp", "Empresarial", "24353456765", "4564567",new Date(2022-02-16)),
				new Client("12245f", "Yan Loayza", "Personal", "45061936","987654324",new Date(2022-02-16)));
		when(service.findByTypeClient(any())).thenReturn(cli);

		Flux<Client> respBody = webTestClient.get().uri("/clients/search/Personal")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Client.class)
				.getResponseBody();

		StepVerifier.create(respBody)
				.expectSubscription()
				.expectNextMatches(c -> c.getTypeClient().equals("Personal"))
				.expectComplete();
	}

}
