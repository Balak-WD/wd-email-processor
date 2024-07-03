package com.wd.wdnap.email.pojo;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

public class WDNAPMailNotificationObjectSerializer implements Serializer<WDNAPMailNotificationObject> {

	private boolean isKey;

	public void configure(Map<String, ?> configs, boolean isKey) {

		this.isKey = isKey;		

	}



	public byte[] serialize(String arg0, WDNAPMailNotificationObject arg1) {

		byte[] retVal = null;

		ObjectMapper mapper = new ObjectMapper();

		try {

			retVal = mapper.writeValueAsString(arg1).getBytes();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return retVal;	

	}

	
	public void close() {

	}

}
