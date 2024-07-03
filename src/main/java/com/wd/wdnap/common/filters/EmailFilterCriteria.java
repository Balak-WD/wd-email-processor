package com.wd.wdnap.common.filters;

import javax.mail.Message;


public interface EmailFilterCriteria {

	public FilteredMailsObject meetCriteria(Message[] allMessages);

}
