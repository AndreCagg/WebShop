package it.webshop.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import it.webshop.conf.AppConf;
import reactor.core.publisher.Mono;


@Service
public class TokenIntrospector implements ReactiveOpaqueTokenIntrospector{
	private AppConf secConf;
	private WebClient client;
	private Map<String, CacheEntry> cache;
	
	public TokenIntrospector(AppConf conf) {
		this.client=WebClient.create();
		this.cache=new ConcurrentHashMap<>();
		this.secConf=conf;
	}

	@Override
	public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
		long now = Instant.now().getEpochSecond();
        CacheEntry entry = cache.get(token);

        if (entry != null && entry.getExpiry() > now) {
            return Mono.just(entry.getPrincipal());
        }
        
        if (entry != null && entry.getExpiry() < now) {
        	throw new RuntimeException("Token non valido");
        }

        return client.post()
                .uri(secConf.getIntrospectURI())
                .headers(h -> h.setBasicAuth(secConf.getResourceServerID(), secConf.getResourceServerSecret()))
                .body(BodyInserters.fromFormData("token", token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> {
                    if (resp == null || !Boolean.parseBoolean(resp.get("active").toString()) || !resp.containsKey("exp")) {
                        throw new RuntimeException("Token non valido");
                    }
                    long exp = Long.parseLong(resp.get("exp").toString());

                    //gestione utente
                    List<Map<String, String>> roles=new ArrayList<>((List<Map<String, String>>)resp.get("roles"));
                    List<GrantedAuthority> auths=new ArrayList<>();
                    System.out.println(resp.get("roles").toString());
                    
                    for(Map<String, String> role : roles) {
                    	auths.add(new SimpleGrantedAuthority(role.get("authority").trim()));
                    }
                 
                    DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
                            Map.of("username", resp.get("sub")),
                            auths
                    );

                    cache.put(token, new CacheEntry(principal, exp));
                    return principal;
                });
    }
}
