package it.userservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.userservice.service.intf.IAuthenticationServ;

@Service
public class AuthenticationServ implements IAuthenticationServ{
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private JwtServ servJwt;
	
	public Map<String, String> authenticate(String user, String psw) {
		Authentication auth=manager.authenticate(
				new UsernamePasswordAuthenticationToken(user, psw)
				);
		Map<String, String> m=new HashMap<>();
		m.put("jwt", this.servJwt.generateJWT((UserDetails) auth.getPrincipal(), false));
		m.put("refresh", this.servJwt.generateJWT((UserDetails) auth.getPrincipal(), true));
		
		return m;
	}
}
