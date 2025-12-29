package it.webshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.webshop.entity.Listino;
import it.webshop.repo.IListinoRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ListinoServ implements IListinoServ{

	@Autowired
	private IListinoRepo repoListino;
	
	@Override
	public Mono<Listino> selListinoById(String id) {
		return this.repoListino.selById(id);
	}

	@Override
	public Flux<Listino> getListini() {
		return this.repoListino.findAll();
	}

}
