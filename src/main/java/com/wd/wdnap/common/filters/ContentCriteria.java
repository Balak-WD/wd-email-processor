package com.wd.wdnap.common.filters;

import javax.mail.Message;

public class ContentCriteria implements EmailFilterCriteria {

 
	private static final String FilteredMailsObject = null;

	@Override
	public FilteredMailsObject meetCriteria(Message[] allMessages) {
		
		
		//TODO: Remove dummy implementation
		return new FilteredMailsObject(allMessages,allMessages);
	}

}
