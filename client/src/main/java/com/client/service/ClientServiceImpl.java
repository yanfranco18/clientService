package com.client.service;

import com.client.models.Client;
import com.client.repository.ClientDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements IClientService{

    private final ClientDao clientDao;

    @Override
    public Flux<Client> findAll() {
        return clientDao.findAll();
    }

    @Override
    public Mono<Client> save(Client client) {
        return clientDao.save(client);
    }

    @Override
    public Mono<Void> delete(String id) {
        return clientDao.deleteById(id);
    }

    @Override
    public Mono<Client> findById(String id) {
        return clientDao.findById(id);
    }

    @Override
    public Flux<Client> findByTypeClient(String typeClient) {
        return clientDao.findByTypeClient(typeClient);
    }
}
