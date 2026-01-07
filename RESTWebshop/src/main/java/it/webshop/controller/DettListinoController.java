package it.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.webshop.dto.DettListinoDTO;
import it.webshop.service.DettListinoServ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/dettlistini")
@CrossOrigin(origins="http://localhost:9090", allowCredentials = "true")
public class DettListinoController {
	@Autowired
	private DettListinoServ servDettlistino;
	
	@GetMapping("/{codart}")
	public Mono<ResponseEntity<Flux<DettListinoDTO>>> getDettlistinoByCodart(@PathVariable("codart") String codart){
		Flux<DettListinoDTO> dett=this.servDettlistino.selDettListinoByCodart(codart);
		
		
		return dett.count().map(c ->
			c>0 ? ResponseEntity.ok(dett) 
					: ResponseEntity.noContent().build());
	}
}
