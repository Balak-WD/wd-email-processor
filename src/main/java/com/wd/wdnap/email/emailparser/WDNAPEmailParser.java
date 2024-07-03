package com.wd.wdnap.email.emailparser;

import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;

public interface WDNAPEmailParser {
	
	public String parse(WDNAPMailNotificationObject mailObject, String content);

}
