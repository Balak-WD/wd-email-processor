package com.wd.wdnap.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HttpPost {
	public static void main(String[] args){
		
	try{	
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("https://laeb-rtp1-brk01.cisco.com/broker/rest/application/58dab378018f1997e8000221/cartridge/jbossews-2.0")
		  .get()
		  .addHeader("accept", "application/json")
		  .addHeader("authorization", "Basic eHhsYWVhZG06QWRtaW5sYWUxMjM=")
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "3c9d48ab-5394-0bcf-aaa9-75b9c9d506c2")
		  .build();

		Response response = client.newCall(request).execute();
	}catch(Exception e){
		e.printStackTrace();
	}
	}

}
