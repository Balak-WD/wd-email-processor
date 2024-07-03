package com.wd.wdnap.email.processor;

import org.junit.Before;
import org.junit.Test;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.email.service.WDNAPEmailService;


public class WDNAPEmailServiceTest {
 
	
	@Before
	public void setUp() throws Exception {
		
		ApplicationConfigParams config = new ApplicationConfigParams();

		
		WDNAPEmailService service = new WDNAPEmailService(config);


	}
	
 //@Test
 public void testPerform(){

	 
  }

 
  
}
