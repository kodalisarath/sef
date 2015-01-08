package com.ericsson.raso.sef.notification.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ericsson.raso.sef.catalog.Exception.FrontEndException;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.NotificationMessageDetail;
import com.ericsson.raso.sef.catalog.response.NotificationResult;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ChargeAmount;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent;
import com.ericsson.raso.sef.core.ne.Language;
import com.ericsson.raso.sef.core.ne.NotificationAction;
import com.ericsson.raso.sef.core.ne.NotificationMessage;
import com.ericsson.raso.sef.ne.notification.internal.SmExternalNotificationTemplateCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class NotificationController {
	private SmExternalNotificationTemplateCatalog externalNotificationCatalog = null;

	public NotificationController() {
		try {
			externalNotificationCatalog = new SmExternalNotificationTemplateCatalog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getNotificationWorkflows", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getNotificationWorkflows() throws FrameworkException {

		InputStream reader = null;
		for (URL url : ((URLClassLoader) (Thread.currentThread()
				.getContextClassLoader())).getURLs()) {
			if (url.getPath().contains("notification-engine")) {
				System.out.println("url.getPath()---------------"+url.getPath());
				String inputFile = "jar:file:" + url.getPath()
						+ "!/META-INF/spring/beans.xml";
				URL inputURL = null;
				try {
					inputURL = new URL(inputFile);
					JarURLConnection conn = (JarURLConnection) inputURL
							.openConnection();
					reader = conn.getInputStream();
				} catch (MalformedURLException e1) {
					System.err.println("Malformed input URL: " + inputURL);
				} catch (IOException e1) {
					System.err.println("IO error open connection");
				}
				break;
			}

		}

		List<String> workflowIds = new ArrayList<String>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(reader);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("bean");

			outer: for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element beanElement = (Element) nNode;

					NodeList childNodeList = beanElement.getChildNodes();

					for (int i = 0; i < childNodeList.getLength(); i++) {
						Node childNode = childNodeList.item(i);

						if (childNode.getNodeType() == Node.ELEMENT_NODE
								&& childNode.getNodeName().equals("property")) {

							Element childElement = (Element) childNode;

							if (childElement
									.getAttribute("value")
									.equals("com.ericsson.raso.sef.core.ne.NotificationWorkflowService")
									&& childElement.getAttribute("name")
											.equals("serviceType")) {

								workflowIds.add(beanElement.getAttribute("id"));
								continue outer;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workflowIds;
	}

	// get all notification from external-notification.ccm file
	@RequestMapping(value = "/getAllNotifications", method = RequestMethod.GET)
	public @ResponseBody
	List<NotificationResult> getAllNotifications() throws FrameworkException {
		NotificationResult notificationResult = new NotificationResult();
		String jsonNotificationResponse = null;
		Response response = new Response();
		List<NotificationResult> notificationResultList = new ArrayList<NotificationResult>();
		// Map<String, NotificationResult> notificationMap = null;
		NotificationAction notificationAction = null;
		// StringBuilder builder = new StringBuilder("[");
		Collection<ExternalNotifcationEvent> notifications = externalNotificationCatalog
				.getAllEvents();
		Iterator iterator = notifications.iterator();
		while (iterator.hasNext()) {
			ExternalNotifcationEvent notificationEvent = (ExternalNotifcationEvent) iterator
					.next();
			if (notificationEvent.getEventId() != null) {
				notificationResult = new NotificationResult();
				notificationAction = toAction(notificationEvent.getAction());
				notificationResult.setEventId(notificationEvent.getEventId());
				notificationResult.setDescription(notificationEvent
						.getDescription());
				notificationResult.setWsClientId(notificationEvent
						.getWsClientId());
				notificationResult.setSenderAddr(notificationEvent
						.getSenderAddr());
				notificationResult.setNotificationAction(notificationAction);
				notificationResult.setChargeAmount(notificationEvent
						.getChargeAmount().getAmount());
				notificationResultList.add(notificationResult);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonNotificationResponse = mapper
					.writeValueAsString(notificationResultList);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Notification Event Count :"
					+ notifications.size()
					+ "loaded from external-notification.ccem file.");
			response.setResponseString(jsonNotificationResponse);
		} catch (Exception e) {
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage(e.getMessage());
		}
		return notificationResultList;
	}

	public static NotificationAction toAction(NotificationAction action) {
		switch (action) {
		case ASIS:
			return com.ericsson.raso.sef.core.ne.NotificationAction.ASIS;
		case MASSAGE:
			return com.ericsson.raso.sef.core.ne.NotificationAction.MASSAGE;
		case WORKFLOW:
			return com.ericsson.raso.sef.core.ne.NotificationAction.WORKFLOW;
		}
		return null;
	}

	@RequestMapping(value = "/isNotificationExists", method = RequestMethod.GET)
	public @ResponseBody
	Response checkIfNotificationExists(ModelMap model,
			@RequestParam(value = "eventId", required = true) String eventId) {
		ExternalNotifcationEvent notificationEvent = null;
		Response response = new Response();
		// Right now we don't have exception class implemented,to be done later
		if (eventId == null)
			throw new FrontEndException("The eventId provided was null", "");
		try {
			notificationEvent = externalNotificationCatalog
					.getEventById(eventId);
			if (notificationEvent == null) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Notification [" + eventId
						+ "] can be created.");
			} else {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Duplicate Notification EventId : ["
						+ eventId + "] cannot be created.");
			}
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	@RequestMapping(value = "/createNotification", method = RequestMethod.GET)
	public @ResponseBody
	Response createNotification(
			ModelMap model,
			@RequestParam(value = "notificationEventId", required = true) String notificationEventId,
			@RequestParam(value = "notificationDescription", required = true) String notificationDescription,
			@RequestParam(value = "notificationSenderAddress", required = true) String notificationSenderAddress,
			@RequestParam(value = "chargeAmount", required = true) String chargeAmount,
			@RequestParam(value = "chargeAmountCurrency", required = true) String chargeAmountCurrency,
			@RequestParam(value = "notificationAction", required = true) String notificationAction,
			@RequestParam(value = "wsClientId", required = true) String wsClientId,
			@RequestParam(value = "messages", required = true) String messages) {
		Response response = new Response();
		List<NotificationMessage> messageList = new ArrayList<NotificationMessage>();
		try {
			ExternalNotifcationEvent notificationEvent = new ExternalNotifcationEvent();
			notificationEvent.setEventId(notificationEventId);
			notificationEvent.setDescription(notificationDescription);
			notificationEvent.setSenderAddr(notificationSenderAddress);
			NotificationAction action = NotificationAction
					.valueOf(notificationAction);
			notificationEvent.setAction(toNotificationAction(action));
			// if user won't enable charge amount for event notification
			if (!chargeAmount.equalsIgnoreCase("")) {
				long updatedChargeAmount = Long.parseLong(chargeAmount);
				CurrencyCode currencyCode = CurrencyCode
						.valueOf(chargeAmountCurrency);
				notificationEvent.setChargeAmount(new ChargeAmount(
						updatedChargeAmount, toCurrencyCode(currencyCode)));
			}
			switch (action) {
			case ASIS:
				notificationEvent.setAction(action);
				break;
			case MASSAGE:
				notificationEvent.setAction(action);
				break;
			case WORKFLOW:
				notificationEvent.setAction(action);
				notificationEvent.setWsClientId(wsClientId);
				break;
			}
			// if user won't enter any message (Notification Action -> Workflow)
			if (!messages.equalsIgnoreCase("")) {
				String[] notificationMessagesArray = messages.split("@");
				for (int index = 0; index < notificationMessagesArray.length; index++) {
					NotificationMessage notificationMessage = new NotificationMessage();
					String NotificationMessageString = notificationMessagesArray[index];
					String[] notificationMessageArray = NotificationMessageString
							.split("::");
					notificationMessage.setId(notificationMessageArray[0]);
					notificationMessage.setMessage(notificationMessageArray[1]);
					notificationMessage.setLang(Language
							.valueOf(notificationMessageArray[2]));
					messageList.add(notificationMessage);
				}
				notificationEvent.setMessages(toMessageList(messageList));
			}

			// notificationEvent.setMetas(toMetaList(oldEvent.getMetas()));
			try {
				externalNotificationCatalog.storeEvent(notificationEvent);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Notification Event Id:"
						+ notificationEvent.getEventId() + " "
						+ "has been created in Notification Manager");
			} catch (SmException e) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e.getMessage());
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		}
		return response;
	}

	// delete notification from notification manager
	@RequestMapping(value = "/deleteNotification", method = RequestMethod.GET)
	public @ResponseBody
	Response deleteNotification(ModelMap model,
			@RequestParam(value = "eventId", required = true) String eventId) {
		Response response = new Response();
		try {
			externalNotificationCatalog.remove(eventId);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Notification Event Id:" + eventId + " "
					+ "has been deleted from Notification Manager");
		} catch (SmException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// Fetch notification details based on event id
	@RequestMapping(value = "/getNotificationEventDetails", method = RequestMethod.GET)
	public @ResponseBody
	NotificationResult getNotificationEventDetails(ModelMap model,
			@RequestParam(value = "eventId", required = true) String eventId) {
		NotificationResult notificationDetails = new NotificationResult();
		List<NotificationMessageDetail> detailMessageList = new ArrayList<NotificationMessageDetail>();
		Response notificationResponse = new Response();

		ExternalNotifcationEvent eventDetails = externalNotificationCatalog
				.getEventById(eventId);
		if (eventDetails != null) {
			notificationResponse.setStatus(GuiConstants.SUCCESS);
			notificationResponse.setMessage("Notification Event Id:" + eventId
					+ "has been deleted from Notification Manager");
			notificationDetails.setEventId(eventDetails.getEventId());
			notificationDetails.setDescription(eventDetails.getDescription());
			notificationDetails.setSenderAddr(eventDetails.getSenderAddr());
			long chargeAmount = eventDetails.getChargeAmount().getAmount();
			CurrencyCode currencyCode = eventDetails.getChargeAmount()
					.getCurrencyCode();
			notificationDetails.setChargeAmount(chargeAmount);
			notificationDetails.setCurrencyCode(currencyCode);

			NotificationAction notificationAction = eventDetails.getAction();
			notificationDetails.setNotificationAction(notificationAction);

			// IF Notification action is WORKFLOW, THEN SET WorkflowId in UI
			notificationDetails.setWsClientId(eventDetails.getWsClientId());

			NotificationMessageDetail notificationMessageDetail = null;
			List<NotificationMessage> messageList = eventDetails.getMessages();
			if (!messageList.isEmpty()) {
				for (NotificationMessage message : messageList) {
					notificationMessageDetail = new NotificationMessageDetail();
					notificationMessageDetail.setMessage(message.getMessage());
					notificationMessageDetail.setLanguage(message.getLang());
					detailMessageList.add(notificationMessageDetail);
				}
			}
			notificationDetails.setNotificationMessages(detailMessageList);
		} else {
			notificationResponse.setStatus(GuiConstants.FAILURE);
			notificationResponse.setMessage("Unknown Event Id :" + eventId
					+ "In Notification Manager");
		}
		return notificationDetails;
	}

	@RequestMapping(value = "/updateNotification", method = RequestMethod.GET)
	public @ResponseBody
	Response updateNotification(
			ModelMap model,
			@RequestParam(value = "notificationEventId", required = true) String notificationEventId,
			@RequestParam(value = "notificationDescription", required = true) String notificationDescription,
			@RequestParam(value = "notificationSenderAddress", required = true) String notificationSenderAddress,
			@RequestParam(value = "chargeAmount", required = true) String chargeAmount,
			@RequestParam(value = "chargeAmountCurrency", required = true) String chargeAmountCurrency,
			@RequestParam(value = "notificationAction", required = true) String notificationAction,
			@RequestParam(value = "wsClientId", required = true) String wsClientId,
			@RequestParam(value = "messages", required = true) String messages) {
		Response response = new Response();

		List<NotificationMessage> messageList = new ArrayList<NotificationMessage>();
		try {
			ExternalNotifcationEvent notificationEvent = new ExternalNotifcationEvent();
			notificationEvent.setEventId(notificationEventId);
			notificationEvent.setDescription(notificationDescription);
			notificationEvent.setSenderAddr(notificationSenderAddress);
			NotificationAction action = NotificationAction
					.valueOf(notificationAction);
			notificationEvent.setAction(toNotificationAction(action));
			// if user won't enable charge amount for event notification
			if (!chargeAmount.equalsIgnoreCase("")) {
				long updatedChargeAmount = Long.parseLong(chargeAmount);
				CurrencyCode currencyCode = CurrencyCode
						.valueOf(chargeAmountCurrency);
				notificationEvent.setChargeAmount(new ChargeAmount(
						updatedChargeAmount, toCurrencyCode(currencyCode)));
			}
			switch (action) {
			case ASIS:
				notificationEvent.setAction(action);
				break;
			case MASSAGE:
				notificationEvent.setAction(action);
				break;
			case WORKFLOW:
				notificationEvent.setAction(action);
				notificationEvent.setWsClientId(wsClientId);
				break;
			}
			// if user won't enter any message (Notification Action ->
			// Work-flow)
			if (!messages.equalsIgnoreCase("")) {
				String[] notificationMessagesArray = messages.split("@");
				for (int index = 0; index < notificationMessagesArray.length; index++) {
					NotificationMessage notificationMessage = new NotificationMessage();
					String NotificationMessageString = notificationMessagesArray[index];
					String[] notificationMessageArray = NotificationMessageString
							.split("::");
					notificationMessage.setId(notificationMessageArray[0]);
					notificationMessage.setMessage(notificationMessageArray[1]);
					notificationMessage.setLang(Language
							.valueOf(notificationMessageArray[2]));
					messageList.add(notificationMessage);
				}
				notificationEvent.setMessages(toMessageList(messageList));
			}

			// notificationEvent.setMetas(toMetaList(oldEvent.getMetas()));
			try {
				ExternalNotifcationEvent event = externalNotificationCatalog
						.getEventById(notificationEvent.getEventId());
				if (event != null) {
					// notification exists in ccm file, first remove and the
					// store event
					externalNotificationCatalog.remove(notificationEvent
							.getEventId());
					// store event
					externalNotificationCatalog.storeEvent(notificationEvent);
				} else {
					// notification is not exists in ccm file, store event
					externalNotificationCatalog.storeEvent(notificationEvent);
				}
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Notification Event Id:"
						+ notificationEvent.getEventId() + " "
						+ "has been updated in Notification Manager");
			} catch (SmException e) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e.getMessage());
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		}
		return response;
	}

	public static com.ericsson.raso.sef.core.ne.NotificationAction toNotificationAction(
			NotificationAction action) {
		switch (action) {
		case ASIS:
			return com.ericsson.raso.sef.core.ne.NotificationAction.ASIS;
		case MASSAGE:
			return com.ericsson.raso.sef.core.ne.NotificationAction.MASSAGE;
		case WORKFLOW:
			return com.ericsson.raso.sef.core.ne.NotificationAction.WORKFLOW;
		}
		return null;
	}

	public static com.ericsson.raso.sef.core.db.model.CurrencyCode toCurrencyCode(
			CurrencyCode code) {
		switch (code) {
		case CENTS:
			return com.ericsson.raso.sef.core.db.model.CurrencyCode.CENTS;
		case PHP:
			return com.ericsson.raso.sef.core.db.model.CurrencyCode.PHP;
		}
		return null;
	}

	public static com.ericsson.raso.sef.core.ne.Language toLanguage(
			Language code) {
		switch (code) {
		case bahasa:
			return com.ericsson.raso.sef.core.ne.Language.bahasa;
		case en:
			return com.ericsson.raso.sef.core.ne.Language.en;
		case fil:
			return com.ericsson.raso.sef.core.ne.Language.fil;
		}
		return null;
	}

	public static List<com.ericsson.raso.sef.core.ne.NotificationMessage> toMessageList(
			List<NotificationMessage> messageList) {
		List<com.ericsson.raso.sef.core.ne.NotificationMessage> result = new ArrayList<com.ericsson.raso.sef.core.ne.NotificationMessage>();
		for (NotificationMessage notificationMessage : messageList) {
			com.ericsson.raso.sef.core.ne.NotificationMessage str = toNotificationMessage(notificationMessage);
			result.add(str);
		}
		return result;
	}

	public static com.ericsson.raso.sef.core.ne.NotificationMessage toNotificationMessage(
			NotificationMessage message) {
		com.ericsson.raso.sef.core.ne.NotificationMessage newMessage = new com.ericsson.raso.sef.core.ne.NotificationMessage();
		newMessage.setId(message.getId());
		newMessage.setLang(toLanguage(message.getLang()));
		newMessage.setMessage(message.getMessage());
		return newMessage;
	}
}