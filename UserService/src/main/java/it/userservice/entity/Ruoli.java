package it.userservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ruoli")
public class Ruoli{
	private Integer id;
	private User utente;
	private String ruolo;
	
	@Id
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idutente")
	@JsonBackReference
	public User getUtente() {
		return utente;
	}
	
	public void setUtente(User u) {
		this.utente = u;
	}
	
	@Column(name="ruolo", length=20)
	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
}
