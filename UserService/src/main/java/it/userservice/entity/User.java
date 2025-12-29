package it.userservice.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.userservice.service.impl.UserServ;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="utenti")
public class User{
	private String id;
	private String userid;
	private String password;
	private String attivo;
	private List<Ruoli> ruoli;
	
	
	@Id
	@Column(name="id")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="userid")
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="attivo")
	public String getAttivo() {
		return attivo;
	}
	
	public void setAttivo(String attivo) {
		this.attivo = attivo;
	}

	@OneToMany(mappedBy="utente", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JsonManagedReference
	public List<Ruoli> getRuoli() {
		return ruoli;
	}

	public void setRuoli(List<Ruoli> ruoli) {
		this.ruoli = ruoli;
	}

	/*public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return (UserDetails) this.servUser.cerca(userid);
	}

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRuoli();
	}
	

	@Override
	@Transient
	public String getUsername() {
		return this.getUserid();
	}*/
	
}
