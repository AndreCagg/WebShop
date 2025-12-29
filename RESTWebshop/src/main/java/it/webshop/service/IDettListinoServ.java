package it.webshop.service;

import java.util.List;
import java.util.Map;

import it.webshop.dto.DettListinoDTO;
import it.webshop.entity.Dettlistino;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDettListinoServ {
	public Flux<DettListinoDTO> selDettListinoByCodart(String codart);
	public Flux<DettListinoDTO> getDettListini();
	public Mono<Integer> getIdDettListino(String codart, String idlist);
	public Mono<Void> removeById(Integer id);
	public Mono<Dettlistino> salva(Dettlistino d);
	public Mono<Dettlistino> salva(DettListinoDTO d, String codart);
	public Mono<Map<String, List<DettListinoDTO>>> getPrezziListini(List<String> ids);
}
