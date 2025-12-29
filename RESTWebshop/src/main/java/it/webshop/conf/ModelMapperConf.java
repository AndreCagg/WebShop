package it.webshop.conf;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.webshop.dto.ArticoloDTO;
import it.webshop.entity.Articolo;

import it.webshop.entity.Dettlistino;
import it.webshop.dto.DettListinoDTO;

@Configuration
public class ModelMapperConf {
	
	@Bean
	public ModelMapper configure() {
		ModelMapper m=new ModelMapper();
		m.addMappings(articoliDTOMapper);
		m.addMappings(DTOArticoli);
		m.addMappings(dettListinoDTOMapper);
		m.addMappings(DTOdettListinoMapper);
		
		return m;
	}
	
	PropertyMap<Articolo, ArticoloDTO> articoliDTOMapper=new PropertyMap<Articolo, ArticoloDTO>(){
		protected void configure() {
			map().setCodart(source.getCodart());
			map().setDescrizione(source.getDescrizione());
			map().setPesonetto(source.getPesonetto());
			map().setStato(source.getIdstatoart());
			map().setUm(source.getUm());
			map().setIdfamass(source.getIdfamass());
			map().setIdiva(source.getIdiva());
			map().setPzcart(source.getPzcart());
		}
	};
	
	PropertyMap<ArticoloDTO, Articolo> DTOArticoli=new PropertyMap<ArticoloDTO, Articolo>(){
		protected void configure() {
			map().setCodart(source.getCodart());
			map().setDescrizione(source.getDescrizione());
			map().setPesonetto(source.getPesonetto());
			map().setIdstatoart(source.getStato());
			map().setUm(source.getUm());
			map().setIdfamass(source.getIdfamass());
			map().setIdiva(source.getIdiva());
			map().setPzCart(source.getPzcart());
		}
	};
	
	PropertyMap<Dettlistino, DettListinoDTO> dettListinoDTOMapper=new PropertyMap<Dettlistino, DettListinoDTO>(){
		protected void configure() {
			map().setListino(source.getIdlist());
			map().setPrezzo(source.getPrezzo());
			map().setId(source.getId());
		}
	};
	
	PropertyMap<DettListinoDTO, Dettlistino> DTOdettListinoMapper=new PropertyMap<DettListinoDTO, Dettlistino>(){
		protected void configure() {
			map().setPrezzo(source.getPrezzo());
			map().setId(source.getId());
			map().setIdlist(source.getListino());
		}
	};
}
