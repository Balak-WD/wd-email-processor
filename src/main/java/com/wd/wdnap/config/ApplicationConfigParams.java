package com.wd.wdnap.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.wd.wdnap.utils.Util;
import com.google.common.base.Strings;


//@ConfigurationProperties(prefix = "application")
public class ApplicationConfigParams {

	// Defines the key for encryption algorithm
	private String decryptionKey;

	// host for email - mail.cisco.com
	private String host; 

	// like pop3
	private String mailStoreType; 

	// creds for the email
	private String username; 

	// creds for the email
	private String password; 
	
	private String idServiceURL;
	private String mailExchURL;
	private String mailExchPWD;
	private String mailExchDomain;
	
	private String subjectUUIDServiceURL;
	private String kafkaADBootstrapServers;
	private String monitorMailSub;
	public String getMonitorMailSub() {
		return monitorMailSub;
	}

	public void setMonitorMailSub(String monitorMailSub) {
		this.monitorMailSub = monitorMailSub;
	}

	public String getIdServiceURL() {
		return idServiceURL;
	}

	public void setIdServiceURL(String idServiceURL) {
		this.idServiceURL = idServiceURL;
	}

	public String getToServiceURL() {
		return toServiceURL;
	}

	public void setToServiceURL(String toServiceURL) {
		this.toServiceURL = toServiceURL;
	}

	public String getKafkaBootstrapServers() {
		return kafkaBootstrapServers;
	}

	public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
	}

	private String toServiceURL;
	
	private String kafkaBootstrapServers;

	public String getDecryptionKey() {
		return decryptionKey;
	}

	public void setDecryptionKey(String decryptionKey) {
		this.decryptionKey = decryptionKey;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getMailStoreType() {
		return mailStoreType;
	}

	public void setMailStoreType(String mailStoreType) {
		this.mailStoreType = mailStoreType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@PostConstruct
	public void decrypt() {
		if (!Strings.isNullOrEmpty(this.password)) {
			this.password = Util.decrypt(this.password, getDecryptionKey());
		}
	}

	public String getSubjectUUIDServiceURL() {
		return subjectUUIDServiceURL;
	}

	public void setSubjectUUIDServiceURL(String subjectUUIDServiceURL) {
		this.subjectUUIDServiceURL = subjectUUIDServiceURL;
	}

	public String getKafkaADBootstrapServers() {
		return kafkaADBootstrapServers;
	}

	public void setKafkaADBootstrapServers(String kafkaADBootstrapServers) {
		this.kafkaADBootstrapServers = kafkaADBootstrapServers;
	}

	public String getMailExchURL() {
		return mailExchURL;
	}

	public void setMailExchURL(String mailExchURL) {
		this.mailExchURL = mailExchURL;
	}

	public String getMailExchPWD() {
		return mailExchPWD;
	}

	public void setMailExchPWD(String mailExchPWD) {
		this.mailExchPWD = mailExchPWD;
	}

	public String getMailExchDomain() {
		return mailExchDomain;
	}

	public void setMailExchDomain(String mailExchDomain) {
		this.mailExchDomain = mailExchDomain;
	}
}
