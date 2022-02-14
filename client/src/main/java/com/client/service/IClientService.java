package com.client.service;

import com.client.models.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {

    public Flux<Client> findAll();

    public Mono<Client> save(Client client);

    public Mono<Void> delete(Client client);

    public Mono<Client> findById(String id);

    public Flux<Client> findByTypeClient(String typeClient);
}
