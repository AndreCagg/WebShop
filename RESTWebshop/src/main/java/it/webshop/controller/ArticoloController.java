package it.webshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.webshop.service.ArticoloServ;
import it.webshop.service.DettListinoServ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import it.webshop.dto.ArticoloDTO;


@CrossOrigin(origins = "http://localhost:9090", allowCredentials = "true")

@RestController
@RequestMapping("/api/v1.0/articoli")
public class ArticoloController {
	@Autowired
	private ArticoloServ servArticolo;
	
	@Autowired
	private DettListinoServ servDettlistino;
	
	
	@GetMapping(produces="application/json")
	public Mono<List<ArticoloDTO>> getArticoli(
	        @RequestParam("filter") String filter,
	        @RequestParam("status") int status,
	        @RequestParam("rows") int rows,
	        @RequestParam("pageToGo") int pageToGo) {

	        Flux<ArticoloDTO> artList = (status > 0
	                ? this.servArticolo.getArticoli(filter, status, rows, pageToGo)
	                : this.servArticolo.getArticoli(filter, rows, pageToGo));
	        
	        //unisco gli articoli con i loro listino
	        return artList.collectList().flatMap(articoli ->{
	        	List<String> ids=articoli.stream().map(ArticoloDTO::getCodart).toList();
	        	if(ids.size()>0) {
		        	return this.servDettlistino.getPrezziListini(ids).map(prezziMap ->{
		        		articoli.forEach(art->art.setPrezzoListini(prezziMap.get(art.getCodart())!=null?prezziMap.get(art.getCodart()):new ArrayList<>()));
		        		return articoli;
		        	});
	        	}else {
	        		return Mono.just(new ArrayList<ArticoloDTO>());
	        	}
	        });
	}
	
	@GetMapping(produces="application/json", value="/{id}")
	public Mono<ResponseEntity<ArticoloDTO>> getArticolo(@PathVariable("id") String id){
		return this.servArticolo.getArticolo(id).map(art -> ResponseEntity.ok(art));
	}

	
	@PostMapping(consumes="application/json")
	public Mono<ResponseEntity<String>> inserisciArticolo(@RequestBody ArticoloDTO body) {
		return this.servArticolo.salvaArticolo(body, true)
		        .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("OK"))
		        .onErrorResume(e ->
		            Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("FAIL " + e.getMessage()))
		        );
	}
	
	@PutMapping(consumes="application/json")
	public Mono<ResponseEntity<String>> aggiornaArticolo(@RequestBody ArticoloDTO body) {
		return this.servArticolo.salvaArticolo(body, false)
		        .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("OK"))
		        .onErrorResume(e ->
		            Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("FAIL " + e.getMessage()))
		        );
	}

	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<String>> rimuoviArticolo(@PathVariable("id") String codart){
		return this.servArticolo.rimuoviArticolo(codart).thenReturn(ResponseEntity.ok("EXECUTED"));		
	}
}
