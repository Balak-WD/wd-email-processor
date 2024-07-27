package com.wd.wdnap.email.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wd.wdnap.config.ApplicationConfigParams;
import com.wd.wdnap.email.pojo.WDNAPMailNotificationObject;
import com.wd.wdnap.utils.EWSMailUtil;
import com.wd.wdnap.utils.MailUtil;
import com.wd.wdnap.utils.Util;
import com.wd.code.cnap.kafka.WDNAPKafkaProducer;

@Service
public class WDNAPEmailService {


	private EWSMailUtil mailUtil;

	@Autowired
	public Util util;


	@Value("${application.host}")
	private String host;
	@Value("${application.username}")
	private String username;
	@Value("${application.password}")
	private String password;
	@Value("${application.mailStoreType}")
	private String mailStoreType;
	private int threadIdCounter = 1;

	private String folder=null;

	private static final Logger LOGGER = LoggerFactory.getLogger(WDNAPEmailService.class);

	@Autowired
	WDNAPKafkaProducer kp;

	public WDNAPEmailService(){

	}

	public WDNAPEmailService(ApplicationConfigParams applicationConfigParams) {
		//Skp = new WDNAPKafkaProducer(config);
		folder = System.getProperty("Folder");
	}

	public void initAndFetch() {
		fetchMail(host, mailStoreType, username, password);
	}

	public void fetch(String pop3Host, String storeType, String user, String password) {
		try {
			LOGGER.info("CNAPEmailService  - fetch");

			// load the properties
			Properties properties = util.loadEmailProperties(pop3Host);

			// establish the session
			Session emailSession = Session.getDefaultInstance(properties);
			// emailSession.setDebug(true);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");
			store.connect(pop3Host, user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			// Folder cnapFolder = store.getFolder("CNAPREAD");

			emailFolder.open(Folder.READ_WRITE);
			// cnapFolder.open(Folder.READ_WRITE);

			int msgcount;

			// while(emailFolder.hasNewMessages()){

			msgcount = emailFolder.getMessageCount();
			Message[] allMessages = null;

			if (msgcount > 20) {
				allMessages = emailFolder.getMessages(1, 20);
			} else {
				allMessages = emailFolder.getMessages();
			}

			// Message[] allMessages = emailFolder.getMessages();
			LOGGER.info("CNAPEmailService " + "All messages.length---" + allMessages.length);

			// TODO:open later
			// FilteredMailsObject filteredMailsObject =
			// FilterEmailUtil.applyFilters(allMessages,config);
			// Message[] messages = filteredMailsObject.getUsefulMessages();

			LOGGER.info("CNAPEmailService " + "Filtered messages.length---" + allMessages.length);

			for (int i = 0; i < allMessages.length; i++) {
				Message message = allMessages[i];

				LOGGER.info("CNAPEmailService " + "--------------------------------- Message No " + i);

				WDNAPMailNotificationObject cnapMessage = MailUtil.getCNAPObjectFromMessage(message);

				kp.publish(cnapMessage);
				// TODO:find way to callback and delete on receive success
				if (!message.isSet(Flags.Flag.DELETED)) {
					// cnapFolder.appendMessages(new Message[] {message});
					message.setFlag(Flag.DELETED, true);
				}

				// LOGGER.info(" MimeMessage " + message.toString());
				LOGGER.info("CNAPEmailService " + "--------------------------------- End Message No " + i);
			}

			// TODO:open later
			// Message[] remMessages = filteredMailsObject.getRemovedMessages();
			// for (int i = 0; i < remMessages.length; i++) {
			//
			// Message message = remMessages[i];
			// if(!message.isSet(Flags.Flag.DELETED)){
			// // cnapFolder.appendMessages(new Message[] {message});
			// message.setFlag(Flag.DELETED, true);
			// }
			// }
			// }
			// close the store and folder objects
			emailFolder.close(true);
			LOGGER.info("CNAPEmailService  - email folder close");
			store.close();
			LOGGER.info("CNAPEmailService  - store close");
		} catch (NoSuchProviderException e) {
			LOGGER.error("NoSuchProviderException", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			LOGGER.error("MessagingException", e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("IOException", e);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception", e);
			e.printStackTrace();
		}
	}

	public void fetchMail(String imapHost, String storeType, String user, String password) {
		long startTime = System.currentTimeMillis();
		Store store = null;
		Folder emailFolder = null;
		try {
			LOGGER.info("WDNAPEmailService  - fetch - email - imap - protocal.");
			// load the properties
			Properties properties = Util.loadIMAPEmailProperties(imapHost);

			// establish the session
			Session emailSession = Session.getDefaultInstance(properties);
			// emailSession.setDebug(true);

			// create the POP3 store object and connect with the pop server
			store = emailSession.getStore("imaps");
			store.connect(imapHost, user, password);

			// create the folder object and open it
			emailFolder = store.getFolder(folder);
			emailFolder.open(Folder.READ_WRITE);

			int THREAD_COUNT;
			int msgcount = emailFolder.getMessageCount();
			System.out.println("messgae count: " + msgcount);
			if (msgcount > 0) {
				if (msgcount >= 50) {
					THREAD_COUNT = 50;
				} else {
					THREAD_COUNT = msgcount;
				}

				ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
				List<ImapsMailBoxExecutor> executors = new ArrayList<ImapsMailBoxExecutor>();
				for (int i = 1; i <= THREAD_COUNT; i++) {
					// executors.add(new ImapsMailBoxExecutor(emailFolder, kp,
					// config, threadIdCounter++));
					executors.add(new ImapsMailBoxExecutor(emailFolder, kp, i));
				}

				List<Future<Boolean>> futures = threadPool.invokeAll(executors);
				threadPool.shutdown();
			}
			emailFolder.close(true);
			LOGGER.info("WDNAPEmailService  - email folder close");
			store.close();
			LOGGER.info("WDNAPEmailService  - store close");
			LOGGER.info("WDNAPEmailService  - store close");
		} catch (NoSuchProviderException e) {
			LOGGER.error("NoSuchProviderException", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			LOGGER.error("MessagingException", e);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception", e);
			e.printStackTrace();
		}
		finally {
			
			try {
				
				if(emailFolder.isOpen())
					emailFolder.close(true);
				LOGGER.info(" email folder closed finally");
			   if(store.isConnected())	
				store.close();
				LOGGER.info(" store  closed finally");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
		}
		

	}

	private class ImapsMailBoxExecutor implements Callable<Boolean> {
		WDNAPKafkaProducer producer;
	   private final int myThreadId;
		Folder emailFolder;

		public ImapsMailBoxExecutor(Folder folder, WDNAPKafkaProducer kafkaProducer,
				 int threadId) {
			this.emailFolder = folder;
			this.producer = kafkaProducer;
			this.myThreadId = threadId;
		}

		@Override
		public Boolean call() {
			try {
				WDNAPMailNotificationObject message = getNextCnapMail();
				producer.publish(message, myThreadId);

			} catch (Exception ex) {
				LOGGER.error("Exception at thread :", ex);
				//CNAPMailNotificationObject message1 = new CNAPMailNotificationObject();
				//message1.setFrom("testmail@cisc.com");
				//message1.setContent("test");
				//producer.publish(message1, confParams, myThreadId);
			}
			return true;
		}

		public WDNAPMailNotificationObject getNextCnapMail() {
			try {
				long startTime = System.currentTimeMillis();
				int thread = this.myThreadId;
				int threadMsgNo = thread++;
				WDNAPMailNotificationObject cnapMessage;
				Message[] allMessages = emailFolder.getMessages(threadMsgNo, threadMsgNo);
			/*	
				// System.out.println("thread:: -----"+thread +"thread msg
				// no"+threadMsgNo);
				Message[] allMessages = emailFolder.getMessages(threadMsgNo, threadMsgNo);
				// System.out.println("MESSAGE"+allMessages[0].getSubject());
				if (allMessages[0].getSubject().contains(config.getMonitorMailSub())) {
					System.out.println("MESSAGE"+allMessages[0].getSubject());
					cnapMessage = new CNAPMailNotificationObject();
					cnapMessage.setContent("No Text or HTML content");
					// System.out.println("SUBJECT MESSAGE MATCHED");
					MongoUtil updateDb = new MongoUtil();
					updateDb.mongoInsert();
				} else {
			*/		cnapMessage = MailUtil.getCNAPObjectFromMessage(allMessages[0]);
			//	}
				if (!allMessages[0].isSet(Flags.Flag.DELETED)) {
					allMessages[0].setFlag(Flag.DELETED, true);
				}

				return cnapMessage;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

	}// End of Thread Class.

}
