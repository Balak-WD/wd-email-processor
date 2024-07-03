package com.wd.wdnap.common.filters;

import javax.mail.Message;

public class FromCriteria implements EmailFilterCriteria {

	@Override
	public FilteredMailsObject meetCriteria(Message[] allMessages) {
	 
		return new FilteredMailsObject(allMessages,allMessages);
	}



}
