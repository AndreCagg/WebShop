package it.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.webshop.entity.Famassort;
import it.webshop.service.FamAssServ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/v1.0/famass")
public class FamAssController {
	@Autowired
	private FamAssServ servFamAss;
	
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Famassort>>> getFamAss() {
	    Flux<Famassort> famAssFlux = servFamAss.getFamAssort();
	    //return Mono.just(ResponseEntity.ok(famAssFlux));
	    
	    return famAssFlux.count().map(f ->
	    f>0 ? ResponseEntity.ok(famAssFlux)
	    		: ResponseEntity.noContent().build());
	}
}
