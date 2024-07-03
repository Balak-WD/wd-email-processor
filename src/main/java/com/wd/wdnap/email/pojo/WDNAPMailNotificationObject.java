package com.wd.wdnap.email.pojo;

import java.util.Date;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wd.wdnap.utils.Util;
import com.wd.wdnap.config.ApplicationConfigParams;

public class WDNAPMailNotificationObject implements Serializable{

	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPMailNotificationObject.class);

	private String from;
	private String to;
	private String cc;
	private Date sentDate;
	private String subject;
	private String content;
	private String messageId;
	private String mailerReferencesId;
	private String mailerInReplyToId;
	private String htmlTxtContent;
	private String origin;
	
	private String application;
	private String errorMessage;
	private String errorType;
	private String eventNotificationName;
	private String eventTime;
	private String eventType;
	private String eventTypeName;
	private String count;
	private String node;
	private String severity;
	private String source;
	private String currentValue;
	private String thresholdValue;
	private String businessTransaction;
	private String jmxInstance;
	private String tier;
	private String conditionTime;
	private String note;
	private String violStatus;
	private String callsPerMin;
	private String availVal;
	private String errorPerMinDeviation;
	private String errorPerMin;
	private String configSubjectUUIDServiceURL;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getConditionTime() {
		return conditionTime;
	}
	public void setConditionTime(String conditionTime) {
		this.conditionTime = conditionTime;
	}
	public String getTier() {
		return tier!=null?tier.trim().replaceAll("\\s", ""):"null";
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getApplication() {
		return application!=null?application.trim().replaceAll("\\s", ""):"null";
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getEventNotificationName() {
		return eventNotificationName;
	}
	public void setEventNotificationName(String eventNotificationName) {
		this.eventNotificationName = eventNotificationName;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	public String getThresholdValue() {
		return thresholdValue;
	}
	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	public String getBusinessTransaction() {
		return businessTransaction;
	}
	public void setBusinessTransaction(String businessTransaction) {
		this.businessTransaction = businessTransaction;
	}
	public String getJmxInstance() {
		return jmxInstance;
	}
	public void setJmxInstance(String jmxInstance) {
		this.jmxInstance = jmxInstance;
	}

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getHtmlTxtContent() {
		return htmlTxtContent;
	}
	public void setHtmlTxtContent(String htmlTxtContent) {
		this.htmlTxtContent = htmlTxtContent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMailerReferencesId() {
		return mailerReferencesId;
	}

	public void setMailerReferencesId(String mailerReferencesId) {
		this.mailerReferencesId = mailerReferencesId;
	}

	public String getMailerInReplyToId() {
		return mailerInReplyToId;
	}

	public void setMailerInReplyToId(String mailerInReplyToId) {
		this.mailerInReplyToId = mailerInReplyToId;
	}

	public void setCC(String cc) {
		this.cc = cc;
	}

	public String getCC() {
		return cc;
	}
	public String getViolStatus() {
		return violStatus;
	}
	public void setViolStatus(String violStatus) {
		this.violStatus = violStatus;
	}
	public String getCallsPerMin() {
		return callsPerMin;
	}
	public void setCallsPerMin(String callsPerMin) {
		this.callsPerMin = callsPerMin;
	}
	public String getAvailVal() {
		return availVal;
	}
	public void setAvailVal(String availVal) {
		this.availVal = availVal;
	}
	public String getErrorPerMinDeviation() {
		return errorPerMinDeviation;
	}
	public void setErrorPerMinDeviation(String errorPerMinDeviation) {
		this.errorPerMinDeviation = errorPerMinDeviation;
	}
	public String getErrorPerMin() {
		return errorPerMin;
	}
	public void setErrorPerMin(String errorPerMin) {
		this.errorPerMin = errorPerMin;
	}
	public String getConfigSubjectUUIDServiceURL() {
		return configSubjectUUIDServiceURL;
	}
	public void setConfigSubjectUUIDServiceURL(String configSubjectUUIDServiceURL) {
		this.configSubjectUUIDServiceURL = configSubjectUUIDServiceURL;
	}

	@Override
	public String toString() {		
		return "From:"+from+"|"+"To:"+to+"|"+ "CC:"+cc+"|"+"Subject:"+subject+"|"+"MessageId:"+messageId+"|"+"MailerReferencesId:"+mailerReferencesId+"|"+"MailerInReplyToId:"+mailerInReplyToId+"|"+
			   "SubjectUUID:"+Util.getSubjectUUID(subject,getConfigSubjectUUIDServiceURL())+"|"+"SentDate:"+sentDate.toString()+"|"+"Content:"+content+"|"+"HtmlTxtContent:"+htmlTxtContent+"|"+"Application:"+getApplication()+"|"+
		"ErrorMessage:"+errorMessage+"|"+ "ErrorType:"+errorType+"|"+ "EventNotificationName:"+eventNotificationName+"|"+ "EventTime:"+eventTime+"|"+ "EventType:"+eventType+"|"+ "EventTypeName:"+eventTypeName+"|"+ "Count:"+count+"|"+ "Node:"+node+"|"+
		"Severity:"+severity+"|"+ "Source:"+source+"|"+"Tier:"+getTier()+"|"+ "CurValue:"+currentValue+"|"+ "ThValue:"+thresholdValue+"|"+ "BusTrans:"+businessTransaction+"|"+ "JmxInstance:"+jmxInstance+"|"+ "ConditionTime:"+conditionTime+"|"+ "Note:"+note+"|"+ "ViolStatus:"+violStatus+"|"+ 
		"CallsPerMin:"+callsPerMin+"|"+ "AvailValue:"+availVal+"|"+"ErrorsPerMin:"+errorPerMin+"|"+"Origin:"+origin;
	}
		
/*	@Override
	public String toString() {		
		return "from:"+from+" - "+ "to:"+to+" - "+ "cc:"+cc+" - "+  "sentDate:"+sentDate+" - "+ "subject:"+subject+" - "+  "messageId:"+messageId+" - "+ "mailerReferencesId:"+mailerReferencesId+" - "+ "mailerInReplyToId:"+mailerInReplyToId+" - "+
		"origin:"+origin+" - "+"application:"+application+" - "+ "errorMessage:"+errorMessage+" - "+ "errorType:"+errorType+" - "+ "eventNotificationName:"+eventNotificationName+" - "+ "eventTime:"+eventTime+" - "+ "eventType:"+eventType+" - "+ "eventTypeName:"+eventTypeName+" - "+ "count:"+count+" - "+ "node:"+node+" - "+
		"severity:"+severity+" - "+ "source:"+source+" - "+ "currentValue:"+currentValue+" - "+ "thresholdValue:"+thresholdValue+" - "+ "businessTransaction:"+businessTransaction+" - "+ "jmxInstance:"+jmxInstance+" - "+ "tier:"+tier+" - "+ "conditionTime:"+conditionTime+" - "+ "note:"+note+" - "+ "violStatus:"+violStatus+" - "+ 
		"callsPerMin:"+callsPerMin+" - "+ "availVal:"+availVal+" - "+ "errorPerMinDeviation:"+errorPerMinDeviation+" - "+ "errorPerMin:"+errorPerMin;
	}*/

	public String toString1(ApplicationConfigParams config) {		
		return "From:"+from+"|"+"To:"+to+"|"+ "CC:"+cc+"|"+"Subject:"+subject+"|"+"MessageId:"+messageId+"|"+"MailerReferencesId:"+mailerReferencesId+"|"+"MailerInReplyToId:"+mailerInReplyToId+"|"+
			   "SubjectUUID:"+Util.getSubjectUUID(subject,config.getSubjectUUIDServiceURL())+"|"+"SentDate:"+sentDate.toString()+"|"+"Content:"+content+"|"+"HtmlTxtContent:"+htmlTxtContent+"|"+"Application:"+getApplication()+"|"+
		"ErrorMessage:"+errorMessage+"|"+ "ErrorType:"+errorType+"|"+ "EventNotificationName:"+eventNotificationName+"|"+ "EventTime:"+eventTime+"|"+ "EventType:"+eventType+"|"+ "EventTypeName:"+eventTypeName+"|"+ "Count:"+count+"|"+ "Node:"+node+"|"+
		"Severity:"+severity+"|"+ "Source:"+source+"|"+"Tier:"+getTier()+"|"+ "CurValue:"+currentValue+"|"+ "ThValue:"+thresholdValue+"|"+ "BusTrans:"+businessTransaction+"|"+ "JmxInstance:"+jmxInstance+"|"+ "ConditionTime:"+conditionTime+"|"+ "Note:"+note+"|"+ "ViolStatus:"+violStatus+"|"+ 
		"CallsPerMin:"+callsPerMin+"|"+ "AvailValue:"+availVal+"|"+"ErrorsPerMin:"+errorPerMin+"|"+"Origin:"+origin;
	}
	
}
