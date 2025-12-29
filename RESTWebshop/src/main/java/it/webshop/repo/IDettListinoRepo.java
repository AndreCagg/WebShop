package it.webshop.repo;

import java.util.List;
import org.springframework.data.r2dbc.repository.Query;


/*import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;*/
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


import it.webshop.entity.Dettlistino;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDettListinoRepo extends ReactiveCrudRepository<Dettlistino, Integer>{
	
	@Query("SELECT * FROM dettlistino AS d WHERE d.codart=:codart")
	public Flux<Dettlistino> selDettListinoByCodart(@Param("codart") String id);
	
	@Query("SELECT * FROM dettlistino AS d WHERE d.codart IN (:ids)")
	public Flux<Dettlistino> selDettListinoByCodarts(@Param("ids") List<String> ids);
	
	@Query("SELECT d.id FROM dettlistino AS d WHERE d.codart=:codart AND d.listino.id=:idlist")
	public Mono<Integer> selByCodartAndIdlist(String codart, String idlist);
}
