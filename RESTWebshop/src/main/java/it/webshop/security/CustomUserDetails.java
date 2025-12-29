package it.webshop.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CustomUserDetails implements ReactiveUserDetailsService {

    private final WebClient webClient;

    public CustomUserDetails(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090").build();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return webClient.get()
            .uri("/api/v1.0/utenti/{id}", username)
            .retrieve()
            .bodyToMono(Utenti.class)
            .switchIfEmpty(Mono.error(
                new UsernameNotFoundException("Utente non trovato")
            ))
            .map(u -> org.springframework.security.core.userdetails.User
                .withUsername(u.getUserid())
                .password(u.getPassword())
                .roles(u.getRuoli().toArray(String[]::new))
                .build()
            );
    }
}
