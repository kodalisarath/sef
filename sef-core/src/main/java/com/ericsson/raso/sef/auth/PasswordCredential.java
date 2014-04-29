package com.ericsson.raso.sef.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class PasswordCredential implements Credential {

	private String		simplePassword	= null;
	private Algorithm	algorithm		= Algorithm.UNDEF;

	public String getClearPassword() {
		return this.simplePassword;
	}

	public byte[] getCipheredPassword() {
		return null;
	}

	public void setPassword(String clearPassword) {
		this.simplePassword = clearPassword;
	}

	@Override
	public Type getCredentialType() {
		return Type.PASSWORD;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	private byte[] encryptPassword(String password) throws FrameworkException {
		switch (this.algorithm) {
			case BASIC:
				return encodeBase64(password);
			case DES:
				return encryptDes(password);
			case DIGEST_MD5:
				return createMd5Hash(password);
			case DIGEST_SHA1:
				return createSha1Hash(password);
			case UNDEF:
			default:
				throw new FrameworkException("Algorithm not set yet!!");
		}
	}

	private byte[] createMd5Hash(String password) throws FrameworkException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new FrameworkException("Failed to MD5 Digest (" + password + ")", e);
		}
	}

	private byte[] createSha1Hash(String password) throws FrameworkException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return md.digest(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new FrameworkException("Failed to SHA-1 Digest (" + password + ")", e);
		}
	}

	private byte[] encodeBase64(String password) {
		return Base64.getUrlEncoder().encode(password.getBytes());
	}

	private byte[] encryptDes(String password) throws FrameworkException {
		SecureSerializationHelper ssh = new SecureSerializationHelper();
		return ssh.encrypt(password);
	}

}
