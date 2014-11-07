package me.itsoho.rbac.exceptions;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5606784453873810807L;
	
	public UserNotFoundException(String message){
		super(message);
	}
}
