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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@Service
public class WDNAPEmailEWSService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPEmailEWSService.class);
	private static ExchangeService service;

	@Autowired
	private WDNAPKafkaProducer kp = null;

	@Value("${application.username}")
	private String username;
	@Value("${application.password}")
	private String password;

	@Value("${application.mailExchURL}")
	private String url;

	@Value("${application.mailExchDomain}")
	private String domain;
	private EWSMailUtil mailUtil;
	
	
	public WDNAPEmailEWSService() {
			
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
		mailUtil.processEmails(kp);
	}
	
	

}
