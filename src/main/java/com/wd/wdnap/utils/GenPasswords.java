package com.wd.wdnap.utils;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenPasswords {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenPasswords.class);
	
	
	
	public static void main(String[] args) {
		//String plainTextPassword="123C!sco";
		String plainTextPassword="Top7$ecret";
		
		System.out.println(encrypt(plainTextPassword,"dev"));
		System.out.println(encrypt(plainTextPassword,"stage"));
		System.out.println(encrypt(plainTextPassword,"prod"));

	}

	public static String encrypt(String plainTextPassword, String key) {
		String ecryptedPassword = null;
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(key);
		try {
			LOGGER.debug("plainTextPassword:  " + plainTextPassword);
			ecryptedPassword = encryptor.encrypt(plainTextPassword);
			
		} catch (Exception e) {
			LOGGER.error("An error occurred while ecrypting the password");
			LOGGER.info("context", e);
		}
		return ecryptedPassword;
}
}