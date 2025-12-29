package it.webshop.service;

import it.webshop.entity.Iva;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IIvaServ {
	public Flux<Iva> getIva();
	public Mono<Iva> getIva(Integer id);
}
