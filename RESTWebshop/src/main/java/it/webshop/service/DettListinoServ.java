package it.webshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.webshop.dto.DettListinoDTO;
import it.webshop.entity.Dettlistino;
import it.webshop.repo.IDettListinoRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DettListinoServ implements IDettListinoServ {
	@Autowired
	private IDettListinoRepo repoDettListino;
	
	@Autowired
	private ModelMapper mm;
	
	@Override
	public Flux<DettListinoDTO> selDettListinoByCodart(String codart) {
		return this.repoDettListino.selDettListinoByCodart(codart).map(this::DettListinoToDTO);
	}
	
	@Override
	public Flux<DettListinoDTO> getDettListini() {
		return this.repoDettListino.findAll().map(this::DettListinoToDTO);
	}
	
	@Override
	public Mono<Integer> getIdDettListino(String codart, String idlist) {
		return this.repoDettListino.selByCodartAndIdlist(codart, idlist);
	}
	
	@Override
	public Mono<Void> removeById(Integer id) {
		return this.repoDettListino.deleteById(id);
	}
	
	@Override
	public Mono<Dettlistino> salva(Dettlistino d){
		return this.repoDettListino.save(d);
	}
	
	public Mono<Dettlistino> salva(DettListinoDTO d, String codart){
		Dettlistino dett=this.DTOToDettListino(d);
		dett.setCodart(codart);
		return this.repoDettListino.save(dett);
	}
	
	public Mono<Map<String, List<DettListinoDTO>>> getPrezziListini(List<String> ids){
	    return this.repoDettListino.selDettListinoByCodarts(ids)
	        .collectList() 
	        .map(lst -> {
	            Map<String, List<DettListinoDTO>> map = new HashMap<>();
	            for (Dettlistino d : lst) {
	                List<DettListinoDTO> dett = map.getOrDefault(d.getCodart(), new ArrayList<>());
	                DettListinoDTO obj = new DettListinoDTO();
	                obj.setId(d.getId());
	                obj.setListino(d.getIdlist());
	                obj.setPrezzo(d.getPrezzo());
	                dett.add(obj);
	                map.put(d.getCodart(), dett);
	            }
	            return map;
	        });
	}
	
	private DettListinoDTO DettListinoToDTO(Dettlistino dett) {
		return this.mm.map(dett, DettListinoDTO.class);
	}
	
	private Dettlistino DTOToDettListino(DettListinoDTO dett) {
		return this.mm.map(dett, Dettlistino.class);
	}
}
