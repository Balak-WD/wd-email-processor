package com.wd.wdnap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateIDUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIDUtil.class);

	 
	public static String generateSubjectUUID(String subject, String subUUIDURL) {

		String output = null;
		StringBuffer buff = new StringBuffer();

		  try {

			URL url = new URL(subUUIDURL+ URLEncoder.encode(subject));
			LOGGER.info("GenerateIDUtil" + "URL" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			LOGGER.info("GenerateIDUtil" + "Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				buff.append(output);
				LOGGER.info(output);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		  return buff.toString();

		}


	public static String generateMessageID(String from, String messageIDURL) {

		String output = null;
		StringBuffer buff = new StringBuffer();

		  try {

			URL url = new URL(messageIDURL+ URLEncoder.encode(from));
			LOGGER.info("GenerateIDUtil" + "URL" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			LOGGER.info("GenerateIDUtil" + "Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				buff.append(output);
				LOGGER.info(output);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		  return buff.toString();

		}

	
//	public static void main(String []args){
//	
////				String resp = GenerateIDUtil.generateMessageID("shri");
////				System.out.println(resp);
////		
////				JSONObject object = new JSONObject(resp);
////				System.out.println(object.get("id"));
//				
//				System.out.println(Util.getKafkaUniqueRecordKey("shri"));
//
//		
//	}
	
	}