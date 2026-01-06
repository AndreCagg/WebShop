package it.webshop.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenAuthenticationConverter;

import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter implements ReactiveOpaqueTokenAuthenticationConverter{
	/*@Override
	public Mono<AbstractAuthenticationToken> convert(OAuth2AuthenticatedPrincipal source) {
		List<Map<String, String>> rolesStr=source.getAttribute("roles");
		List<GrantedAuthority> roles=new ArrayList<>();
		
		for(Map<String, String> str : rolesStr) {
			roles.add(new SimpleGrantedAuthority(str.get("authority")));
			System.out.println(str.get("authority"));
		}
		
		OAuth2AuthenticatedPrincipal newPrincipal =
                new OAuth2IntrospectionAuthenticatedPrincipal(
                        source.getName(),
                        source.getAttributes(),
                        roles
                );

        return Mono.just(new BearerTokenAuthentication(
                newPrincipal,
                principal,
                authorities
        ));
	}*/

	@Override
	public Mono<Authentication> convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
		List<Map<String, String>> rolesStr=authenticatedPrincipal.getAttribute("roles");
		List<GrantedAuthority> roles=new ArrayList<>();
		
		for(Map<String, String> str : rolesStr){
			roles.add(new SimpleGrantedAuthority(str.get("authority")));
			//System.out.println(str.get("authority"));
		}
		
		OAuth2AccessToken accessToken =
                new OAuth2AccessToken(
                        TokenType.BEARER,
                        introspectedToken,
                        authenticatedPrincipal.getAttribute("iat"),
                        authenticatedPrincipal.getAttribute("exp")
                );

        // Authentication finale
        Authentication authentication =
                new BearerTokenAuthentication(
                		authenticatedPrincipal,
                        accessToken,
                        roles
                );

        return Mono.just(authentication);
	}

}
