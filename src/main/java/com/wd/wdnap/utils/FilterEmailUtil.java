package com.wd.wdnap.utils;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wd.wdnap.common.filters.EmailFilterCriteria;
import com.wd.wdnap.common.filters.FilteredMailsObject;
import com.wd.wdnap.common.filters.ToAndCCCriteria;
import com.wd.wdnap.config.ApplicationConfigParams;

public class FilterEmailUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterEmailUtil.class);


	public static FilteredMailsObject applyFilters(Message[] allMessages, ApplicationConfigParams config) {
		
		LOGGER.info("In FilterEmailUtil");
		
		if(allMessages == null){ 
			
			return new FilteredMailsObject();
		}else{
			
			LOGGER.info("In FilterEmailUtil Message[] Length BEFORE filtering " + allMessages.length);
			EmailFilterCriteria criteria = new ToAndCCCriteria(config);
			FilteredMailsObject filteredMailsObject = criteria.meetCriteria(allMessages);
			
			if(filteredMailsObject.getUsefulMessages() != null){ 
			LOGGER.info("In FilterEmailUtil Message[] Length AFTER filtering " + filteredMailsObject.getUsefulMessages().length);
			}
			
			return filteredMailsObject;

		}
		
	}

}
