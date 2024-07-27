package com.wd.wdnap.email.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;



/**
 * @author shrdeshp
 *
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan({ "com.wd.wdnap.email.tasks", "com.wd.wdnap.email.service","com.wd.code.cnap.kafka","com.wd.wdnap.utils" })
public class Application  {

	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Application.class);

	public static void main(String[] args) {
		LOGGER.info("Startng the Application");
		try {
			SpringApplication.run(Application.class, args);
		}catch (Exception e){
			LOGGER.info("BALA");
			e.printStackTrace();
		}

		
	}

}
