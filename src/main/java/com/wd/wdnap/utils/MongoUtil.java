package com.wd.wdnap.utils;



import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;


public class MongoUtil {
	
	
	public  void  mongoInsert(){
		
		MongoClient mongoClient = new MongoClient( "clip-prod-98" , 27017 );
		 
		try{
		
			 System.out.println("Inside mongo");
			 MongoCollection<Document> collection = mongoClient.getDatabase("cnapdb").getCollection("emailAuth");
			 Bson filter = new Document("uniqueId", "1");   
			 Bson newValue = new Document("geneatedKey", "77777").append("lastUpdated", new Date());      
			 Bson updateOperationDocument = new Document("$set", newValue);
			 collection.updateMany(filter, updateOperationDocument);
			 System.out.println("updated  mongo");
			 
			 mongoClient.close();
		
		} catch (Exception e) {
			e.printStackTrace();
			mongoClient.close();
		}
		finally {
			mongoClient.close();
		}
		
	}
}
