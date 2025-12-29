package it.webshop.entity;

import org.springframework.data.annotation.Id;

public class Dettlistino {
	private Integer id;
	private Double prezzo;
	
	private String codart;
	
	private String idlist;
	
	@Id
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Double getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public String getCodart() {
		return codart;
	}

	public void setCodart(String codart) {
		this.codart = codart;
	}

	public String getIdlist() {
		return idlist;
	}

	public void setIdlist(String idlist) {
		this.idlist = idlist;
	}
}
