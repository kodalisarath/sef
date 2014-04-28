package com.ericsson.raso.sef.auth.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureTest {

	public static void main(String[] args) {
		byte[] key = {0x7f, 0x01, 0x64, 0x23, 0x22, 0x51, 0x00, 0x78};
		System.out.println("Manual key: " + printByteArray(key));
		
		try {
			DESKeySpec ks = new DESKeySpec(key);
			byte[] secureKey = ks.getKey();
			System.out.println("Generated Key: " + printByteArray(secureKey));
			System.out.println("Is it good? " + ks.isWeak(secureKey, 0));
			
			
			
			// ENcryption part
			SecretKeySpec sks = new SecretKeySpec(key, "DES");
			Cipher encrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			encrypt.init(Cipher.ENCRYPT_MODE, sks);
			FileOutputStream fos = new FileOutputStream("Z:\\Common Share\\Projects\\raso-cac\\rasocac\\serialKey.sccm");
			ObjectOutputStream oosKey = new ObjectOutputStream(fos);
			oosKey.writeObject(key);
			oosKey.writeObject(encrypt.getIV());
			oosKey.close();
			fos.close();
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CipherOutputStream cos = new CipherOutputStream(baos, encrypt);
			ObjectOutputStream oos = new ObjectOutputStream(cos);
			
			String clearText = "Hello.... Here I come";
			
			oos.writeUTF(clearText);
			oos.close();
			cos.close();
			byte[] ciphered = baos.toByteArray();
			
			System.out.println("Clear Text: " + clearText);
			System.out.println("Cipher Text: " + printByteArray(ciphered));
			
			
			
			// Decryption
			FileInputStream fis = new FileInputStream("Z:\\Common Share\\Projects\\raso-cac\\rasocac\\serialKey.sccm");
			ObjectInputStream oisKey = new ObjectInputStream(fis);
			byte[] enryptionKey = (byte[]) oisKey.readObject();
			System.out.println("Encryption Key from File: " + printByteArray(enryptionKey));
			SecretKeySpec decryptKeySpec = new SecretKeySpec(enryptionKey, "DES");
			byte[] encryptionIv = (byte[]) oisKey.readObject();
			
			Cipher decrypt = Cipher.getInstance("DES/CFB8/NoPadding");
			decrypt.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(encryptionIv));
			ByteArrayInputStream bais = new ByteArrayInputStream(ciphered);
			
			CipherInputStream cis = new CipherInputStream(bais, decrypt);
			ObjectInputStream ois = new ObjectInputStream(cis);
			String decoded = ois.readUTF();
			System.out.println("Decoded Text: " + decoded);
			
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		

	}
	
	private static String printByteArray(byte[] payload) {
		String text = "{";
		for (byte octet: payload) {
			text += Byte.toString(octet) + ", ";
		}
		return text += "}";
	}

}
