package com.wd.wdnap.email.emailparser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;

@Component
public class AppDynamicAgentStopParser implements WDNAPEmailParser {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AppDynamicAgentStopParser.class);
	@Override
	public String parse(WDNAPMailNotificationObject cnapMailNotificationObj, String originalContent) {
		// TODO Auto-generated method stub

		String content = originalContent.replaceAll("&nbsp;", "");
		StringBuilder builder = new StringBuilder("{");
		try {
			Document doc = Jsoup.parse(content);
			int y = 0;
			Elements ele = doc.getElementsByTag("table");
			Elements trElements = doc.getElementsByTag("tr");
			for (int i = 0; i < trElements.size(); i++) {
				String appDynamicNotification = trElements.get(i).text().replaceAll(" ", "");
				if (appDynamicNotification.equals("AppDynamicsNotification ")
						|| appDynamicNotification.equals("AppDynamicsNotification")) {
					String applicationValue = trElements.get(i + 1).text();
					if (StringUtils.isNotEmpty(applicationValue)) {
						cnapMailNotificationObj.setApplication(applicationValue.trim());
					} else {
						cnapMailNotificationObj.setApplication("");
					}
					builder.append("applicationName=" + applicationValue).append(",\n");
					break;
				}
			}

			for (int i = ele.size() - 1; i > 0; i--) {
				Element element = ele.get(i);

				String value = element.text();
				if (value.contains("Event Type")) {
					// String applicationName = ele.get(i-2).html();
					String eventNotificationName = ele.get(i - 1).getElementsByTag("td").get(0).getElementsByTag("h4")
							.get(0).text();
					if (StringUtils.isNotEmpty(eventNotificationName)) {
						cnapMailNotificationObj.setEventNotificationName(eventNotificationName.trim());
					}

					builder.append("eventNotificationName=" + eventNotificationName).append(",\n");
					// System.out.println("eventNotificationName=" +
					// eventNotificationName);

					// event name and count .i.e healthRule 11
					Elements eventT = ele.get(i).getElementsByTag("tr").get(1).getElementsByTag("td");
					String count = eventT.get(0).text();
					cnapMailNotificationObj.setCount(count.trim());
					builder.append("count=" + count).append(",\n");
					// System.out.println("count=" + eventT.get(0).text());
					String eventTypeName = eventT.get(1).text();
					if (StringUtils.isNotEmpty(eventTypeName)) {
						cnapMailNotificationObj.setEventTypeName(eventTypeName.trim());
					}
					builder.append("eventTypeName=" + eventTypeName).append(",\n");
					// System.out.println("eventTypeName=" +
					// eventT.get(1).text());

					if (ele.get(i + 1) != null && ele.get(i + 1).getElementsByTag("tr").size() >= 3) {
						// eventTime:Thu Sep 21 13:30:33 PDT 2017 ===
						// tier:atacan-PROD ====node:edelapprdlae-kjgylq4iprd-1
						String[] val = ele.get(i + 1).getElementsByTag("tr").get(2).append("|").text().split("\\|");
						if (!val[0].isEmpty()) {
							cnapMailNotificationObj.setEventTime(val[0].trim());
							builder.append("eventTime=" + val[0]).append(",\n");
							// System.out.println("eventTime =" + val[0]);
						}
						if (val.length > 1 && !(val[1].isEmpty())) {
							cnapMailNotificationObj.setTier(val[1].trim());
							builder.append("tier=" + val[1]).append(",\n");
							// System.out.println("tier =" + val[1]);
						}
						if (val.length > 2 && !(val[2].isEmpty())) {
							cnapMailNotificationObj.setNode(val[2].trim());
							builder.append("node=" + val[2]).append(",\n");
							// System.out.println("node =" + val[2]);
						}
						// System.out.println((i+1)+"====element
						// text=-==="+ele.get(i+1).getElementsByTag("tr").get(2).append("|").text());
						// errorMessage
						builder.append("errorMessage=" + ele.get(i + 1).getElementsByTag("tr").get(3).text())
								.append(",\n");
						// System.out.println("errorMessage=" + ele.get(i +
						// 1).getElementsByTag("tr").get(3).text());

						// template formatting for jxminstance
						String errorMessage = ele.get(i + 1).getElementsByTag("tr").get(3).text();
						if (StringUtils.isNotEmpty(errorMessage)) {
							cnapMailNotificationObj.setErrorMessage(errorMessage.trim());
						}
						Elements notesElements = ele.get(i-1).select("td:contains(Notes:)");
						if(notesElements.size()>0){
							String notesValue = notesElements.get(0).text();
							if(StringUtils.isNotEmpty(notesValue) && notesValue.contains("Notes:")){
								notesValue = StringUtils.substringAfter(notesValue, "Notes:").trim();
								cnapMailNotificationObj.setNote(notesValue);
							}
						}
						
						String innermsg = AppDynamicAgentStopParser.parseInnerText(errorMessage, cnapMailNotificationObj);
						builder.append(innermsg);

					}
					String eventType = ele.get(i + 2).text();
					if (StringUtils.isNotEmpty(eventType)) {
						cnapMailNotificationObj.setEventType(eventType.trim());
					}
					builder.append("eventType=" + eventType).append(",\n");
					// System.out.println("eventType=" + ele.get(i + 2).text());
					/*String severity = ele.get(i + 2).getElementsByTag("img").attr("alt");
					if (StringUtils.isNotEmpty(severity)) {
						cnapMailNotificationObj.setSeverity(severity.trim());
						cnapMailNotificationObj.setErrorType(severity.trim());
					}*/
					//builder.append("severity=" + severity).append("\n");
					// System.out.println("severity=" + ele.get(i +
					// 2).getElementsByTag("img").attr("alt"));
					break;
				}
				else if(value.contains("Health Rule Violation")) {
					
					String[] values = value.split(" ");
					cnapMailNotificationObj.setViolStatus(values[0]);
					cnapMailNotificationObj.setSeverity(values[1]);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error("== error in mail util parsing msg content" + e.toString());
		}
		return builder.append("}").toString();
	
	}
	private static String parseInnerText(String innerText, WDNAPMailNotificationObject cnapMailNotificationObj) {
		String msg = innerText;
		StringBuilder builder = new StringBuilder("");
		if (StringUtils.isNoneEmpty(msg)) {
			if (msg.contains("JMX Instance Name")) {
				String jmxInstancevalue = StringUtils.substringBetween(msg, "JMX Instance Name", "\".");
				cnapMailNotificationObj.setJmxInstance(jmxInstancevalue.replaceAll("\"", "").trim());
				builder.append("jmxInstance=" + jmxInstancevalue.replaceAll("\"", "")).append(",\n");

			}
			if (msg.contains("1)") || msg.contains("condition 1") || msg.contains("JVM|Memory:")) {

				String condition1value = StringUtils.substringBetween(msg, "value", "was");
				cnapMailNotificationObj.setCurrentValue(condition1value.trim());
				builder.append("conditionValue=" + condition1value).append(",\n");
				String availValue = StringUtils.substringBetween(msg, "Availability's value", "was");
				if(StringUtils.isNotEmpty(availValue))
					cnapMailNotificationObj.setAvailVal(availValue.trim());
				
				String balanceString = StringUtils.substringAfter(msg, condition1value);
				if (balanceString.contains("threshold")) {

					String threshold = StringUtils.substringBetween(msg, "threshold", "for");
					cnapMailNotificationObj.setThresholdValue(threshold.trim());
					String conditionTime = StringUtils.substringBetween(msg, "last ", "minutes");
					if(conditionTime != null)
						cnapMailNotificationObj.setConditionTime(conditionTime.trim());
					
					builder.append("threshold=" + threshold).append(",\n");
					
					String callsPerMin = StringUtils.substringBetween(balanceString, "Calls per Minute Condition's value", "was");
					
					if(callsPerMin != null)
						cnapMailNotificationObj.setCallsPerMin(callsPerMin.trim());
				}

			}
			// template for Business Transaction parsing
			if (msg.contains("For Business Transaction")) {
				String businessTransaction = StringUtils.substringBetween(msg, "For Business Transaction", ":");
				cnapMailNotificationObj.setBusinessTransaction(businessTransaction.trim());
				builder.append("businessTransaction = " + businessTransaction).append(",\n");

			}
		}
		return builder.toString();

	}
}
