package it.webshop.security;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public class CacheEntry {
	private OAuth2AuthenticatedPrincipal principal;
    private long expiry;
    
    
    public CacheEntry(OAuth2AuthenticatedPrincipal p, long e) { 
    	principal = p;
    	expiry = e; 
	}


	public OAuth2AuthenticatedPrincipal getPrincipal() {
		return principal;
	}


	public void setPrincipal(OAuth2AuthenticatedPrincipal principal) {
		this.principal = principal;
	}


	public long getExpiry() {
		return expiry;
	}


	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}
    
    
}
