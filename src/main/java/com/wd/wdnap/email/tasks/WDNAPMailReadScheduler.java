package com.wd.wdnap.email.tasks;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.email.service.WDNAPEmailEWSService;
import com.wd.wdnap.email.service.WDNAPEmailService;

@Component
public class WDNAPMailReadScheduler {

	@Autowired
	private ApplicationConfigParams config;
	private WDNAPEmailService cNAPEmailService;
	private WDNAPEmailEWSService ewsEmailService;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPMailReadScheduler.class);

	@PostConstruct
	public void init() {
		if (cNAPEmailService == null)
			cNAPEmailService = new WDNAPEmailService(config);
	}

	@Scheduled(fixedRate = 2000)
	public void readNotificationEMails() {

		long subStartTime = System.currentTimeMillis();

		LOGGER.info("cnap-email-service Application START!");

		if (cNAPEmailService == null) {
			cNAPEmailService = new WDNAPEmailService(config);
		}

		cNAPEmailService.initAndFetch();

		/*
		 * if(ewsEmailService == null) ewsEmailService = new
		 * CNAPEmailEWSService(config);
		 * 
		 * ewsEmailService.fetchEmails();
		 */

		long subEndTime = System.currentTimeMillis();
		LOGGER.info(
				"cnap-email-service Application - Total elapsed time " + (subEndTime - subStartTime) / 1000 + " Secs");
		LOGGER.info("cnap-email-service Application END!");

	}
}
