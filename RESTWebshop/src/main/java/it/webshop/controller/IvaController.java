package it.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.webshop.entity.Iva;
import it.webshop.service.IIvaServ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v1.0/iva")
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
public class IvaController {
	@Autowired
	private IIvaServ servIva;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Iva>>> getIva(){
		Flux<Iva> iva=this.servIva.getIva();
		
		//return Mono.just(ResponseEntity.ok(iva));
		return iva.count().map(i ->
	    i>0 ? ResponseEntity.ok(iva)
	    		: ResponseEntity.noContent().build());
	}
	
}
