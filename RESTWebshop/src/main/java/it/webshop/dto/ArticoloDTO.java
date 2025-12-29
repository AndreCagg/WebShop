package it.webshop.dto;

import java.util.List;


public class ArticoloDTO {
	private String codart;
	private String descrizione;
	private String stato;
	private Double pesonetto;
	private String um;
	private Integer pzcart;
	private Integer idfamass;
	private Integer idiva;
	
	private List<DettListinoDTO> prezzoListini;
	
	public String getCodart() {
		return codart;
	}
	
	public void setCodart(String codart) {
		this.codart = codart;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public String getStato() {
		return stato;
	}
	
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	/*public Map<Integer, Double> getPrezzoListini() {
		return prezzoListini;
	}
	
	public void setPrezzoListini(Map<Integer, Double> prezzoListini) {
		this.prezzoListini = prezzoListini;
	}*/

	public Double getPesonetto() {
		return pesonetto;
	}

	public void setPesonetto(Double pesonetto) {
		this.pesonetto = pesonetto;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public Integer getPzcart() {
		return pzcart;
	}

	public void setPzcart(Integer pzcart) {
		this.pzcart = pzcart;
	}

	public Integer getIdfamass() {
		return idfamass;
	}

	public void setIdfamass(Integer idfamass) {
		this.idfamass = idfamass;
	}

	public Integer getIdiva() {
		return idiva;
	}

	public void setIdiva(Integer idiva) {
		this.idiva = idiva;
	}

	public List<DettListinoDTO> getPrezzoListini() {
		return prezzoListini;
	}

	public void setPrezzoListini(List<DettListinoDTO> prezzoListini) {
		this.prezzoListini = prezzoListini;
	}
}
