package it.userservice.dto;
import java.util.List;

public class UserRegisterRequest {
	private String user;
	private String password;
	private List<String> ruoli;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}
}
