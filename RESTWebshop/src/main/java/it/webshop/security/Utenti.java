package it.webshop.security;

import java.util.List;

public class Utenti {
	private String id;
	private String userid;
	private String password;
	private String attivo;
	
	private List<String> ruoli;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAttivo() {
		return attivo;
	}
	public void setAttivo(String attivo) {
		this.attivo = attivo;
	}
	public List<String> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}
}
