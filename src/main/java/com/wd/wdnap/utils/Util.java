package com.wd.wdnap.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author shrdeshp
 *
 */
@Service
public class Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	private static final String SECURITY_FILE_PATH = System.getProperty("secfilepath");

	@Value("application.kafkaBootstrapServers")
	public  String bootstrapServers;

	private Util() {
		super();
	}

	/**
	 * Returns 
	 * @param value
	 * @return
	 */
	public static Boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static String decrypt(String encryptedPassword, String key) {
		String decryptedPassword = null;
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(key);
		try {
			LOGGER.debug("Encrypted password:  " + encryptedPassword);
			decryptedPassword = encryptor.decrypt(encryptedPassword);

		} catch (Exception e) {
			LOGGER.error("An error occurred while decrypting the password");
			LOGGER.info("context", e);
		}
		return decryptedPassword;
	}



	public  Properties loadEmailProperties(String pop3Host){
		// create properties field
		Properties properties = new Properties();
		System.setProperty("javax.net.ssl.trustStore",SECURITY_FILE_PATH+"jssecacerts");
	    System.setProperty("javax.net.ssl.trustAnchors",SECURITY_FILE_PATH+"jssecacerts");
	    
		properties.setProperty("mail.pop3.ssl.trust", pop3Host);
		properties.put("mail.pop3.port", "995");
		properties.put("mail.store.protocol", "pop3");
		properties.setProperty("mail.pop3.ssl.trust", "*"); // Trust all Servers
		properties.put("mail.pop3.host", pop3Host);
		properties.put("mail.pop3.starttls.enable", "true");
		//added to fix handshake issue
		properties.setProperty("mail.pop3.starttls.required", "true");
		return properties;
	}


	public  Properties loadKafkaProperties(){

		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrapServers);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("ProducerConfig.ACKS_CONFIG", "none");

		return properties;
	}

	/**
	 * TODO: Make it a service in future
	 * @param senderAddress
	 * @return
	 */
	public static String getKafkaUniqueRecordKey(String senderAddress, String messageIDURL){



		String kafkaUniqueRecordKey = null;//new String (senderAddress + System.currentTimeMillis());

		String respJson = GenerateIDUtil.generateMessageID(senderAddress, messageIDURL);

		JSONObject object = new JSONObject(respJson);
		kafkaUniqueRecordKey = (String) object.get("id");
		LOGGER.info("getKafkaUniqueRecordKey" + kafkaUniqueRecordKey);
		return kafkaUniqueRecordKey;
	}


	public static String getSubjectUUID(String subject, String subUUIDURL){

		String mailSubjectUUID = null;

		String respJson = GenerateIDUtil.generateSubjectUUID(subject,subUUIDURL);
		JSONObject object = new JSONObject(respJson);
		mailSubjectUUID = (String) object.get("id");
		LOGGER.info("getSubjectUUID" + mailSubjectUUID);
		return mailSubjectUUID;
	}

	public static Properties loadIMAPEmailProperties(String imapHost){
		// create properties field
		System.setProperty("javax.net.ssl.trustStore",SECURITY_FILE_PATH+"jssecacerts");
	    System.setProperty("javax.net.ssl.trustAnchors",SECURITY_FILE_PATH+"jssecacerts");
		Properties properties = System.getProperties();
		properties.put("mail.store.protocol", "imap");
		properties.put("mail.imap.host", imapHost);
		properties.put("mail.imap.port", "995");	
		properties.put("mail.imap.ssl.trust", "*");      // Trust all Servers
		properties.put("mail.imap.starttls.enable", "true");
		properties.setProperty("mail.imap.ssl.trust", imapHost);
		//added to fix handshake issue
		properties.setProperty("mail.imap.starttls.required", "true");		
		return properties;
	}


	public static String readFile(String path, Charset encoding)  throws IOException {

		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}



}
