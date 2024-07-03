package com.wd.wdnap.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.util.SharedByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.email.emailparser.WDNAPConstants;
import com.wd.wdnap.email.emailparser.WDNAPEmaiParsingFactory;
import com.wd.wdnap.email.emailparser.WDNAPEmailParser;
import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;


public class MailUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailUtil.class);

	private static String ORIGIN_VALUE = WDNAPConstants.APPDYNAMICSAPPLICATIONNAME;
	
	public static String processOtherTypes(Part p){

		String contentString = "";

		try {
			if (p.isMimeType("multipart/*")) {
			
				Multipart mp = (Multipart) p.getContent();
				int count = mp.getCount();
				StringBuffer contentStringBuffer = new StringBuffer();

				for (int i = 0; i < count; i++){
					contentStringBuffer.append( processOtherTypes(mp.getBodyPart(i)) );
				}
				contentString = contentStringBuffer.toString();
			} 
			//check if the content is a nested message
			else if (p.isMimeType("message/rfc822")) {
				//				LOGGER.info("*** This is a Nested Message");
				//				LOGGER.info("---------------------------");
				contentString = processOtherTypes((Part) p.getContent());

			} 
			//check if the content is an inline image
			else if (p.isMimeType("image/jpeg")) {
				//				LOGGER.info("--------> image/jpeg");
				contentString = "image/jpeg - REMOVED";
			} 
			else if (p.getContentType().contains("image/")) {
				//				LOGGER.info("content type" + p.getContentType());
				contentString = p.getContentType()+"-- REMOVED";
			} 
			else{ 

				Object o = null;
				o = p.getContent();
				
				//LOGGER.info("content ..................");
				//LOGGER.info(String.valueOf(o));
				

				if (o instanceof String) {
					contentString = (String) o;
				} 
				else if (o instanceof InputStream) {
					InputStream is = (InputStream) o;
					is = (InputStream) o;
					int c = 0;
					try {
						c = is.read();

//						while ((c = is.read()) != -1)
//							LOGGER.info(Integer.toString(c));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					contentString = Integer.toString(c);
				} 
				else {
					contentString = o.toString();
				}
			}
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentString;
	}

	/*
	 * This method checks for content-type 
	 * based on which, it processes and
	 * fetches the content of the message
	 */
	public static WDNAPMailNotificationObject getCNAPObjectFromMessage(Message m) throws Exception {
		long startTime = System.currentTimeMillis();
		Address[] a;
		Date sentDate;
		String messageId;
		String referencesId;
		String inReplyToId;


		WDNAPMailNotificationObject cnapNotificationMailObject = new WDNAPMailNotificationObject();

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++){
				String fromAddr = a[j].toString();
				LOGGER.info("FROM: " + fromAddr);
				cnapNotificationMailObject.setFrom( fromAddr);
			}
		}
		//System.out.println("****** TIME IN MILLI-SECONDS after from :::::::: "+(System.currentTimeMillis()-startTime));
		// TO
		StringBuffer buff = new StringBuffer();
		try{		
		
		  if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
		
			for (int j = 0; j < a.length; j++){
	
					if(j > 0) {
						buff.append(",");
					}
					String toAddrs = a[j].toString();
					//LOGGER.info("Address j = : "  + j);
					buff.append(toAddrs);
					//LOGGER.info("TO: "  + toAddrs);
				}

			//cnapNotificationMailObject.setTo( buff.toString());
			}
		  
		} catch(Exception e){
				LOGGER.error("Exception in processing TO field :"+e.getMessage());
			}
			cnapNotificationMailObject.setTo( buff.toString());
			//System.out.println("****** TIME IN MILLI-SECONDS after TO :::::::: "+(System.currentTimeMillis()-startTime));

	try{
		// CC
		StringBuffer buff1 = new StringBuffer();
		
		if ((a = m.getRecipients(Message.RecipientType.CC)) != null) {
			
				for (int j = 0; j < a.length; j++){
	
					if(j > 0) {
						buff.append(",");
					}
					String toAddrs = a[j].toString();
					//LOGGER.info("Address j = : "  + j);
					buff.append(toAddrs);
				//	LOGGER.info("CC: "  + toAddrs);
				}
			
			cnapNotificationMailObject.setCC( buff1.toString());

		}
		
		}catch(Exception e){
			LOGGER.error("Exception in processing CC field :"+e.getMessage());
		}
	//System.out.println("****** TIME IN MILLI-SECONDS after CC :::::::: "+(System.currentTimeMillis()-startTime));

		//Sent Date
		if ((sentDate = m.getSentDate()) != null) {

			cnapNotificationMailObject.setSentDate(sentDate);
			LOGGER.info("sentDate: " + sentDate);
		}

	//	System.out.println("****** TIME IN MILLI-SECONDS after send Date :::::::: "+(System.currentTimeMillis()-startTime));

		//Message Id
		String[] idHeaders = m.getHeader("Message-ID");

		if (idHeaders != null && idHeaders.length > 0){
		//	System.out.println("****** TIME IN MILLI-SECONDS before 206 :::::::: "+(System.currentTimeMillis()-startTime));
			messageId = idHeaders[0];
			cnapNotificationMailObject.setMessageId(messageId);
		//	System.out.println("****** TIME IN MILLI-SECONDS before 208 :::::::: "+(System.currentTimeMillis()-startTime));
			LOGGER.info("messageId: " + messageId);
		}

		String[] referencesHeaders = m.getHeader("References");
				if (referencesHeaders != null && referencesHeaders.length > 0){

			referencesId = referencesHeaders[0];
			cnapNotificationMailObject.setMailerReferencesId(referencesId);
			LOGGER.info("referencesId: " + referencesId);
		}

	

		String[] inReplyToHeaders = m.getHeader("In-Reply-To");


		if (inReplyToHeaders != null && inReplyToHeaders.length > 0){

			inReplyToId = inReplyToHeaders[0];
			cnapNotificationMailObject.setMailerInReplyToId(inReplyToId);
			LOGGER.info("inReplyToId: " + inReplyToId);
		}

		// SUBJECT
		if (m.getSubject() != null)
			LOGGER.info("SUBJECT: " + m.getSubject());
		cnapNotificationMailObject.setSubject( m.getSubject());
		
		//add clone code here start 
		MimeMessage cloneEmail = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			m.writeTo(bos);
			bos.close();
			SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());

			cloneEmail = new MimeMessage(null, bis);
			bis.close();
		} catch (Exception e) {
			LOGGER.info("Could not clone email - Exception - " + e);
			
		}
		MimeMessage mimeMessage=cloneEmail;
		
		
		//end 
		//MimeMessage mimeMessage = cloneEmail(m);
		MimeMessageParser parser = new MimeMessageParser(mimeMessage);
		
		if (parser.hasHtmlContent()) {
			cnapNotificationMailObject.setContent(MailUtil.getHtmlTxtContent(parser.getHtmlContent(),"HTML"));
			cnapNotificationMailObject.setHtmlTxtContent(MailUtil.getHtmlTxtContent(parser.getHtmlContent(),"TEXT"));
			
		} else if (parser.hasPlainContent()) {
			cnapNotificationMailObject.setContent(parser.getPlainContent());
			
		}
		else if(m instanceof Part){
			String contentFromMultiPart = MailUtil.processOtherTypes(m);
			
		try{	
			
				if(contentFromMultiPart.contains("cisco1.saas.appdynamics.com")){
					/*CNAPEmailParser appDynamicsParser = CNAPEmaiParsingFactory.getCNAPEmailParser(CNAPConstants.APPDYNAMICSAPPLICATIONNAME);
					 String parsemessage = appDynamicsParser.parse(cnapNotificationMailObject, contentFromMultiPart);*/
			//if(cnapNotificationMailObject.getHtmlTxtContent().contains("cisco-appdynamics@saas.appdynamics.com")){
					//String parsemessage=  MailUtil.appDynamicParser(contentFromMultiPart,cnapNotificationMailObject);
					  cnapNotificationMailObject.setSource(WDNAPConstants.APPDYNAMICSAPPLICATIONNAME);
					  //LOGGER.debug("====================="+parsemessage+"======================");
					if(cnapNotificationMailObject.getSubject().contains("Warning events detected for Agent stopped reporting") ){
						ORIGIN_VALUE="adwarnAgentStopped";
						cnapNotificationMailObject.setOrigin("adwarnAgentStopped");
					}else if(cnapNotificationMailObject.getSubject().contains("Critical events detected for Connection pool usage is too high") || cnapNotificationMailObject.getSubject().contains("Critical events detected for Thread Pool Usage is high")){
						//ORIGIN_VALUE="";
						cnapNotificationMailObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRICONTOOHIGH);
					}else if(cnapNotificationMailObject.getSubject().contains("Critical events detected for JVM Garbage Collection Time is too high") || cnapNotificationMailObject.getSubject().contains("Critical events detected for JVM CPU utilization is too high")
							|| cnapNotificationMailObject.getSubject().contains("Warning events detected for JVM Heap and GC Time is too high") 
							){
						cnapNotificationMailObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRIJVMGAROOHIGH);
					}else if(cnapNotificationMailObject.getSubject().contains("Critical events detected for Business Transaction error rate is much higher than normal")
							|| 	cnapNotificationMailObject.getSubject().contains("Critical events detected for Error occured")){
						cnapNotificationMailObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRIBUSTRANRTHIGH);
					}else if(cnapNotificationMailObject.getSubject().contains("Critical events detected for Code Deadlock has occurred") ||
							cnapNotificationMailObject.getSubject().contains("Critical events detected for JVM Down") ||
							cnapNotificationMailObject.getSubject().contains("Events detected for JVM Restart")){
						cnapNotificationMailObject.setOrigin(WDNAPConstants.APPDYNAMICS_CRICODEDEADLOCK);
					}
					else if(cnapNotificationMailObject.getSubject().contains("Critical events detected for Thread Pool Usage is high") ||  cnapNotificationMailObject.getSubject().contains("JVM Heap utilization is too high"))
						cnapNotificationMailObject.setOrigin("adcriThpool");
					else if (cnapNotificationMailObject.getSubject().contains("Critical events detected for Error occured"))
						cnapNotificationMailObject.setOrigin("adcriError");
					else if (cnapNotificationMailObject.getSubject().contains("Critical events detected for Business Transaction response time"))
						cnapNotificationMailObject.setOrigin("adBusResHigh");
					else if (cnapNotificationMailObject.getSubject().contains("Warning events detected for Business Transaction response time"))
						cnapNotificationMailObject.setOrigin("adWarBusTran");
					else if (cnapNotificationMailObject.getSubject().contains("Warning events detected for JVM Garbage Collection Time"))
						cnapNotificationMailObject.setOrigin("adWarJVMGarbage");
					else if (cnapNotificationMailObject.getSubject().contains("Warning events detected for Alert!! OPUI Application!"))
						cnapNotificationMailObject.setOrigin("adWarOPUI");
					else if (cnapNotificationMailObject.getSubject().contains("Critical events detected for Agent stopped reporting"))
						cnapNotificationMailObject.setOrigin("adCrAgent");
					else if (cnapNotificationMailObject.getSubject().contains("Critical events detected for SocketTimeOutException"))
						cnapNotificationMailObject.setOrigin("adCrSocket");
					else if (cnapNotificationMailObject.getSubject().contains("Business Transaction response time is much higher than normal") ){
						cnapNotificationMailObject.setOrigin("adBusResHigh");
					}
					
					
					WDNAPEmailParser appDynamicsParser = WDNAPEmaiParsingFactory.getCNAPEmailParser(cnapNotificationMailObject.getOrigin());
					 String parsemessage = appDynamicsParser.parse(cnapNotificationMailObject, contentFromMultiPart);
					 
					 
				}else if(contentFromMultiPart.contains("grafana")){
					if(cnapNotificationMailObject.getSubject().contains("[OK] /var/log :  High Usage"))
						cnapNotificationMailObject.setOrigin("GfHUView");
					else if(cnapNotificationMailObject.getSubject().contains("Openshift Node Process"))
						cnapNotificationMailObject.setOrigin("GfOsnAlert");
					else if(cnapNotificationMailObject.getSubject().contains("Docker Service"))
						cnapNotificationMailObject.setOrigin("GfDcMetric");
					else if(cnapNotificationMailObject.getSubject().contains("[OK] Root FS Capacity Low") || cnapNotificationMailObject.getSubject().contains("Docker API  Check"))
						cnapNotificationMailObject.setOrigin("GfFSview");
					else if(cnapNotificationMailObject.getSubject().contains("[No Data] Root FS Capacity Low"))
						cnapNotificationMailObject.setOrigin("GfFSMetric");
					else if(cnapNotificationMailObject.getSubject().contains("[OK] Available Memory") || cnapNotificationMailObject.getSubject().contains("[OK] CPU Usage"))
						cnapNotificationMailObject.setOrigin("GrHMCUAlet");
					else if(cnapNotificationMailObject.getSubject().contains("[No Data] Docker VG missing"))
						cnapNotificationMailObject.setOrigin("GfDcVgMetric");
					else if(cnapNotificationMailObject.getSubject().contains("[OK] Docker VG missing"))
						cnapNotificationMailObject.setOrigin("GfHUView");
					else if(cnapNotificationMailObject.getSubject().contains("clip-prod-100"))
						cnapNotificationMailObject.setOrigin("GfHUView");
				}
				
				cnapNotificationMailObject.setContent(MailUtil.getHtmlTxtContent(contentFromMultiPart,"HTML"));
				/*if(cnapNotificationMailObject.getContent().contains("grafana"))
					cnapNotificationMailObject.setOrigin("grafana");*/
				cnapNotificationMailObject.setHtmlTxtContent(MailUtil.getHtmlTxtContent(contentFromMultiPart,"TEXT"));
				//LOGGER.info("&&&&isMultipart -" + contentFromMultiPart);
				
			}catch(Exception e){
				LOGGER.error("Exception in setting the origin :"+e.getMessage());
			}
		}
		else{
			cnapNotificationMailObject.setContent("Not Text or HTML content");
		}
		
		return cnapNotificationMailObject;
	}


	public static MimeMessage cloneEmail( Message mail){

		MimeMessage cloneEail = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mail.writeTo(bos);
			bos.close();
			SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());

			cloneEail = new MimeMessage(null, bis);
			bis.close();
		} catch (Exception e) {
			LOGGER.info("Could not clone email - Exception - " + e);
			return null;
		}

		return cloneEail;
	}
	
	/*public static String getHtmlTxtContent(String msgbody){
		String parsedString="";
		try{
			if(!StringUtil.isBlank(msgbody)){
				Document doc= Jsoup.parse(msgbody);
				parsedString= doc.body().text();
					
			}
		}catch(Exception e){
			LOGGER.error("Error in parsing Content :"+e.getMessage());
		}
	
		return parsedString;
	}
	*/
	
	public static String getHtmlTxtContent(String content,String parseType){		
		//--------------------------
		Document document=null;
		try {
			document = Jsoup.parse(content);
			Elements elemts = document.select("b:contains(From:)");
			 if(elemts.size()>0){
				 elemts.first().parent().children().remove();
			 }
			
			Element ele = document.getElementById("divRplyFwdMsg");
			if(ele!=null){
				ele.remove();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//document.select("td>div.WordSection1").remove();
		//border:none;border-top:solid #E1E1E1 1.0pt;padding:3.0pt 0in 0in 0in
		
		//
		if(document!=null && parseType.equalsIgnoreCase("HTML")){
			return document.body().html();
		}
		else if(document!=null && parseType.equalsIgnoreCase("TEXT")){
			return document.body().text();
		}
		else{
			return "";
		}
		
	}
	
	
	private static String appDynamicParser(String originalContent, WDNAPMailNotificationObject cnapMailNotificationObj) {
		String content = originalContent.replaceAll("&nbsp;", "");
		StringBuilder builder = new StringBuilder("{");
		try {
			Document doc = Jsoup.parse(content);
			int y = 0;
			Elements ele = doc.getElementsByTag("table");
			Elements trElements = doc.getElementsByTag("tr");
			for (int i = 0; i < trElements.size(); i++) {
				String appDynamicNotification = trElements.get(i).text().replaceAll(" ", "");
				if (appDynamicNotification.equals("AppDynamicsNotification ")
						|| appDynamicNotification.equals("AppDynamicsNotification")) {
					String applicationValue = trElements.get(i + 1).text();
					if (StringUtils.isNotEmpty(applicationValue)) {
						cnapMailNotificationObj.setApplication(applicationValue.trim());
					} else {
						cnapMailNotificationObj.setApplication("");
					}
					builder.append("applicationName=" + applicationValue).append(",\n");
					break;
				}
			}

			for (int i = ele.size() - 1; i > 0; i--) {
				Element element = ele.get(i);

				String value = element.text();
				if (value.contains("Event Type")) {
					// String applicationName = ele.get(i-2).html();
					String eventNotificationName = ele.get(i - 1).getElementsByTag("td").get(0).getElementsByTag("h4")
							.get(0).text();
					if (StringUtils.isNotEmpty(eventNotificationName)) {
						cnapMailNotificationObj.setEventNotificationName(eventNotificationName.trim());
					}

					builder.append("eventNotificationName=" + eventNotificationName).append(",\n");
					// System.out.println("eventNotificationName=" +
					// eventNotificationName);

					// event name and count .i.e healthRule 11
					Elements eventT = ele.get(i).getElementsByTag("tr").get(1).getElementsByTag("td");
					String count = eventT.get(0).text();
					cnapMailNotificationObj.setCount(count.trim());
					builder.append("count=" + count).append(",\n");
					// System.out.println("count=" + eventT.get(0).text());
					String eventTypeName = eventT.get(1).text();
					if (StringUtils.isNotEmpty(eventTypeName)) {
						cnapMailNotificationObj.setEventTypeName(eventTypeName.trim());
					}
					builder.append("eventTypeName=" + eventTypeName).append(",\n");
					// System.out.println("eventTypeName=" +
					// eventT.get(1).text());

					if (ele.get(i + 1) != null && ele.get(i + 1).getElementsByTag("tr").size() >= 3) {
						// eventTime:Thu Sep 21 13:30:33 PDT 2017 ===
						// tier:atacan-PROD ====node:edelapprdlae-kjgylq4iprd-1
						String[] val = ele.get(i + 1).getElementsByTag("tr").get(2).append("|").text().split("\\|");
						if (!val[0].isEmpty()) {
							cnapMailNotificationObj.setEventTime(val[0].trim());
							builder.append("eventTime=" + val[0]).append(",\n");
							// System.out.println("eventTime =" + val[0]);
						}
						if (val.length > 1 && !(val[1].isEmpty())) {
							cnapMailNotificationObj.setTier(val[1].trim());
							builder.append("tier=" + val[1]).append(",\n");
							// System.out.println("tier =" + val[1]);
						}
						if (val.length > 2 && !(val[2].isEmpty())) {
							cnapMailNotificationObj.setNode(val[2].trim());
							builder.append("node=" + val[2]).append(",\n");
							// System.out.println("node =" + val[2]);
						}
						// System.out.println((i+1)+"====element
						// text=-==="+ele.get(i+1).getElementsByTag("tr").get(2).append("|").text());
						// errorMessage
						builder.append("errorMessage=" + ele.get(i + 1).getElementsByTag("tr").get(3).text())
								.append(",\n");
						// System.out.println("errorMessage=" + ele.get(i +
						// 1).getElementsByTag("tr").get(3).text());

						// template formatting for jxminstance
						String errorMessage = ele.get(i + 1).getElementsByTag("tr").get(3).text();
						if (StringUtils.isNotEmpty(errorMessage)) {
							cnapMailNotificationObj.setErrorMessage(errorMessage.trim());
						}
						Elements notesElements = ele.get(i-1).select("td:contains(Notes:)");
						if(notesElements.size()>0){
							String notesValue = notesElements.get(0).text();
							if(StringUtils.isNotEmpty(notesValue) && notesValue.contains("Notes:")){
								notesValue = StringUtils.substringAfter(notesValue, "Notes:").trim();
								cnapMailNotificationObj.setNote(notesValue);
							}
						}
						
						String innermsg = MailUtil.parseInnerText(errorMessage, cnapMailNotificationObj);
						builder.append(innermsg);

					}
					String eventType = ele.get(i + 2).text();
					if (StringUtils.isNotEmpty(eventType)) {
						cnapMailNotificationObj.setEventType(eventType.trim());
					}
					builder.append("eventType=" + eventType).append(",\n");
					// System.out.println("eventType=" + ele.get(i + 2).text());
					String severity = ele.get(i + 2).getElementsByTag("img").attr("alt");
					if (StringUtils.isNotEmpty(severity)) {
						cnapMailNotificationObj.setSeverity(severity.trim());
						cnapMailNotificationObj.setErrorType(severity.trim());
					}
					builder.append("severity=" + severity).append("\n");
					// System.out.println("severity=" + ele.get(i +
					// 2).getElementsByTag("img").attr("alt"));
					break;
				}
				// System.out.println( i+"====element text=-==="+ element );
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error("== error in mail util parsing msg content" + e.toString());
		}
		return builder.append("}").toString();
	}

	private static String parseInnerText(String innerText, WDNAPMailNotificationObject cnapMailNotificationObj) {
		String msg = innerText;
		StringBuilder builder = new StringBuilder("");
		if (StringUtils.isNoneEmpty(msg)) {
			if (msg.contains("JMX Instance Name")) {
				String jmxInstancevalue = StringUtils.substringBetween(msg, "JMX Instance Name", "\".");
				cnapMailNotificationObj.setJmxInstance(jmxInstancevalue.replaceAll("\"", "").trim());
				builder.append("jmxInstance=" + jmxInstancevalue.replaceAll("\"", "")).append(",\n");

			}
			if (msg.contains("1)") || msg.contains("condition 1") || msg.contains("JVM|Memory:")) {

				String condition1value = StringUtils.substringBetween(msg, "value", "was");
				cnapMailNotificationObj.setCurrentValue(condition1value.trim());
				builder.append("conditionValue=" + condition1value).append(",\n");
				String balanceString = StringUtils.substringAfter(msg, condition1value);
				if (balanceString.contains("threshold")) {

					String threshold = StringUtils.substringBetween(msg, "threshold", "for");
					cnapMailNotificationObj.setThresholdValue(threshold.trim());
					String conditionTime = StringUtils.substringBetween(msg, "last ", "minutes");
					cnapMailNotificationObj.setConditionTime(conditionTime.trim());

					builder.append("threshold=" + threshold).append(",\n");
				}

			}
			// template for Business Transaction parsing
			if (msg.contains("For Business Transaction")) {
				String businessTransaction = StringUtils.substringBetween(msg, "For Business Transaction", ":");
				cnapMailNotificationObj.setBusinessTransaction(businessTransaction.trim());
				builder.append("businessTransaction = " + businessTransaction).append(",\n");

			}
		}
		return builder.toString();

	}
}
