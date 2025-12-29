package it.webshop.security;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServ {
private final String SECRET="hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
	
	
	public  Claims decodeJwt(String token){
		try {
			return Jwts.parser().verifyWith((SecretKey) this.getKey()).build().parseClaimsJws(token).getBody();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public String getUsername(String token) {
		try {
			return this.decodeJwt(token).getSubject();
		}catch(Exception e) {
			return null;
		}
	}
	
	public List<GrantedAuthority> getRoles(String token) {
		try {
			List<Map<String, String>> roles=this.decodeJwt(token).get("roles", ArrayList.class);
			
			return roles.stream().map(r->new SimpleGrantedAuthority(r.get("authority").trim())).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.println("ciaooo");
			return null;
		}
	}
	
	public Boolean isValid(String jwt) {
		try {
			Date exp=this.decodeJwt(jwt).getExpiration();
			Date now=Date.from(Instant.now());
			if(exp.after(now)) {
				return true;
			}else{
				return false;
			}
		}catch(Exception e) {
			return false;
		}
		
	}
	
	private Key getKey() {
		byte[] b=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(b);
	}

}
