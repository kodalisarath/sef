package com.ericsson.raso.sef.ne.core.smpp.internal;

import java.nio.channels.ClosedChannelException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.EnquireLinkResp;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.ericsson.raso.sef.ne.core.config.SourceSmppAddress;
import com.ericsson.raso.sef.ne.core.config.TargetSmppAddress;
import com.ericsson.raso.sef.ne.core.smpp.SmppClient;

public class CloudhopperSmppClient implements SmppClient {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT = 160;
	private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2 = 134;

	private SmppSessionConfiguration configuration;

	private SmppSession session;
	private DefaultSmppClient clientBootstrap;

	private SourceSmppAddress source;
	private TargetSmppAddress destination;

	private ExecutorService executor;
	private ScheduledThreadPoolExecutor monitorExecutor;

	private SmppSessionHandler smppSessionHandler;

	private EnquireLinkThread enquireLinkThread;

	public CloudhopperSmppClient(SmppSessionConfiguration configuration, SourceSmppAddress source,
			TargetSmppAddress destination) {
		this.configuration = configuration;
		this.source = source;
		this.destination = destination;
	}

	public void setSmppSessionHandler(SmppSessionHandler smppSessionHandler) {
		this.smppSessionHandler = smppSessionHandler;
	}

	public void start() throws Exception {
		try {
			executor = Executors.newCachedThreadPool();
			monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
				private AtomicInteger sequence = new AtomicInteger(0);

				
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
					return t;
				}
			});

			clientBootstrap = new DefaultSmppClient(executor, 1, monitorExecutor);
			if (smppSessionHandler == null) {
				smppSessionHandler = new DefaultSmppSessionHandler();
			}
			session = clientBootstrap.bind(configuration, smppSessionHandler);

			enquireLinkThread = new EnquireLinkThread();
			enquireLinkThread.start();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	private class EnquireLinkThread extends Thread {
		private volatile boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				try {
					EnquireLinkResp enquireLinkResp = CloudhopperSmppClient.this.session.enquireLink(new EnquireLink(),
							CloudhopperSmppClient.this.configuration.getConnectTimeout());
					log.info("enquire_link_resp #1: commandStatus [" + enquireLinkResp.getCommandStatus() + "="
							+ enquireLinkResp.getResultMessage() + "]");
					Thread.sleep(configuration.getConnectTimeout());
				} catch (Exception e) {
					if (!session.isBound() || session.isClosed() || e instanceof ClosedChannelException || e instanceof SmppChannelException) {
						try {
							session = clientBootstrap.bind(configuration, smppSessionHandler);
						} catch (Exception e1) {
							log.error(e.getMessage(), e);
						}
					}
					log.error(e.getMessage(), e);
				}
			}
		}

		public void stopEnquiry() {
			this.stop = true;
		}
	}

	public void destroy() {
		enquireLinkThread.stopEnquiry();
		session.unbind(15000);
		session.destroy();
		clientBootstrap.destroy();
		executor.shutdown();
		monitorExecutor.shutdown();
	}

	
	public void sendMessage(String msisdn, String message, String senderAddr) throws Exception {
		log.debug("Sending message to msisdn:" + msisdn + " message:" + message + " with senderAddr:" + senderAddr);
		if (message.length() <= MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT) {
			sendShortMessage(msisdn, message, senderAddr);
		} else {
			sendLongMessage(msisdn, message, senderAddr);
		}
		log.debug("Message sent to msisdn:" + msisdn + " message:" + message + " with senderAddr:" + senderAddr);
	}

	private void sendLongMessage(String msisdn, String message, String senderAddr) throws Exception {
		byte sourceTon = (byte)0x03;
		if(senderAddr != null && senderAddr.length() > 0) {
			sourceTon = (byte)0x05;
		}
		
		byte[] textBytes = CharsetUtil.encode(message, CharsetUtil.CHARSET_ISO_8859_15);

		int maximumMultipartMessageSegmentSize = MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2;
		byte[] byteSingleMessage = textBytes;
		byte[][] byteMessagesArray = splitUnicodeMessage(byteSingleMessage, maximumMultipartMessageSegmentSize);
		// submit all messages
		for (int i = 0; i < byteMessagesArray.length; i++) {
			SubmitSm submit0 = new SubmitSm();
			submit0.setEsmClass(SmppConstants.ESM_CLASS_UDHI_MASK);
			submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
			submit0.setSourceAddress(new Address(sourceTon, (byte)0x00, senderAddr));
			submit0.setDestAddress(new Address(destination.getTon(), destination.getNpi(), msisdn));
			submit0.setShortMessage(byteMessagesArray[i]);
			session.submit(submit0, 10000);
		}
	}

	private void sendShortMessage(String msisdn, String message, String senderAddr) throws Exception {
		byte sourceTon = (byte)0x03;
		if(senderAddr != null && senderAddr.length() > 0) {
			sourceTon = (byte)0x05;
		}
		
		byte[] textBytes = CharsetUtil.encode(message, CharsetUtil.CHARSET_ISO_8859_1);
		SubmitSm submit0 = new SubmitSm();

		submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
		submit0.setSourceAddress(new Address(sourceTon, (byte)0x00, senderAddr));
		submit0.setDestAddress(new Address(destination.getTon(), destination.getNpi(), msisdn));
		submit0.setShortMessage(textBytes);
		session.submit(submit0, configuration.getConnectTimeout());
	}

	private static byte[][] splitUnicodeMessage(byte[] aMessage, Integer maximumMultipartMessageSegmentSize) {
		final byte UDHIE_HEADER_LENGTH = 0x05;
		final byte UDHIE_IDENTIFIER_SAR = 0x00;
		final byte UDHIE_SAR_LENGTH = 0x03;

		// determine how many messages have to be sent
		int numberOfSegments = aMessage.length / maximumMultipartMessageSegmentSize;
		int messageLength = aMessage.length;
		if (numberOfSegments > 255) {
			numberOfSegments = 255;
			messageLength = numberOfSegments * maximumMultipartMessageSegmentSize;
		}
		if ((messageLength % maximumMultipartMessageSegmentSize) > 0) {
			numberOfSegments++;
		}

		// prepare array for all of the msg segments
		byte[][] segments = new byte[numberOfSegments][];

		int lengthOfData;

		// generate new reference number
		byte[] referenceNumber = new byte[1];
		new Random().nextBytes(referenceNumber);

		// split the message adding required headers
		for (int i = 0; i < numberOfSegments; i++) {
			if (numberOfSegments - i == 1) {
				lengthOfData = messageLength - i * maximumMultipartMessageSegmentSize;
			} else {
				lengthOfData = maximumMultipartMessageSegmentSize;
			}

			// new array to store the header
			segments[i] = new byte[6 + lengthOfData];

			// UDH header
			// doesn't include itself, its header length
			segments[i][0] = UDHIE_HEADER_LENGTH;
			// SAR identifier
			segments[i][1] = UDHIE_IDENTIFIER_SAR;
			// SAR length
			segments[i][2] = UDHIE_SAR_LENGTH;
			// reference number (same for all messages)
			segments[i][3] = referenceNumber[0];
			// total number of segments
			segments[i][4] = (byte) numberOfSegments;
			// segment number
			segments[i][5] = (byte) (i + 1);

			// copy the data into the array
			System.arraycopy(aMessage, (i * maximumMultipartMessageSegmentSize), segments[i], 6, lengthOfData);

		}
		return segments;
	}

}
