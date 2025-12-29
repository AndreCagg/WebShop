package it.webshop.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;

import it.webshop.entity.Listino;
import reactor.core.publisher.Mono;

public interface IListinoRepo extends ReactiveCrudRepository<Listino, String> {
	@Query(value="SELECT * FROM listini AS l WHERE l.id=:id")
	public Mono<Listino> selById(@Param("id") String id);
}
