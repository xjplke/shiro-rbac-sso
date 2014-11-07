package me.itsoho.rbac.angularshiro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenWarpper {

	private Token token;
	
	@JsonCreator
	public TokenWarpper(@JsonProperty("token")Token token){
		this.token = token;
	}
	
	public TokenWarpper(){
		
	}
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
}
