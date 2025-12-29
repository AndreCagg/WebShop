package it.webshop.entity;

import org.springframework.data.annotation.Id;

public class Listino {
	private String id;
	private String descrizione;
	private String obsoleto;
		
	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public String getObsoleto() {
		return obsoleto;
	}
	
	public void setObsoleto(String obsoleto) {
		this.obsoleto = obsoleto;
	}
}
