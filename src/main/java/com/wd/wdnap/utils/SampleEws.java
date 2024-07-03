package com.wd.wdnap.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class SampleEws {

	 private static ExchangeService service;
	 private static Integer NUMBER_EMAILS_FETCH = 10;
	 
	 static{
		 try {
	            service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
	            service.setUrl(new URI("https://mail.cisco.com/ews/Exchange.asmx"));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public SampleEws(){
		  ExchangeCredentials credentials = new WebCredentials("cnap.gen", "T0p$ecret", "cisco.com");
	        service.setCredentials(credentials);
	 }
	    
	public static void main(String[] args) {
		SampleEws mail = new SampleEws();
		mail.readEmails();
		  
		        
		  
	}
	
	
	public List<Map>  readEmails() {
        List<Map> msgDataList = new ArrayList ();
        try {
            Folder folder = Folder.bind(service, WellKnownFolderName.Inbox);
            FindItemsResults<Item> results = service.findItems(folder.getId(), new ItemView(NUMBER_EMAILS_FETCH));
            int i = 1;
            for (Item item :  results) {
                Map messageData = new HashMap();
                messageData = readEmailItem(item.getId());
                System.out.println("\nEmails #" + (i++) + ":");
                System.out.println("subject : " + messageData.get("subject").toString());
                System.out.println("Sender : " + messageData.get("senderName").toString());
                msgDataList.add(messageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgDataList;
    }
	
	
	  public Map readEmailItem(ItemId itemId) {
	        Map messageData = new HashMap();
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
	          //  emailMessage.delete(DeleteMode.HardDelete);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return messageData;
	    }


}
