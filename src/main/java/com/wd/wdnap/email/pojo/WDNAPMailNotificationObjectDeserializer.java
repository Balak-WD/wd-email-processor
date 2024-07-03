package com.wd.wdnap.email.pojo;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;

public class WDNAPMailNotificationObjectDeserializer implements Deserializer<WDNAPMailNotificationObject> {

	private boolean isKey;

	public void configure(Map<String, ?> configs, boolean isKey) {

		this.isKey = isKey;
	}


	public WDNAPMailNotificationObject deserialize(String arg0, byte[] arg1) {

		ObjectMapper mapper = new ObjectMapper();

		WDNAPMailNotificationObject cnapObject = null;

		try {

			cnapObject = mapper.readValue(arg1, WDNAPMailNotificationObject.class);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return cnapObject;

	}

	public void close() {

	}


}