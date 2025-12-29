package it.userservice.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app.jwt")
public class JwtConf {
	private Integer exp;
	private Integer refreshExp;
	
	
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getRefreshExp() {
		return refreshExp;
	}
	public void setRefreshExp(Integer refreshExp) {
		this.refreshExp = refreshExp;
	}
}
