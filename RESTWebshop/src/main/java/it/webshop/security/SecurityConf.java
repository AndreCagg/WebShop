package it.webshop.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import it.webshop.conf.AppConf;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")
public class SecurityConf {
	
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityWebFilterChain chain(ServerHttpSecurity http, TokenIntrospector intro) {
    	/*http.oauth2ResourceServer(s -> {
    		s.opaqueToken(t -> {
    			t.introspectionUri(this.secConf.getIntrospectURI())
    			.introspectionClientCredentials(this.secConf.getResourceServerID(), this.secConf.getResourceServerSecret())
    			.authenticationConverter(new JwtAuthenticationConverter());
    		});
    	});*/
    	
    	
    	http.oauth2ResourceServer(oauth -> oauth.opaqueToken(t -> t.introspector(intro)));


        http.authorizeExchange(auth -> auth
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                //.pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/api/v1.0/**").hasRole("USER")
                .pathMatchers("/.well-known/**").permitAll()
                .anyExchange().authenticated()
            );

        return http.build();
    }

}


