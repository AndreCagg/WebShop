package it.userservice.security;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.bind.annotation.CrossOrigin;

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
	
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
	        throws Exception {

	    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
	            new OAuth2AuthorizationServerConfigurer();

	    authorizationServerConfigurer.oidc(Customizer.withDefaults());

	    http
	        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
	        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
	        .csrf(csrf -> csrf.disable())
	        .exceptionHandling(ex -> ex
	            .authenticationEntryPoint(
	                new LoginUrlAuthenticationEntryPoint("/login")
	            )
	        )
	        .apply(authorizationServerConfigurer); // configurazione oauth2 ed inserimento del login

	    return http.build();
	}


	
	//serve??
	@Bean
	@Order(2)
	public SecurityFilterChain filter(HttpSecurity http) {
		http.formLogin(Customizer.withDefaults());
		
		http.authorizeHttpRequests(auth -> {
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
				.redirectUri("http://localhost:3000/")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build()) //per token opachi oppure access token ttl per la scadenza
				.build();
		
		return new InMemoryRegisteredClientRepository(c);
	} 
	
	
	// gestione chiavi per la firma dei token
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
	    RSAKey rsaKey;
		try {
			rsaKey = new RSAKeyGenerator(2048)
			    .keyID(UUID.randomUUID().toString())
			    .generate();
			
			JWKSet jwkSet = new JWKSet(rsaKey);
			return new ImmutableJWKSet<>(jwkSet);
		} catch (JOSEException e) {
			System.out.println("Errore generazione chiavi");
		}
		
		return null;

	}
}
