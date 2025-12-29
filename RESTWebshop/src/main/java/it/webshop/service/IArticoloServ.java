package it.webshop.service;


import java.util.List;
import java.util.Map;

import it.webshop.dto.ArticoloDTO;
import it.webshop.dto.DettListinoDTO;
import it.webshop.entity.Dettlistino;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IArticoloServ {
	public Flux<ArticoloDTO> getArticoli(String filter, int status, int rows, int pageToGo);
	public Flux<ArticoloDTO> getArticoli(String filter, int rows, int pageToGo);
	public Mono<ArticoloDTO> getArticolo(String id);
	public Mono<Void> salvaArticolo(ArticoloDTO dto, Boolean isNew);
	public Mono<Void> rimuoviArticolo(String codart);
}
