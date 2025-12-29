package it.userservice.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.userservice.conf.JwtConf;
import it.userservice.dto.JwtTokenRequest;
import it.userservice.dto.UserRegisterRequest;
import it.userservice.entity.Ruoli;
import it.userservice.entity.User;
import it.userservice.service.impl.AuthenticationServ;
import it.userservice.service.impl.CookieServ;
import it.userservice.service.impl.JwtServ;
import it.userservice.service.impl.UserServ;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1.0/utenti")
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
public class UserController {
	
	@Autowired
	private UserServ servUser;
	
	@Autowired
	private AuthenticationServ servAuth;
	
	@Autowired
	private JwtServ servJwt;
	
	@Autowired
	private CookieServ servCookie;
	
	@Autowired
	private JwtConf confJwt;
	
	
	@GetMapping("/isLogged")
	public ResponseEntity<String> isLogged(HttpServletRequest http){		
		HttpStatus status=HttpStatus.UNAUTHORIZED;
		String msg="FAIL";
		
		//verifica se il cookie Jwt è valido
		Cookie[] cookies=http.getCookies();
		if(cookies!=null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("Jwt")) {					
					if(this.servUser.isLogged(c.getValue())) {
						status=HttpStatus.OK;
						msg="OK";	
						break;
					}
				}
			}
		}
		
		return new ResponseEntity<String>(msg, status);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody JwtTokenRequest req, HttpServletResponse http){
		Map<String, String> credentials=this.servAuth.authenticate(req.getUser(), req.getPassword());
		String jwt=credentials.get("jwt");
		String refresh=credentials.get("refresh");
		
		http.addCookie(this.servCookie.createCookie("Jwt", jwt, "/",this.confJwt.getExp()));
		http.addCookie(this.servCookie.createCookie("Refresh_token", refresh, "/api/v1.0/utenti/refresh", this.confJwt.getRefreshExp()));
		
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<String> refresh(HttpServletRequest req, HttpServletResponse resp){
		return this.servJwt.refresh(req, resp) ? new ResponseEntity<String>("OK", HttpStatus.OK) : 
			new ResponseEntity<String>("FAIL", HttpStatus.UNAUTHORIZED);		
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody UserRegisterRequest req) {
		List<String> ruoli_str=req.getRuoli();
		List<Ruoli> ruoli=new ArrayList<>();
		
		for(String s : ruoli_str) {
			Ruoli r=new Ruoli();
			r.setRuolo(s);
			ruoli.add(r);
		}
		
		
		User u=this.servUser.crea(req.getUser(), req.getPassword(), ruoli);
		
		if(u==null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.created(null).build();
	}
}
