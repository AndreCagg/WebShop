package it.webshop.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="app.security")
public class AppConf {
	private String introspectURI;
	private String resourceServerID;
	private String resourceServerSecret;

	public String getIntrospectURI() {
		return introspectURI;
	}

	public void setIntrospectURI(String introspectURI) {
		this.introspectURI = introspectURI;
	}

	public String getResourceServerID() {
		return resourceServerID;
	}

	public void setResourceServerID(String resourceServerID) {
		this.resourceServerID = resourceServerID;
	}

	public String getResourceServerSecret() {
		return resourceServerSecret;
	}

	public void setResourceServerSecret(String resourceServerSecret) {
		this.resourceServerSecret = resourceServerSecret;
	}
}
