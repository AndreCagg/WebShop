package it.userservice.service.intf;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IJwtServ {
	public String generateJWT(UserDetails u, Boolean refresh);
	public String generateJWT(String username, List<GrantedAuthority> roles, Boolean refresh);
	//private String generateJWT_core(String username, List<GrantedAuthority> roles, Integer expSec);
	public String getUsername(String token);
	public List<GrantedAuthority> getRoles(String token);
	public  Claims decodeJwt(String token);
	public Boolean isValid(String jwt);
	public Boolean refresh(HttpServletRequest req, HttpServletResponse resp);
}
