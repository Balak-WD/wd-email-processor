package com.wd.code.cnap.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;
import com.wd.wdnap.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class WDNAPKafkaProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPKafkaProducer.class);
	private static final String AD_TOPIC = "appdynamics.alldc.prod.events.allapps";
	Properties props1 = null;
	Producer<String, String> producer1 = null;

	Properties props = null;
	Producer<String, String> producer = null;
	ProducerRecord<String, String> record = null;

	@Autowired
	public Util util;

	@Value("${application.idServiceURL}")
	public String idServiceURL;

	@Value("${application.subjectUUIDServiceURL}")
	public String subjectUUIDServiceURL;


	@Value("${application.kafkaBootstrapServers}")
	public String bootstrapServers;

	public WDNAPKafkaProducer() {
		props = util.loadKafkaProperties();
		producer = new KafkaProducer<>(props);

	}

	public void publish(WDNAPMailNotificationObject WDNAPMailNotificationObject) {

		String topicName = "NotificationTopic";
		String key = Util.getKafkaUniqueRecordKey(WDNAPMailNotificationObject.getFrom(),idServiceURL);
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("From:" + WDNAPMailNotificationObject.getFrom() + "|");
		sBuffer.append("To:" + WDNAPMailNotificationObject.getTo() + "|");
		sBuffer.append("CC:" + WDNAPMailNotificationObject.getCC() + "|");
		sBuffer.append("Subject:" + WDNAPMailNotificationObject.getSubject() + "|");
		sBuffer.append("MessageId:" + WDNAPMailNotificationObject.getMessageId() + "|");
		sBuffer.append("MailerReferencesId:" + WDNAPMailNotificationObject.getMailerReferencesId() + "|");
		sBuffer.append("MailerInReplyToId:" + WDNAPMailNotificationObject.getMailerInReplyToId() + "|");

		sBuffer.append("SubjectUUID:"
				+ Util.getSubjectUUID(WDNAPMailNotificationObject.getSubject(), subjectUUIDServiceURL)
				+ "|");

		sBuffer.append("SentDate:" + WDNAPMailNotificationObject.getSentDate().toString() + "|");

		sBuffer.append("Content:" + WDNAPMailNotificationObject.getContent() + "|");
		sBuffer.append("HtmlTxtContent:" + WDNAPMailNotificationObject.getHtmlTxtContent() + "|");

		sBuffer.append("Application:" + WDNAPMailNotificationObject.getApplication() + "|");
		sBuffer.append("ErrorMessage:" + WDNAPMailNotificationObject.getErrorMessage() + "|");
		sBuffer.append("ErrorType:" + WDNAPMailNotificationObject.getErrorType() + "|");
		sBuffer.append("EventNotificationName:" + WDNAPMailNotificationObject.getEventNotificationName() + "|");
		sBuffer.append("EventTime:" + WDNAPMailNotificationObject.getEventTime() + "|");
		sBuffer.append("EventType:" + WDNAPMailNotificationObject.getEventType() + "|");
		sBuffer.append("EventTypeName:" + WDNAPMailNotificationObject.getEventTypeName() + "|");
		sBuffer.append("Count:" + WDNAPMailNotificationObject.getCount() + "|");
		sBuffer.append("Node:" + WDNAPMailNotificationObject.getNode() + "|");
		sBuffer.append("Severity:" + WDNAPMailNotificationObject.getSeverity() + "|");
		sBuffer.append("Source:" + WDNAPMailNotificationObject.getSource() + "|");
		sBuffer.append("Tier:" + WDNAPMailNotificationObject.getTier() + "|");
		sBuffer.append("CurValue:" + WDNAPMailNotificationObject.getCurrentValue() + "|");
		sBuffer.append("ThValue:" + WDNAPMailNotificationObject.getThresholdValue() + "|");
		sBuffer.append("BusTrans:" + WDNAPMailNotificationObject.getBusinessTransaction() + "|");
		sBuffer.append("JmxInstance:" + WDNAPMailNotificationObject.getJmxInstance() + "|");
		sBuffer.append("ConditionTime:" + WDNAPMailNotificationObject.getConditionTime() + "|");
		sBuffer.append("Note:" + WDNAPMailNotificationObject.getNote() + "|");
		sBuffer.append("ViolStatus:" + WDNAPMailNotificationObject.getViolStatus() + "|");
		sBuffer.append("CallsPerMin:" + WDNAPMailNotificationObject.getCallsPerMin() + "|");
		sBuffer.append("AvailValue:" + WDNAPMailNotificationObject.getAvailVal() + "|");
		sBuffer.append("ErrorsPerMin:" + WDNAPMailNotificationObject.getErrorPerMin() + "|");

		sBuffer.append("Origin:" + WDNAPMailNotificationObject.getOrigin());

		String value = sBuffer.toString();

		// LOGGER.info("CNAPKafkaProducer - Going to publish the message with
		// key "+ key );
		// LOGGER.info("CNAPKafkaProducer - Going to publish the message with
		// value "+ value );

		try {

			record = new ProducerRecord(topicName, key, value);
			producer.send(record);
			// producer.close();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in Posting Kafka message to : " + topicName);
		}

		// Topic to post messages to Appd kafka cluster
		/*
		 * try{
		 * 
		 * 
		 * ProducerRecord<String, String> record1 = new ProducerRecord
		 * (AD_TOPIC,value); producer1.send(record1); //producer1.close();
		 * 
		 * }catch(Exception e){
		 * LOGGER.error("Exception in Posting Kafka message to : "+AD_TOPIC); }
		 */
	}

	public void publish(WDNAPMailNotificationObject WDNAPMailNotificationObject,
						int threadId) {
		//String topicName = "unix_alerts";
		String topicName = "NotificationTopic";
		String key = Util.getKafkaUniqueRecordKey(WDNAPMailNotificationObject.getFrom(),idServiceURL);
		WDNAPMailNotificationObject.setConfigSubjectUUIDServiceURL(subjectUUIDServiceURL);

		String value = WDNAPMailNotificationObject.toString();

		try {
			record = new ProducerRecord(topicName, key, value);
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception in Posting Kafka message to : " + topicName);
		}
	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

}
