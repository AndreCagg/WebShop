package it.webshop.entity;

import org.springframework.data.annotation.Id;

public class Famassort {
	private Integer id;
	private String descrizione;
	
	@Id
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
