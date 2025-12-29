package it.webshop.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


import it.webshop.entity.Iva;

public interface IIvaRepo extends ReactiveCrudRepository<Iva, Integer>{
}
