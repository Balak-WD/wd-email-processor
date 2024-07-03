package com.wd.wdnap.email.emailparser;

import org.springframework.stereotype.Service;

@Service
public class WDNAPEmaiParsingFactory {
	
	
	public static WDNAPEmailParser getCNAPEmailParser(String parserType){
		//System.out.println("PARSERTYPE :"+parserType);
		
		if(parserType == null)
			return  new AppDynamicEmailParser();
		
		if(parserType.equals(WDNAPConstants.APPDYNAMICS_ADWARNAGENTSTOPPED)){
			return  new AppDynamicAgentStopParser();
		}else if(parserType.equals(WDNAPConstants.APPDYNAMICS_CRICONTOOHIGH)){
			return new ADConPoolTooHighParser();
		}else if(parserType.equals(WDNAPConstants.APPDYNAMICS_CRIJVMGAROOHIGH)){
			return new ADJvmGarTooHighParser();
		}else if(parserType.equals(WDNAPConstants.APPDYNAMICS_CRIBUSTRANRTHIGH)){
			return new ADBusTranRateHighParser();
		}else if(parserType.equals(WDNAPConstants.APPDYNAMICS_CRICODEDEADLOCK)){
			return new ADCodeDeadLock();
		}
		else{
			return  new AppDynamicEmailParser();
		}
		
	}
	
	

}
