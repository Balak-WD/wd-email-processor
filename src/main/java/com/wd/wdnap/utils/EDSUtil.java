package com.wd.wdnap.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.email.emailparser.WDNAPConstants;
import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;

public class EDSUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(EDSUtil.class);
	private static String ORIGIN_VALUE = WDNAPConstants.APPDYNAMICSAPPLICATIONNAME;

	public static void setOrigin(WDNAPMailNotificationObject notifObject) {

		try {
			if (notifObject.getContent().contains("cisco1.saas.appdynamics.com")) {
				
				notifObject.setSource(WDNAPConstants.APPDYNAMICSAPPLICATIONNAME);
				if (notifObject.getSubject().contains("Warning events detected for Agent stopped reporting")) {
					ORIGIN_VALUE = "adwarnAgentStopped";
					notifObject.setOrigin(ORIGIN_VALUE);
				} else if (notifObject.getSubject()
						.contains("Critical events detected for Connection pool usage is too high")
						|| notifObject.getSubject()
								.contains("Critical events detected for Thread Pool Usage is high")) {
					// ORIGIN_VALUE="";
					notifObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRICONTOOHIGH);
				} else if (notifObject.getSubject()
						.contains("Critical events detected for JVM Garbage Collection Time is too high")
						|| notifObject.getSubject()
								.contains("Critical events detected for JVM CPU utilization is too high")
						|| notifObject.getSubject()
								.contains("Warning events detected for JVM Heap and GC Time is too high")) {
					notifObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRIJVMGAROOHIGH);
				} else if (notifObject.getSubject().contains(
						"Critical events detected for Business Transaction error rate is much higher than normal")
						|| notifObject.getSubject().contains("Critical events detected for Error occured")) {
					notifObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRIBUSTRANRTHIGH);
				} else if (notifObject.getSubject().contains("Critical events detected for Code Deadlock has occurred")
						|| notifObject.getSubject().contains("Critical events detected for JVM Down")
						|| notifObject.getSubject().contains("Events detected for JVM Restart")) {
					notifObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRICODEDEADLOCK);
				} else if (notifObject.getSubject().contains("Critical events detected for Thread Pool Usage is high")
						|| notifObject.getSubject().contains("JVM Heap utilization is too high"))
					notifObject.setOrigin("adcriThpool");
				else if (notifObject.getSubject().contains("Critical events detected for Error occured"))
					notifObject.setOrigin("adcriError");
				else if (notifObject.getSubject()
						.contains("Critical events detected for Business Transaction response time"))
					notifObject.setOrigin("adBusResHigh");
				else if (notifObject.getSubject()
						.contains("Warning events detected for Business Transaction response time"))
					notifObject.setOrigin("adWarBusTran");
				else if (notifObject.getSubject().contains("Warning events detected for JVM Garbage Collection Time"))
					notifObject.setOrigin("adWarJVMGarbage");
				else if (notifObject.getSubject().contains("Warning events detected for Alert!! OPUI Application!"))
					notifObject.setOrigin("adWarOPUI");
				else if (notifObject.getSubject().contains("Critical events detected for Agent stopped reporting"))
					notifObject.setOrigin("adCrAgent");
				else if (notifObject.getSubject().contains("Critical events detected for SocketTimeOutException"))
					notifObject.setOrigin("adCrSocket");
				else if (notifObject.getSubject()
						.contains("Business Transaction response time is much higher than normal")) {
					notifObject.setOrigin("adBusResHigh");
				}

			//	CNAPEmailParser appDynamicsParser = CNAPEmaiParsingFactory.getCNAPEmailParser(notifObject.getOrigin());
			//	String parsemessage = appDynamicsParser.parse(notifObject, notifObject.getContent());

			} else if (notifObject.getContent().contains("grafana")) {
				if (notifObject.getSubject().contains("[OK] /var/log :  High Usage"))
					notifObject.setOrigin("GfHUView");
				else if (notifObject.getSubject().contains("Openshift Node Process"))
					notifObject.setOrigin("GfOsnAlert");
				else if (notifObject.getSubject().contains("Docker Service"))
					notifObject.setOrigin("GfDcMetric");
				else if (notifObject.getSubject().contains("[OK] Root FS Capacity Low")
						|| notifObject.getSubject().contains("Docker API  Check"))
					notifObject.setOrigin("GfFSview");
				else if (notifObject.getSubject().contains("[No Data] Root FS Capacity Low"))
					notifObject.setOrigin("GfFSMetric");
				else if (notifObject.getSubject().contains("[OK] Available Memory")
						|| notifObject.getSubject().contains("[OK] CPU Usage"))
					notifObject.setOrigin("GrHMCUAlet");
				else if (notifObject.getSubject().contains("[No Data] Docker VG missing"))
					notifObject.setOrigin("GfDcVgMetric");
				else if (notifObject.getSubject().contains("[OK] Docker VG missing"))
					notifObject.setOrigin("GfHUView");
				else if (notifObject.getSubject().contains("clip-prod-100"))
					notifObject.setOrigin("GfHUView");
			}

			notifObject.setContent(MailUtil.getHtmlTxtContent(notifObject.getContent(), "HTML"));
			/*
			 * if(notifObject.getContent().contains("grafana"))
			 * notifObject.setOrigin("grafana");
			 */
			notifObject.setHtmlTxtContent(MailUtil.getHtmlTxtContent(notifObject.getContent(), "TEXT"));
			// LOGGER.info("&&&&isMultipart -" + contentFromMultiPart);

		} catch (Exception e) {
			LOGGER.error("Exception in setting the origin :" + e.getMessage());
		}

	}

}
