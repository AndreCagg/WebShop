package it.webshop.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import it.webshop.conf.AppConf;
import reactor.core.publisher.Mono;

@Component
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

        if (entry != null && entry.expiry > now) {
            return Mono.just(entry.principal);
        }

        return client.post()
                .uri(secConf.getIntrospectURI())
                .headers(h -> h.setBasicAuth(secConf.getResourceServerID(), secConf.getResourceServerSecret()))
                .body(BodyInserters.fromFormData("token", token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> {
                    if (resp == null || !Boolean.TRUE.equals(resp.get("active"))) {
                        throw new RuntimeException("Token non valido");
                    }
                    long exp = resp.containsKey("exp") ? ((Number) resp.get("exp")).longValue() : now + 60;

                    //gestione utente
                    List<Map<String, String>> roles=new ArrayList<>((List<Map<String, String>>)resp.get("roles"));
                    List<GrantedAuthority> auths=new ArrayList<>();
                    System.out.println(resp.get("roles").toString());
                    
                    for(Map<String, String> role : roles) {
                    	auths.add(new SimpleGrantedAuthority(role.get("authority")));
                    }
                 
                    DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
                            Map.of("username", resp.getOrDefault("sub", "user")),
                            auths
                    );

                    cache.put(token, new CacheEntry(principal, exp));
                    return principal;
                });
    }

    class CacheEntry {
        final OAuth2AuthenticatedPrincipal principal;
        final long expiry;
        CacheEntry(OAuth2AuthenticatedPrincipal p, long e) { principal = p; expiry = e; }
    }
}
