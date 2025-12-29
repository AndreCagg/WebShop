package it.webshop.service;
import it.webshop.entity.Famassort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFamAssServ {
	public Flux<Famassort> getFamAssort();
	public Mono<Famassort> getFamAssort(Integer id);
}
