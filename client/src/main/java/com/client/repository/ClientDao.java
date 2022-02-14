package com.client.repository;

import com.client.models.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ClientDao extends ReactiveMongoRepository<Client, String> {

    //devuelve por tipo
    public Flux<Client> findByTypeClient(String typeClient);

}
