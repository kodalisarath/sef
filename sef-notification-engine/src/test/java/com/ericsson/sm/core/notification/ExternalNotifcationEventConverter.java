package com.ericsson.sm.core.notification;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsmpp.bean.MessageClass;
import org.springframework.core.io.FileSystemResource;

import com.ericsson.raso.sef.core.db.model.ChargeAmount;
import com.ericsson.sm.core.Meta;
import com.ericsson.sm.core.config.Language;
import com.ericsson.sm.core.db.model.CurrencyCode;


public class ExternalNotifcationEventConverter {

	public final static void main (String args[]) throws Exception{
		FileSystemResource resource = new FileSystemResource("C:\\tmp\\external-notifications.ccem");
		if(resource.exists())  {
			ObjectInputStream stream = new ObjectInputStream(resource.getInputStream());
			Map<String, ExternalNotifcationEvent> externalNotifications = (Map<String, ExternalNotifcationEvent>) stream.readObject();
			stream.close();
			Map<String, com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent> newresult = new HashMap<String, com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent>();
			//System.out.println(externalNotifications);
			
			Set<String> keys = externalNotifications.keySet();
			Iterator<String> it = keys.iterator();
			
			while (it.hasNext()) {
				
				String next = it.next();
				ExternalNotifcationEvent oldEvent = externalNotifications.get(next);
				com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent newEvent = new com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent();
				if (oldEvent.getMessages().size()>0)
				System.out.println(oldEvent.getEventId()+"-"+oldEvent.getDescription()+"-"+oldEvent.getMessages().get(0).getMessage());
				newEvent.setAction(toAction(oldEvent.getAction()));
				newEvent.setChargeAmount(new ChargeAmount(oldEvent.getChargeAmount().getAmount(), toCurrencyCode(oldEvent.getChargeAmount().getCurrencyCode())));
				newEvent.setDescription(oldEvent.getDescription());
				newEvent.setEventId(oldEvent.getEventId());
				newEvent.setMessages(toMessageList(oldEvent.getMessages()));
				newEvent.setMetas(toMetaList(oldEvent.getMetas()));
				newEvent.setSenderAddr(oldEvent.getSenderAddr());
				newEvent.setWsClientId(oldEvent.getWsClientId());
				newresult.put(next, newEvent);
				 
			}
			//System.out.println(newresult);
			
			FileSystemResource fyr = new FileSystemResource("C:\\tmp\\external-notifications-new.ccem");
			if(!fyr.exists()) {
				fyr.getFile().createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(fyr.getOutputStream());
			oos.writeObject(newresult);
			oos.flush();
			oos.close();
		}
		
		  
	}
	
	public static com.ericsson.raso.sef.core.ne.NotificationAction toAction(NotificationAction action){
		switch (action){
		case ASIS:
			return com.ericsson.raso.sef.core.ne.NotificationAction.ASIS;
		case MASSAGE:
			return com.ericsson.raso.sef.core.ne.NotificationAction.MASSAGE;
		case WORKFLOW:
			return com.ericsson.raso.sef.core.ne.NotificationAction.WORKFLOW;
		}
		return null;
	}
	
	public static com.ericsson.raso.sef.core.db.model.CurrencyCode toCurrencyCode(CurrencyCode code){
		switch (code){
		case CENTS:
			return com.ericsson.raso.sef.core.db.model.CurrencyCode.CENTS;
		case PHP:
			return com.ericsson.raso.sef.core.db.model.CurrencyCode.PHP;
		}
		return null;
	}
	
	public static com.ericsson.raso.sef.core.ne.Language toLanguage(Language code){
		switch (code){
		case bahasa:
			return com.ericsson.raso.sef.core.ne.Language.bahasa;
		case en:
			return com.ericsson.raso.sef.core.ne.Language.en;
		case fil:
			return com.ericsson.raso.sef.core.ne.Language.fil;
		}
		return null;
	}
	
	public static com.ericsson.raso.sef.core.ne.NotificationMessage toNotificationMessage(NotificationMessage message){
		com.ericsson.raso.sef.core.ne.NotificationMessage newMessage = new com.ericsson.raso.sef.core.ne.NotificationMessage();
		newMessage.setId(message.getId());
		newMessage.setLang(toLanguage(message.getLang()));
		newMessage.setMessage(message.getMessage());
		return newMessage;
	}
	
	public static List<com.ericsson.raso.sef.core.ne.NotificationMessage> toMessageList (List<NotificationMessage> oldList){
		
		List<com.ericsson.raso.sef.core.ne.NotificationMessage> result = 
				new ArrayList<com.ericsson.raso.sef.core.ne.NotificationMessage>();
		
		for (NotificationMessage notificationMessage : oldList) {
			result.add(toNotificationMessage(notificationMessage));
		}
		return result;
	}
	
	public static com.ericsson.raso.sef.core.Meta toMeta(Meta oldMeta){
		com.ericsson.raso.sef.core.Meta newMeta = new com.ericsson.raso.sef.core.Meta();
		newMeta.setKey(oldMeta.getKey());
		newMeta.setValue(oldMeta.getValue());
		return newMeta;
	}
	
	public static List<com.ericsson.raso.sef.core.Meta> toMetaList (List<Meta> oldList){
		
		List<com.ericsson.raso.sef.core.Meta> result = 
				new ArrayList<com.ericsson.raso.sef.core.Meta>(); 
		
		for (Meta oldMeta : oldList) {
			result.add(toMeta(oldMeta));
		}
		return result;
	}
}
