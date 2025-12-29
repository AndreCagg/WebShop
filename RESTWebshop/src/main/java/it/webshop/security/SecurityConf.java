package it.webshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebFluxSecurity
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")

public class SecurityConf {
	@Autowired
	private JwtFilter jwtFilter;
	
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityWebFilterChain chain(ServerHttpSecurity http) {
    	http
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((exchange, exAuth) -> {
                exchange.getResponse().getHeaders()
                        .add("Access-Control-Allow-Origin", "http://localhost:3000");
                exchange.getResponse().getHeaders()
                        .add("Access-Control-Allow-Credentials", "true");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            })
        );


        http.authorizeExchange(auth -> auth
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/inserisci/**", "/modifica/**", "/api/v1.0/**").hasRole("USER")
                .anyExchange().authenticated()
            )
            .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}


