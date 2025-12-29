package it.webshop.service;


import it.webshop.entity.Listino;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IListinoServ {
	public Mono<Listino> selListinoById(String id);
	public Flux<Listino> getListini();
}
