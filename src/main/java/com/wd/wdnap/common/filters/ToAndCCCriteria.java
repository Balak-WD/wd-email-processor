package com.wd.wdnap.common.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.config.ApplicationConfigParams;

public class ToAndCCCriteria implements EmailFilterCriteria {

	private ApplicationConfigParams config;


	private static final Logger LOGGER = LoggerFactory.getLogger(ToAndCCCriteria.class);

	public ToAndCCCriteria(ApplicationConfigParams config) {

		this.config = config;
	}



	@Override
	public FilteredMailsObject meetCriteria(Message [] messages) {

		ArrayList<Message> goodMessageList = new ArrayList<Message>();
		ArrayList<Message> badMessageList = new ArrayList<Message>();

		FilteredMailsObject filteredMailsObject = new FilteredMailsObject();

		LOGGER.info("ToAndCCCriteria - in meetCriteria Messages length " + messages.length);

		try {
			String [] allowedList = getAllowedList();

			for (int i = 0; i < messages.length; i++) {

				Message inspectedMessage = messages[i];

				Address[] addesses = inspectedMessage.getAllRecipients();

				if(isRecipientInAllowedList(allowedList,addesses)){

					goodMessageList.add(inspectedMessage);
				}else{
					badMessageList.add(inspectedMessage);
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Message[] goodMessages = (Message[]) goodMessageList.toArray(new Message[goodMessageList.size()]);	
		LOGGER.info("ToAndCCCriteria - in meetCriteria Messages length " + goodMessages.length);
		Message[] badMessages = (Message[]) badMessageList.toArray(new Message[badMessageList.size()]);	
		LOGGER.info("ToAndCCCriteria - in meetCriteria Messages length " + messages.length);

		filteredMailsObject.setUsefulMessages(goodMessages);
		filteredMailsObject.setRemovedMessages(badMessages);

		return filteredMailsObject;


	}



	private boolean isRecipientInAllowedList(String[] allowedList, Address[] addesses) {

		for (int i = 0; i < allowedList.length; i++){

			for (int j = 0; j < addesses.length; j++){
				String toAddrs = addesses[j].toString().toLowerCase();
				//		LOGGER.info("isRecipientInAllowedList - toAddrs " + toAddrs);


				if(toAddrs.indexOf(allowedList[i]) >= 0)	{
					//			LOGGER.info("toAddrs.indexOf(allowedList[i]) >= 0 " + toAddrs);

					return true;
				}

			}
		}
		return false;
	}


	public String getJSONforAllowedRecepients() {

		String output = null;
		StringBuffer buff = new StringBuffer();

		try {

			LOGGER.info("ToAndCCCriteria" + "URL" + config.getToServiceURL());

			URL url = new URL( config.getToServiceURL());

			LOGGER.info("ToAndCCCriteria" + "URL" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			//		LOGGER.info("ToAndCCCriteria" + "Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				buff.append(output);
				//			LOGGER.info(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return buff.toString();

	}


	public String[] getAllowedList(){

		JSONObject object = new JSONObject(getJSONforAllowedRecepients());
		JSONArray jsonarray = (JSONArray) object.get("list");

		int length = jsonarray.length();
		String []  allowedMailers = new String[length];

		for (int i = 0; i < length; i++){

			allowedMailers[i] = (String) jsonarray.get(i);

		}

		return allowedMailers;

	}




}