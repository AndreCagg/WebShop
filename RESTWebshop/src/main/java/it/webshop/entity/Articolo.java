package it.webshop.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public class Articolo implements Persistable<String>{
	private String codart;
	private String descrizione;
	private String um;
	private String codstat;
	private Integer pzcart;
	private Double pesonetto;
	private Integer idiva;
	private String idstatoart;
	private LocalDate datacreazione;
	private Boolean isNew;

	private Integer idfamass;
	
	
	
	@Id
	public String getCodart() {
		return codart;
	}
	

	public Integer getIdiva() {
		return idiva;
	}
	
	public void setIdiva(Integer iva) {
		this.idiva=iva;
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
	
	public String getUm() {
		return um;
	}
	
	public void setUm(String um) {
		this.um = um;
	}
	
	public String getCodstat() {
		return codstat;
	}
	
	public void setCodstat(String codstat) {
		this.codstat = codstat;
	}
	
	public Integer getPzcart() {
		return pzcart;
	}
	
	public void setPzCart(Integer pzcart) {
		this.pzcart = pzcart;
	}
	
	public Double getPesonetto() {
		return pesonetto;
	}
	
	public void setPesonetto(Double pesonetto) {
		this.pesonetto = pesonetto;
	}
	
	public String getIdstatoart() {
		return idstatoart;
	}
	
	public void setIdstatoart(String idstatoart) {
		this.idstatoart = idstatoart;
	}

	public LocalDate getDatacreazione() {
		return datacreazione;
	}
	
	public void setDatacreazione(LocalDate datacreazione) {
		this.datacreazione = datacreazione;
	}

	public Integer getIdfamass() {
		return idfamass;
	}

	public void setIdfamass(Integer idfamass) {
		this.idfamass = idfamass;
	}

	@Transient
	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	@Transient
	public String getId() {
		return codart;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
}
