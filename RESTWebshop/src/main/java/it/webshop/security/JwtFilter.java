package it.webshop.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")
public class JwtFilter implements WebFilter {

    @Autowired
    private JwtServ jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    	//prendo il cookie jwt
        HttpCookie authCookie = exchange.getRequest()
        		.getCookies()
        		.getFirst("Jwt");

        
        if (authCookie == null) {
            return chain.filter(exchange);
        }

        //verifico che sia valido ed eventualmente invio un 401
        String jwt=authCookie.getValue();
        Boolean isValid=jwtService.isValid(jwt);
        String username = jwtService.getUsername(jwt);
        if(username==null) {
    		return this.sendError(exchange);
        }
        
        List<GrantedAuthority> authorities=jwtService.getRoles(jwt);
        
        return this.elabora(exchange, chain, isValid, username, authorities);
    }
    
    private Mono<Void> elabora(ServerWebExchange exchange, WebFilterChain chain, Boolean valid, String username, List<GrantedAuthority> authorities) {
    	if(valid) {
                return chain.filter(exchange)
                    .contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(new CustomAuthentication(username, null, authorities))
                    );
    	}else {
			return this.sendError(exchange);
    	}
    }
    
    private Mono<Void> sendError(ServerWebExchange exchange) {
    	exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
	}
}

