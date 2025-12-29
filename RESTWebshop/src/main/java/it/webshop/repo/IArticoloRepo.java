package it.webshop.repo;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import it.webshop.entity.Articolo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IArticoloRepo extends ReactiveCrudRepository<Articolo, String>{
	
	@Query("SELECT * FROM articolo AS a WHERE (UPPER(a.descrizione) LIKE UPPER(:filter)) OR (a.codart LIKE :filter) ORDER BY idstatoart, descrizione")
	public Flux<Articolo> selByDescrizioneOrIdLike(@Param("filter") String filter);
	
	@Query("SELECT * FROM articolo AS a WHERE ((UPPER(a.descrizione) LIKE UPPER(:filter)) OR (a.codart LIKE :filter)) AND (a.idstatoart=:status) ORDER BY idstatoart, descrizione")
	public Flux<Articolo> selByDescrizioneOrIdLikeAndStatus(@Param("filter") String filter, @Param("status") String stato);
	
	@Modifying
	@Query("UPDATE articolo SET idstatoart='3' WHERE codart=:id")
	public Mono<Void> softRemove(@Param("id") String id);
}
