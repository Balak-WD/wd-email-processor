package com.wd.wdnap.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.email.emailparser.WDNAPEmaiParsingFactory;
import com.wd.wdnap.email.emailparser.WDNAPEmailParser;
import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;
import com.wd.code.cnap.kafka.WDNAPKafkaProducer;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.FolderTraversal;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.FolderSchema;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;

public class EWSMailUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(EWSMailUtil.class);
	private static Integer NUMBER_EMAILS_FETCH = 10;
	private ExchangeService service;
	Folder folder;
	// private CNAPMailNotificationObject cnapNotifObj;

	public EWSMailUtil(ExchangeService service) {
		this.service = service;
	}

	public EWSMailUtil(ExchangeService service,String folderName) {
		this.service = service;
		try {
			folder = Folder.bind(service, getFolderId(folderName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Map<String, Object> readEmailItem(ItemId itemId) {
		Map<String, Object> messageData = new HashMap<String, Object>();

		try {
			Item itm = Item.bind(service, itemId, PropertySet.FirstClassProperties);
			EmailMessage emailMessage = EmailMessage.bind(service, itm.getId());
			messageData.put("emailItemId", emailMessage.getId().toString());
			messageData.put("subject", emailMessage.getSubject().toString());
			messageData.put("fromAddress", emailMessage.getFrom().getAddress().toString());
			messageData.put("senderName", emailMessage.getSender().getName().toString());
			Date dateTimeCreated = emailMessage.getDateTimeCreated();
			messageData.put("SendDate", dateTimeCreated.toString());
			Date dateTimeRecieved = emailMessage.getDateTimeReceived();
			messageData.put("RecievedDate", dateTimeRecieved.toString());
			messageData.put("Size", emailMessage.getSize() + "");
			messageData.put("emailBody", emailMessage.getBody().toString());
			// emailMessage.delete(DeleteMode.HardDelete);

		} catch (Exception e) {
			LOGGER.error(" Error in reading the Email :" + e.getMessage());
		}

		return messageData;
	}

	public List<Map<String, Object>> readEmails() {
		List<Map<String, Object>> msgDataList = new ArrayList<Map<String, Object>>();
		try {
			Folder folder = Folder.bind(service, WellKnownFolderName.Inbox);
			FindItemsResults<Item> results = service.findItems(folder.getId(), new ItemView(NUMBER_EMAILS_FETCH));
			int i = 1;
			for (Item item : results) {
				Map<String, Object> messageData = new HashMap<String, Object>();
				messageData = readEmailItem(item.getId());
				System.out.println("\nEmails #" + (i++) + ":");
				System.out.println("subject : " + messageData.get("subject").toString());
				System.out.println("Sender : " + messageData.get("senderName").toString());
				System.out.println(messageData.get("emailBody").toString());
				msgDataList.add(messageData);
				// item.delete(DeleteMode.HardDelete);
			}
		} catch (Exception e) {
			LOGGER.error(" Error in reading the Email  readEmails():" + e.getMessage());
		}
		return msgDataList;
	}

	public void processEmails(WDNAPKafkaProducer kp) {

		try {

				//Folder folder = Folder.bind(service, getFolderId("AppDynamic"));
			FindItemsResults<Item> results = service.findItems(folder.getId(), new ItemView(NUMBER_EMAILS_FETCH));
			LOGGER.info("Start Processing emials :");
			LOGGER.info("No Of emails Fetched :"+results.getItems().size());
			for (Item item : results) {

				WDNAPMailNotificationObject obj = getCNAPObjectFromItem(item.getId());
				kp.publish(obj);
				System.out.println(obj);
				item.delete(DeleteMode.HardDelete);
			}
			
			LOGGER.info("End of Processing emials");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WDNAPMailNotificationObject getCNAPObjectFromItem(ItemId itemId) {

		WDNAPMailNotificationObject cnapNotifObj = new WDNAPMailNotificationObject();
		StringBuilder builder = null;

		try {

			Item itm = Item.bind(service, itemId, PropertySet.FirstClassProperties);
			EmailMessage emailMessage = EmailMessage.bind(service, itm.getId());

			if (emailMessage.getFrom() != null) {
				cnapNotifObj.setFrom(emailMessage.getFrom().getAddress());
			}

			if (emailMessage.getToRecipients() != null) {
				builder = new StringBuilder();
				EmailAddressCollection toCollection = emailMessage.getToRecipients();

				Iterator<EmailAddress> iteraor = toCollection.iterator();

				if (iteraor.hasNext()) {
					do {
						builder.append(iteraor.next().getAddress() + ",");
					} while (iteraor.hasNext());

				}

				cnapNotifObj.setTo(builder.toString());
			}

			if (emailMessage.getCcRecipients() != null) {
				builder = new StringBuilder();
				EmailAddressCollection toCollection = emailMessage.getCcRecipients();

				Iterator<EmailAddress> iteraor = toCollection.iterator();

				if (iteraor.hasNext()) {
					do {
						builder.append(iteraor.next().getAddress() + ",");
					} while (iteraor.hasNext());

				}

				cnapNotifObj.setCC(builder.toString());
			}

			if (emailMessage.getDateTimeSent() != null) {
				cnapNotifObj.setSentDate(emailMessage.getDateTimeSent());
			}

			if (emailMessage.getInternetMessageId() != null) {
				cnapNotifObj.setMessageId(emailMessage.getId().getUniqueId());
			}

			if (emailMessage.getConversationId() != null) {
				cnapNotifObj.setMailerReferencesId(emailMessage.getConversationId().getUniqueId());
			}

			if (emailMessage.getReplyTo() != null) {

				builder = new StringBuilder();
				EmailAddressCollection toCollection = emailMessage.getReplyTo();

				Iterator<EmailAddress> iteraor = toCollection.iterator();

				if (iteraor.hasNext()) {
					do {
						builder.append(iteraor.next().getAddress() + ",");
					} while (iteraor.hasNext());

				}

				cnapNotifObj.setMailerInReplyToId(builder.toString());
			}

			if (emailMessage.getSubject() != null)
				cnapNotifObj.setSubject(emailMessage.getSubject());

			if (emailMessage.getBody().getBodyType() == BodyType.HTML) {
				cnapNotifObj.setContent(MailUtil.getHtmlTxtContent(emailMessage.getBody().toString(), "HTML"));
			} else if (emailMessage.getBody().getBodyType() == BodyType.Text) {
				cnapNotifObj.setContent(emailMessage.getBody().toString());
				cnapNotifObj.setHtmlTxtContent(MailUtil.getHtmlTxtContent(emailMessage.getBody().toString(), "TEXT"));
			}

			EDSUtil.setOrigin(cnapNotifObj);

			WDNAPEmailParser appDynamicsParser = WDNAPEmaiParsingFactory.getCNAPEmailParser(cnapNotifObj.getOrigin());
			String parsemessage = appDynamicsParser.parse(cnapNotifObj, cnapNotifObj.getContent());

		} catch (Exception e) {
			LOGGER.error("Exception e Reading email from Item :" + e.getLocalizedMessage());
		}

		return cnapNotifObj;

	}

	public FolderId getFolderId(String folderName) throws Exception{
		
		FolderId id = null;
		
		 	FolderView view = new FolderView(100);
		 	view.setTraversal(FolderTraversal.Deep);
		 	PropertySet pset = new PropertySet(BasePropertySet.IdOnly);
		 	pset.add(FolderSchema.DisplayName);
	        view.setPropertySet(pset);
	        
	       
	        
	       
	        FindFoldersResults findFolderResults = service.findFolders(WellKnownFolderName.Root, view);
	        //find specific folder
	        for(Folder f  :findFolderResults)
	        {
	            if (f.getDisplayName().equalsIgnoreCase(folderName)){
	               id = f.getId();
	               break;
	            }
	        }
	        
	        return id;
		
	}
}
