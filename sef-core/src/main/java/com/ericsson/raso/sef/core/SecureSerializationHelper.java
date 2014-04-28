package com.ericsson.raso.sef.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureSerializationHelper {

	private String keyFileLocation = "Z:\\Common Share\\Projects\\raso-cac\\rasocac\\serialKey.sccm";

	/**
	 * hardcoded key defined to ensure consistency to recover when key file is
	 * lost and decryption has to be ensured without risking randomness
	 */
	byte[] key = { 0x7f, 0x01, 0x64, 0x23, 0x22, 0x51, 0x00, 0x78 };

	private Cipher encryptionCipher = null;
	private Cipher decryptionCipher = null;

	public SecureSerializationHelper() {
		// TODO: initialize cipher key location from config, once the config
		// service is ready
		if (this.fileExists(keyFileLocation)) {
			this.loadFromKeyFile();
		} else {
			this.createKeyFile();
		}

	}

	public boolean fileExists(String fileLocation) {
		try {
			FileInputStream fis = new FileInputStream(fileLocation);
			return true;
		} catch (FileNotFoundException e) {
			// TODO logger
			return false;
		}

	}
	
	public byte[] encrypt(Object payload) throws FrameworkException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CipherOutputStream cos = new CipherOutputStream(baos,
					this.encryptionCipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);

			oos.writeObject(payload);

			oos.close();
			cos.close();
			
			byte[] encrypted = baos.toByteArray();
			baos.close();
			
			return encrypted;

		} catch (FileNotFoundException e) {
			throw new FrameworkException("Cannot encrypt payload...", e);
		} catch (IOException e) {
			throw new FrameworkException("Cannot encrypt payload...", e);
		}
	}
	
	public Object decrypt(byte[] payload) throws FrameworkException {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(payload);
			CipherInputStream cis = new CipherInputStream(bais,
					this.decryptionCipher);
			ObjectInputStream ois = new ObjectInputStream(cis);

			Object contents = ois.readObject();

			ois.close();
			cis.close();
			bais.close();

			return contents;

		} catch (FileNotFoundException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		} catch (IOException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		} catch (ClassNotFoundException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		}
	}

	public boolean persistToFile(String fileLocation, Serializable contents)
			throws FrameworkException {
		try {
			FileOutputStream fos = new FileOutputStream(fileLocation);
			CipherOutputStream cos = new CipherOutputStream(fos,
					this.encryptionCipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);

			oos.writeObject(contents);

			oos.close();
			cos.close();
			fos.close();

			return true;

		} catch (FileNotFoundException e) {
			throw new FrameworkException("Cannot persist contents...", e);
		} catch (IOException e) {
			throw new FrameworkException("Cannot persist contents...", e);
		}
	}

	public Serializable fetchFromFile(String fileLocation)
			throws FrameworkException {
		try {
			FileInputStream fis = new FileInputStream(fileLocation);
			CipherInputStream cis = new CipherInputStream(fis,
					this.decryptionCipher);
			ObjectInputStream ois = new ObjectInputStream(cis);

			Serializable contents = (Serializable) ois.readObject();

			ois.close();
			cis.close();
			fis.close();

			return contents;

		} catch (FileNotFoundException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		} catch (IOException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		} catch (ClassNotFoundException e) {
			throw new FrameworkException("Cannot fetch contents...", e);
		}
	}

	private void loadFromKeyFile() {
		System.out.println("Loading Key File...");

		try {
			FileInputStream fis = new FileInputStream(this.keyFileLocation);
			ObjectInputStream ois = new ObjectInputStream(fis);

			byte[] encryptionKey = (byte[]) ois.readObject();
			byte[] initialVector = (byte[]) ois.readObject();

			this.key = encryptionKey;

			Cipher decrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DES"),
					new IvParameterSpec(initialVector));
			this.decryptionCipher = decrypt;

			Cipher encrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			decrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DES"),
					new IvParameterSpec(initialVector));
			this.encryptionCipher = encrypt;

		} catch (FileNotFoundException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (IOException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("loadKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		}
		System.out.println("loadKey Failed!!");
	}

	private void createKeyFile() {
		System.out.println("Creating Key File...");
		try {
			SecretKeySpec sks = new SecretKeySpec(key, "DES");
			Cipher encrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			encrypt.init(Cipher.ENCRYPT_MODE, sks);
			this.encryptionCipher = encrypt;

			Cipher decrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DES"),
					new IvParameterSpec(encrypt.getIV()));
			this.decryptionCipher = decrypt;

			FileOutputStream fos = new FileOutputStream(this.keyFileLocation);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(key);
			oos.writeObject(encrypt.getIV());

			oos.close();
			fos.close();
			oos = null;
			fos = null;

		} catch (NoSuchAlgorithmException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());

		} catch (FileNotFoundException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());

		} catch (IOException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());

		} catch (NoSuchPaddingException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());

		} catch (InvalidKeyException e) {
			// TODO Logger - unable to get the algorithm... quite rare
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			System.out.println("createKey Failed!! " + e.getClass().getCanonicalName() + " = " + e.getMessage());
		}
		System.out.println("keyFile created!!");
	}

}
