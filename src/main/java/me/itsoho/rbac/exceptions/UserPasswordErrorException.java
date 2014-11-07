package me.itsoho.rbac.exceptions;

public class UserPasswordErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2114758030422670995L;

	public UserPasswordErrorException(String message){
		super(message);
	}
}
