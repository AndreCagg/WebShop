package it.webshop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.webshop.entity.Listino;
import it.webshop.service.ListinoServ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin(origins="http://localhost:9090", allowCredentials = "true")
@RequestMapping("/api/v1.0/listini")
public class ListinoController {
	@Autowired
	private ListinoServ servListino;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Listino>>> getListini(){
		Flux<Listino> lst=this.servListino.getListini();
		//return Mono.just(ResponseEntity.ok(lst));
		
		return lst.count().map(l ->
	    l>0 ? ResponseEntity.ok(lst)
	    		: ResponseEntity.noContent().build());
	}
	
	@GetMapping("/{codart}")
	public Mono<ResponseEntity<Flux<Listino>>> getListini(@PathVariable("codart") String codart){
		Flux<Listino> lst=this.servListino.getListini();
		//return Mono.just(ResponseEntity.ok(lst));
		
		return lst.count().map(l ->
	    l>0 ? ResponseEntity.ok(lst)
	    		: ResponseEntity.noContent().build());
	}
}
