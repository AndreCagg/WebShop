package it.proxy.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1.0")
@CrossOrigin(
	    origins = "http://localhost:3000",
	    allowCredentials = "true"
	)
public class ProxyController {
	private Map<String, String> codeVerifiers;
	
	@GetMapping("/login")
	public void login(HttpServletResponse resp, HttpSession session) throws IOException, NoSuchAlgorithmException{	
		SecureRandom random=new SecureRandom();
		byte[] b = new byte[43];
		random.nextBytes(b);
		
		String codeVerifier=Base64.getUrlEncoder().withoutPadding().encodeToString(b);
		
		
		byte[] hash=MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
		String codeChallenge=Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		
		if(codeVerifiers==null) {
			codeVerifiers=new HashMap<>();
		}
		String state=UUID.randomUUID().toString();
		codeVerifiers.put(state, codeVerifier);
		
		UriComponentsBuilder urlBuilding=UriComponentsBuilder.fromUriString("http://localhost:8090/oauth2/authorize");
		urlBuilding.queryParam("response_type", "code");
		urlBuilding.queryParam("client_id", "client");
		urlBuilding.queryParam("redirect_uri", "http://localhost:9090/api/v1.0/callback");
		urlBuilding.queryParam("code_challenge", codeChallenge);
		urlBuilding.queryParam("code_challenge_method", "S256");
		urlBuilding.queryParam("state", state);
		//String url="http://localhost:8090/oauth2/authorize?response_type=code&client_id=client&redirect_uri=http://localhost:9090/api/v1.0/callback&code_challenge="+codeChallenge+"&code_challenge_method=S256&state="+state;
		
		resp.sendRedirect(urlBuilding.build().toString());
	}
	
	@GetMapping("/callback")
	public void callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse resp, HttpSession session) 
			throws RestClientException, URISyntaxException, IOException {
		
		RestTemplate template=new RestTemplate();
		String url="http://localhost:8090/oauth2/token";
		
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("client", "secret");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "client");
		body.add("code", code);
		body.add("redirect_uri", "http://localhost:9090/api/v1.0/callback");
		body.add("code_verifier", this.codeVerifiers.get(state));

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

		
		ResponseEntity<Map<String, String>> tokensStr=template.exchange(
				url, 
				HttpMethod.POST, 
				entity, 
				new ParameterizedTypeReference<Map<String, String>>() {}
			);
		
		//creo sessione inviata come cookie
		session.setAttribute("access_token", tokensStr.getBody().get("access_token"));
		session.setAttribute("refresh_token", tokensStr.getBody().get("refresh_token"));
		session.setAttribute("logged", true);
		
		resp.sendRedirect("http://localhost:3000/home");
	}
	
	@GetMapping("/logged")
	public ResponseEntity<Map<String, Boolean>> logged(HttpSession session){
		Map<String, Boolean> map=new HashMap<>();
		HttpStatus status=HttpStatus.UNAUTHORIZED;
		
		map.put("logged", false);
		
		
		if(session.getAttribute("logged")!=null) {
			Boolean logged=Boolean.parseBoolean(session.getAttribute("logged").toString());
			
			if(logged) {
				status=HttpStatus.OK;
			}
			
			map.put("logged", logged);
		}
		
		
		return new ResponseEntity<Map<String, Boolean>>(map, status);
	}
	
	//tutte le altre richieste
	@RequestMapping("/proxy/**")
	//@DeleteMapping("/proxy/**")
	public ResponseEntity<String> doRequest(HttpServletRequest req, HttpServletResponse resp, HttpSession session, @RequestBody(required=false) String body) throws IOException{
		
		ResponseEntity<String> ret=null;
		if(session.getAttribute("access_token")!=null) {
			ret=this.tryRequest(req, session, body);
			
			
			if(ret.getStatusCode()==HttpStatus.INTERNAL_SERVER_ERROR) {
				String token=session.getAttribute("refresh_token").toString();
				if(token==null || this.tryRefresh(session, token)!=HttpStatus.OK) {
					session.setAttribute("logged", false);
					resp.sendRedirect("http://localhost:3000/");
					//return new ResponseEntity<String>("FAIL", HttpStatus.UNAUTHORIZED);
				}else {
					ret=this.tryRequest(req, session, body);
				}
			}
		}else {
			session.setAttribute("logged", false);
			resp.sendRedirect("http://localhost:3000/");
		}
		
		return ret;
		
	}
	
	private ResponseEntity<String> tryRequest(HttpServletRequest req, HttpSession session, String body) {
		RestTemplate template=new RestTemplate();
		HttpHeaders headers=new HttpHeaders();
		
		headers.setBearerAuth(session.getAttribute("access_token").toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> entity=new HttpEntity<>(body, headers);
		
		UriComponentsBuilder urlBuilding=UriComponentsBuilder.fromUriString("http://localhost:8080"+req.getRequestURI().replace("/proxy", ""));
		for(String p : req.getParameterMap().keySet()) {
			urlBuilding.queryParam(p, req.getParameter(p));
		}
		
		String url=urlBuilding.toUriString();
		System.out.println(url);
		
		
		ResponseEntity<String> ret=null;
		try{
			ret=template.exchange(url, HttpMethod.valueOf(req.getMethod()), entity, String.class);
		}catch(RestClientException e) {
			/*System.out.println(e.getMessage());
			System.out.println(e.getMostSpecificCause());
			if(e instanceof HttpClientErrorException.Unauthorized) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}*/
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return ret;
	}
	
	private HttpStatusCode tryRefresh(HttpSession session, String code) {
		RestTemplate template =new RestTemplate();
		MultiValueMap<String, String> map=new LinkedMultiValueMap<>();
		map.add("grant_type", "refresh_token");
		map.add("refresh_token", code);
		map.add("client_id", "client");
		
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("client", "secret");
		
		HttpEntity<MultiValueMap<String, String>> entity=new HttpEntity<>(map, headers);
		
		ResponseEntity<Map<String, String>> resp=null;
		
		try{
			resp=template.exchange("http://localhost:8090/oauth2/token", HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, String>>(){});
		}catch(RestClientException e) {
			/*if(e instanceof HttpClientErrorException.Unauthorized) {
				return HttpStatus.UNAUTHORIZED;
			}else {
				return HttpStatus.INTERNAL_SERVER_ERROR;
			}*/
			
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		Map<String, String> tokens=resp.getBody();
		
		if(tokens.get("access_token")!=null) {
			session.setAttribute("access_token", tokens.get("access_token"));
			session.setAttribute("refresh_token", tokens.get("refresh_token"));
		}
		
		return resp.getStatusCode();
	}
}
