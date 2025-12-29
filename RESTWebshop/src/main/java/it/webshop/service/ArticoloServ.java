package it.webshop.service;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.webshop.dto.ArticoloDTO;
import it.webshop.dto.DettListinoDTO;
import it.webshop.entity.Articolo;
import it.webshop.entity.Dettlistino;
import it.webshop.repo.IArticoloRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ArticoloServ implements IArticoloServ {
	@Autowired
	ModelMapper mm;
	
	@Autowired
	private IArticoloRepo repoArticolo;
	
	@Autowired
	private DettListinoServ servDettListino;
	

	@Override
	public Flux<ArticoloDTO> getArticoli(String filter, int status, int rows, int pageToGo) {
		filter="%"+filter+"%";
		return this.repoArticolo.selByDescrizioneOrIdLikeAndStatus(filter, String.valueOf(status))
				.skip(pageToGo*rows)
				.take(rows)
				.map(this::ArticoloToDTO);
		
	}
	
	@Override
	public Flux<ArticoloDTO> getArticoli(String filter, int rows, int pageToGo) {
		filter="%"+filter+"%";
		
		return this.repoArticolo.selByDescrizioneOrIdLike(filter)
				.skip(pageToGo*rows)
				.take(rows)
				.map(this::ArticoloToDTO);
		
	}
	
	@Override
	public Mono<Void> salvaArticolo(ArticoloDTO dto, Boolean isNew) {

	    Articolo art = DTOToArticolo(dto);
	    art.setDatacreazione(LocalDate.now());
	    if(art.getIdiva()==-1)
	    	art.setIdiva(null);
	    art.setIsNew(isNew);

	    return this.repoArticolo.save(art)
	            .flatMap(savedArt -> {
	                return Flux.fromIterable(dto.getPrezzoListini())
	                        .flatMap(d -> salvaDettListino(savedArt.getCodart(), d))
	                        .then();
	            });
	    
	}

	 private Mono<Void> salvaDettListino(String codart, DettListinoDTO dto) {

        if (dto.getPrezzo() == null || dto.getPrezzo() == 0) {
            if (dto.getId() != null) {
                return servDettListino.removeById(dto.getId());
            }
            return Mono.empty();
        }
        
        Dettlistino dett = mm.map(dto, Dettlistino.class);
        dett.setCodart(codart); 
        dett.setPrezzo(dto.getPrezzo());
        if(dto.getId()!=null) {
        	dett.setId(dto.getId());
        }

        return this.servDettListino.salva(dett).then();
    }
	
	@Override
	public Mono<Void>rimuoviArticolo(String codart) {
		return this.repoArticolo.softRemove(codart);
	}
	
	@Override
	public Mono<ArticoloDTO> getArticolo(String id) {
		return this.repoArticolo.findById(id).map(this::ArticoloToDTO);
	}
	
	
	private ArticoloDTO ArticoloToDTO(Articolo art) {
		return this.mm.map(art, ArticoloDTO.class);
	}
	
	private Articolo DTOToArticolo(ArticoloDTO dto) {
		Articolo art=this.mm.map(dto, Articolo.class);
		
		return art;
	}
}
