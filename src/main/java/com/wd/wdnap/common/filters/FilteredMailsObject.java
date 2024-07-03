package com.wd.wdnap.common.filters;

import javax.mail.Message;

public class FilteredMailsObject {

	Message[] usefulMessages;

	Message[] removedMessages;

	public FilteredMailsObject(Message[] usefullMessages, Message[] removedMessages) {

		this.usefulMessages = usefullMessages;
		this.removedMessages = removedMessages;

	}

	public FilteredMailsObject() {
	}

	public Message[] getUsefulMessages() {
		return usefulMessages;
	}

	public void setUsefulMessages(Message[] usefullMessages) {
		this.usefulMessages = usefullMessages;
	}

	public Message[] getRemovedMessages() {
		return removedMessages;
	}

	public void setRemovedMessages(Message[] removedMessages) {
		this.removedMessages = removedMessages;
	}

}
