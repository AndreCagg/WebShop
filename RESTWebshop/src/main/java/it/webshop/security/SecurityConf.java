package it.webshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

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
                .pathMatchers("/api/v1.0/**").hasRole("USER")
                //.pathMatchers("/.well-known/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
            );

        return http.build();
    }

}


