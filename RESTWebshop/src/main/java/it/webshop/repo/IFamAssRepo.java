package it.webshop.repo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


import it.webshop.entity.Famassort;

public interface IFamAssRepo extends ReactiveCrudRepository<Famassort, Integer>{

}
