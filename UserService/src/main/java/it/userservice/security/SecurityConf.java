package it.userservice.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;



@Configuration
@EnableWebSecurity
public class SecurityConf {
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager manager(AuthenticationConfiguration conf) {
		return conf.getAuthenticationManager();
	}
	
	// inserisco roles
	@Bean
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> customToken(){
		return context ->{ //utente appena loggato
			OAuth2TokenClaimsSet.Builder claims=context.getClaims();
			List<GrantedAuthority> roles=new ArrayList<>();
			
			for(GrantedAuthority g : context.getPrincipal().getAuthorities()) { // auth dell utente loggato
				if(g.getAuthority().startsWith("ROLE_")) {
					roles.add(g);
				}
			}
			
			claims.claim("roles", roles);
		};
	}
	
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
	        throws Exception {

	    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
	            new OAuth2AuthorizationServerConfigurer();

	    //authorizationServerConfigurer.oidc(Customizer.withDefaults());

	    http
	        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
	        .authorizeHttpRequests(auth -> 
	        {
	        	auth.requestMatchers("/api/v1.0/utenti/register").permitAll();
	        	auth.anyRequest().authenticated();	
	        })
	        .csrf(csrf -> csrf.disable())
	        .exceptionHandling(ex -> ex
	            .authenticationEntryPoint(
	                new LoginUrlAuthenticationEntryPoint("/login")
	            )
	        )
	        .cors(cors -> cors.configurationSource(req -> {
	        	CorsConfiguration conf=new CorsConfiguration();
	        	conf.setAllowedOriginPatterns(List.of("http://localhost:3000"));
	        	conf.setAllowedMethods(List.of("POST", "GET", "OPTIONS"));
	        	conf.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	        	return conf;
	        }))
	        .apply(authorizationServerConfigurer); // configurazione oauth2 ed inserimento del login

	    return http.build();
	}


	
	//serve??
	@Bean
	@Order(2)
	public SecurityFilterChain filter(HttpSecurity http) {
		http.formLogin(Customizer.withDefaults());
		//http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1.0/utenti/exchange"));
		
		http.authorizeHttpRequests(auth -> {
			//auth.requestMatchers("/api/v1.0/utenti/exchange").permitAll();
			auth.requestMatchers("/api/v1.0/utenti/register").permitAll();
			auth.anyRequest().authenticated();
		});
		
		return http.build();
	}
	
	@Bean
	public RegisteredClientRepository clientRepo() {
		RegisteredClient c=RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("client")
				.clientSecret(this.encoder().encode("secret"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:9090/api/v1.0/callback")
				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build()) //per token opachi oppure access token ttl per la scadenza
				.build();
		
		RegisteredClient resourceserver=RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("resourceserver")
				.clientSecret(this.encoder().encode("secret"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.build();
		
		return new InMemoryRegisteredClientRepository(c, resourceserver);
	} 
}
