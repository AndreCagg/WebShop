package it.webshop.entity;

import org.springframework.data.annotation.Id;

public class Iva {
	private Integer idiva;
	private String descrizione;
	private Integer aliquota;
	
	@Id
	public Integer getIdiva() {
		return idiva;
	}
	
	public void setIdiva(Integer idiva) {
		this.idiva = idiva;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public Integer getAliquota() {
		return aliquota;
	}
	
	public void setAliquota(Integer aliquota) {
		this.aliquota = aliquota;
	}
}
