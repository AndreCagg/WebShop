package it.webshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.webshop.entity.Famassort;
import it.webshop.repo.IFamAssRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FamAssServ implements IFamAssServ{

	@Autowired
	private IFamAssRepo repoFamAss;
	
	@Override
	public Flux<Famassort> getFamAssort() {
		return this.repoFamAss.findAll();
	}

	@Override
	public Mono<Famassort> getFamAssort(Integer id) {
		return this.repoFamAss.findById(id);
	}

}
