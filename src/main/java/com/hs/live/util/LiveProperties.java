package com.hs.live.util;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author LIBO
 *
 */
@Component
@ConfigurationProperties(value = "live")
public class LiveProperties implements Serializable{
	private String casServerUrl;
	private String auth2Clientid;
	private String auth2ClientSecret;
	public String getCasServerUrl() {
		return casServerUrl;
	}
	public void setCasServerUrl(String casServerUrl) {
		this.casServerUrl = casServerUrl;
	}
	public String getAuth2Clientid() {
		return auth2Clientid;
	}
	public void setAuth2Clientid(String auth2Clientid) {
		this.auth2Clientid = auth2Clientid;
	}
	public String getAuth2ClientSecret() {
		return auth2ClientSecret;
	}
	public void setAuth2ClientSecret(String auth2ClientSecret) {
		this.auth2ClientSecret = auth2ClientSecret;
	}
	
}
