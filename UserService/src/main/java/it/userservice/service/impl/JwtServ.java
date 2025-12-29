package it.userservice.service.impl;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.userservice.conf.JwtConf;
import it.userservice.service.intf.IJwtServ;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtServ implements IJwtServ {
	private final String SECRET="hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
	
	@Autowired
	private CookieServ servCookie;
	
	@Autowired
	private JwtConf confJwt;
	
	@Override
	public String generateJWT(UserDetails u, Boolean refresh) {
		//System.out.println(u.getAuthorities().toString());
		
		/*return Jwts.builder()
				.subject(u.getUsername().trim())
				.claim("roles", u.getAuthorities())
				.setExpiration(Date.from(Instant.now().plusSeconds(expSec)))
				.signWith(this.getKey()).compact();*/
		return this.generateJWT_core(u.getUsername(), u.getAuthorities().stream().map(r -> r).collect(Collectors.toList()), refresh);
	}
	
	@Override
	// utilizzato per non creare un oggetto authentication a partire ad jwt
	public String generateJWT(String username, List<GrantedAuthority> roles, Boolean refresh) {
		return this.generateJWT_core(username, roles, refresh);
	}
	
	private String generateJWT_core(String username, List<GrantedAuthority> roles, Boolean refresh) {
		Integer expSec = !refresh ? this.confJwt.getExp(): this.confJwt.getRefreshExp();
		
		return Jwts.builder()
		.subject(username.trim())
		.claim("roles", roles)
		.expiration(Date.from(Instant.now().plusSeconds(expSec)))
		.signWith(this.getKey()).compact();
	}
	
	@Override
	public String getUsername(String token) {
		try {
			return this.decodeJwt(token).getSubject();
		}catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public List<GrantedAuthority> getRoles(String token) {
		try {
			List<Map<String, String>> roles=this.decodeJwt(token).get("roles", ArrayList.class);
			
			return roles.stream().map(r->new SimpleGrantedAuthority(r.get("authority").trim())).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.println("ciaooo");
			return null;
		}
	}
	
	@Override
	public  Claims decodeJwt(String token){
		try {
			return Jwts.parser().verifyWith((SecretKey) this.getKey()).build().parseClaimsJws(token).getBody();
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Override
	public Boolean isValid(String jwt) {
		try {
			return this.decodeJwt(jwt).getExpiration().after(Date.from(Instant.now()));
		}catch(Exception e) {
			return false;
		}
		
	}
	
	@Override
	public Boolean refresh(HttpServletRequest req, HttpServletResponse resp) {
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null) {
			for(Cookie c : cookies) {
					if(c.getName().equals("Refresh_token") && this.isValid(c.getValue())) {
						String jwt=this.generateJWT(this.getUsername(c.getValue()), this.getRoles(c.getValue()), false);
						String refresh=this.generateJWT(this.getUsername(c.getValue()), this.getRoles(c.getValue()), true);
						
						resp.addCookie(this.servCookie.createCookie("Jwt", jwt, "/", this.confJwt.getExp()));
						resp.addCookie(this.servCookie.createCookie("Refresh_token", refresh, "/api/v1.0/utenti/refresh", this.confJwt.getRefreshExp()));
						
						return true;
					}
				}
			}
		
		return false;
	}
	
	private Key getKey() {
		byte[] b=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(b);
	}

}
