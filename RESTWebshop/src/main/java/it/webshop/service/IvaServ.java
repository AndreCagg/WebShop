package it.webshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.webshop.entity.Iva;
import it.webshop.repo.IIvaRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IvaServ implements IIvaServ{
	@Autowired
	private IIvaRepo repoIva;

	@Override
	public Flux<Iva> getIva() {
		return this.repoIva.findAll();
	}

	@Override
	public Mono<Iva> getIva(Integer id) {
		return this.repoIva.findById(id);
	}

}
