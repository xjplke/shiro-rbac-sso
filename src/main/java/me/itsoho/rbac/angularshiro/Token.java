package me.itsoho.rbac.angularshiro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
	private String principal;
	private String credentials;
	
	@JsonCreator
	public Token(@JsonProperty("principal")String principal,
				@JsonProperty("credentials")String credentials){
		this.credentials = credentials;
		this.principal = principal; 
	}
	public Token(){
		
	}
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getCredentials() {
		return credentials;
	}
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	
}