package com.client.controllers;

import com.client.models.Client;
import com.client.service.IClientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final IClientService clientService;

    //@CircuitBreaker, name(va el nombre de la instancia usado en la configuracion yml, "items")
    //fallbackMethod, permite manejar el error, mediante un metodo definido
    //anotacion para el timeout - @TimeLimiter name(va el nombre de la instancia usado en la configuracion yml, "items")
    //ComplatebleFuture<"tipo">, es envolver una llamada asincrona, represeta futura que ocurre en el tiempo, maneja un generic
    //supplyAsync, permite envolver la llamada en una futura asincrona del tiempo
    //metodo buscar por typeProduct
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @GetMapping("/search/{typeClient}")
    public Flux<Client> searchType(@PathVariable String typeClient){
        return clientService.findByTypeClient(typeClient);
    }

    //Metodo listar, usando response entity para manejar la respuesta del status y la respuesta del body
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @GetMapping
    public Mono<ResponseEntity<Flux<Client>>> getClient(){
        log.info("iniciando lista");
        return Mono.just(
                //manejo de la respuesta http
                ResponseEntity.ok()
                        //mostrar en el body mediante json
                        .contentType(MediaType.APPLICATION_JSON)
                        //mostrando en el body la respuesta
                        .body(clientService.findAll()));
    }

    //Metodo para eliminar
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete (@PathVariable String id){

        return clientService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    //Metodo para editar, pasamos por el requestBody el client a modificar
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Client>> edit(@RequestBody Client clients, @PathVariable String id){
        //buscamos el id para obtener el client
        return clientService.findById(id)
                //A traves del flatMap actualizamos los campos para modificar
                .flatMap(p ->{
                    p.setNameClient(clients.getNameClient());
                    p.setTypeClient(clients.getTypeClient());
                    p.setPhoneNumber(clients.getPhoneNumber());
                    return clientService.save(p);
                })
                //Utilizando el Map cambiamos la respuesta de Mono a un ResponseEntity
                //mediante created pasamos la uri, y con concat concatenemos el id
                .map(p -> ResponseEntity.created(URI.create("/clients/".concat(p.getId())))
                        //Modificamos la respeusta en el body con el contentType
                        .contentType(MediaType.APPLICATION_JSON)
                        //Y pasamos el cliente modificado
                        .body(p))
                //para manejar el error si el cliente no existe, y build para generar la respuesta sin cuerpo
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //metodo crear
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @PostMapping
    public Mono<ResponseEntity<Client>> create(@RequestBody Client clients){
        //validamos la fecha en caso venga fecha, asigamos la fecha
        if(clients.getCreateDate()==null){
            clients.setCreateDate(new Date());
        }
        //ahora guardamos el cliente, mediante map, cambiamos el flujo de tipo mono a un responseEntity
        return clientService.save(clients)
                //mostramos el estado en el http, indicamos la uri del producto se crea
                .map(p -> ResponseEntity.created(URI.create("/clients/".concat(p.getId())))
                        //Modificamos la respuesta en el body con el contentType
                        .contentType(MediaType.APPLICATION_JSON)
                        //Y pasamos el cliente creado
                        .body(p));
    }

    //metodo buscar por id
    @CircuitBreaker(name="clients", fallbackMethod = "fallback")
    @TimeLimiter(name="clients")
    @GetMapping("/getById/{id}")
    public Mono<ResponseEntity<Client>> getById(@PathVariable String id){
        return clientService.findById(id)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //metodo para manejar el error
    private String fallback(HttpServerErrorException ex) {
        return "Response 200, fallback method for error:  " + ex.getMessage();
    }

}
