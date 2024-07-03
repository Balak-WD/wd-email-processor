package com.wd.wdnap.email.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.utils.EWSMailUtil;
import com.wd.code.cnap.kafka.WDNAPKafkaProducer;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

//@Service
public class WDNAPEmailEWSService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPEmailEWSService.class);
	private static ExchangeService service;
	private ApplicationConfigParams config;
	private WDNAPKafkaProducer kp = null;
	private String username;
	private String password;
	private String url;
	private String domain;
	private EWSMailUtil mailUtil;
	
	
	public WDNAPEmailEWSService(ApplicationConfigParams config) {
			
			this.config = config;
			
			if(kp == null)
				kp = new WDNAPKafkaProducer(config);
			
			this.username = config.getUsername();
			this.password = config.getMailExchPWD();
			this.url = config.getMailExchURL();
			this.domain = config.getMailExchDomain();

			try{
				service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
				service.setUrl(new URI(this.url));
				ExchangeCredentials credentials = new WebCredentials(this.username, this.password, this.domain);
			    service.setCredentials(credentials);
			}catch(Exception e){
				LOGGER.error(" Error in intializing the Exchange Service :"+e.getMessage());
			}
			
			String folderName = System.getProperty("Folder");
			
			System.out.println("Folder is :"+folderName);
			
			if(mailUtil == null)
				mailUtil = new EWSMailUtil(service,folderName);
	}
	
	
	public void fetchEmails(){
		//mailUtil.readEmails();
		mailUtil.processEmails(kp,config);
	}
	
	

}
