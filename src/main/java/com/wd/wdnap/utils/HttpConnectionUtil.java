package com.wd.wdnap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpConnectionUtil {

	public static String callURL(String urlString) {

		String output = null;
		StringBuffer buff = new StringBuffer();

		try {

			URL url = new URL(urlString);
			System.out.println("GenerateIDUtil" + "URL" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			System.out.println("GenerateIDUtil" + "Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				buff.append(output);
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return buff.toString();

	}

	private static int getHttpCall(String apiURL) {
		int currentnumber = 0;
		try {

			URL url = new URL(apiURL);
			Map<String, Object> params = new LinkedHashMap<>();
			// params.put("application/x-www-form-urlencoded",
			// "scales_from=1&scales_to=2");
			// params.put("email", "fishie@seamail.example.com");
			// params.put("reply_to_thread", 10394);
			// params.put("message", "Shark attacks in Botany Bay have gotten
			// out of control. We need more defensive dolphins to protect the
			// schools here, but Mayor Porpoise is too busy stuffing his snout
			// with lobsters. He's so shellfish.");

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			doTrustToCertificates();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "Basic eHhsYWVhZG06QWRtaW5sYWUxMjM=");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			// conn.getOutputStream().write(postDataBytes);

			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			JsonObject obj = new JsonParser().parse(in).getAsJsonObject();
			currentnumber = obj.get("data").getAsJsonObject().get("current_scale").getAsInt();

			System.out.println("current value is " + currentnumber);

			/*
			 * for (int c; (c = in.read()) >= 0;) System.out.print((char)c);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return currentnumber;
	}

	private static String putHttpCall(String apiURL, int instanceVal) {
		System.out.println("==========calling scaleup api ============");
		String responseStatus = "";
		try {

			URL url = new URL(apiURL);
			Map<String, Object> params = new LinkedHashMap<>();
			// params.put("application/x-www-form-urlencoded",
			// "scales_from=1&scales_to=2");
			params.put("scales_from", instanceVal + 1);
			params.put("scales_to", instanceVal + 1);
			// params.put("message", "Shark attacks in Botany Bay have gotten
			// out of control. We need more defensive dolphins to protect the
			// schools here, but Mayor Porpoise is too busy stuffing his snout
			// with lobsters. He's so shellfish.");

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			doTrustToCertificates();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("authorization", "Basic eHhsYWVhZG06QWRtaW5sYWUxMjM=");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);
			responseStatus = conn.getResponseMessage();
			System.out.println("final response==" + responseStatus);

			/*
			 * Reader in = new BufferedReader(new
			 * InputStreamReader(conn.getInputStream(), "UTF-8")); JsonObject
			 * obj = new JsonParser().parse(in).getAsJsonObject(); currentnumber
			 * =obj.getAsString();
			 * 
			 * System.out.println("response messege " + currentnumber);
			 * 
			 * 
			 * for (int c; (c = in.read()) >= 0;) System.out.print((char)c);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return responseStatus;
	}

	public static void doTrustToCertificates() throws Exception {
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				// No need to implement.
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				// No need to implement.
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String urlHostName, SSLSession session) {
				if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
					System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
							+ session.getPeerHost() + "'.");
				}
				return true;
			}

		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	public static void main(String[] args) {
		String url = "https://laeb-rtp1-brk01.cisco.com/broker/rest/domains/autoscaling/applications/imxv0r3xdev/cartridges/jbossews-2.0";
	    int currentValue = HttpConnectionUtil.getHttpCall(url);
		if (currentValue > 0) {
			String responseStatus = HttpConnectionUtil.putHttpCall(url, currentValue);
			System.out.println("finall response STATUS" + responseStatus);
		}

	}
}