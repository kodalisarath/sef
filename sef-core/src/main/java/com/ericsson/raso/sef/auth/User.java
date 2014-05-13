package com.ericsson.raso.sef.auth;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.core.Meta;



public class User extends Identity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8085548188898149578L;
	
	
	private List<Meta> userMeta;
	private Credential userCredentialType;
	private String password;

	public User(String userName, Credential userCredentialType) {
		super(userName);
	}
	
	
	public List<Meta> getUserMeta() {
		if(userMeta == null) {
			userMeta = new ArrayList<Meta>();
		}
		return userMeta;
	}




	public void setUserMeta(List<Meta> userMeta) {
		this.userMeta = userMeta;
	}




	public Credential getUserCredentialType() {
		return userCredentialType;
	}

	public void setUserCredentialType(Credential userCredentialType) {
		this.userCredentialType = userCredentialType;
	}
	
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((userCredentialType == null) ? 0 : userCredentialType
						.hashCode());
		result = prime * result
				+ ((userMeta == null) ? 0 : userMeta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userCredentialType == null) {
			if (other.userCredentialType != null)
				return false;
		} else if (!userCredentialType.equals(other.userCredentialType))
			return false;
		if (userMeta == null) {
			if (other.userMeta != null)
				return false;
		} else if (!userMeta.equals(other.userMeta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userMeta=" + userMeta + ", userCredentialType="
				+ userCredentialType + "]";
	}

}
