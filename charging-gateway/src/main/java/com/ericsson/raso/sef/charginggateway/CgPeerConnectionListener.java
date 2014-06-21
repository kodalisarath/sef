package com.ericsson.raso.sef.charginggateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.rfcapi.base.PeerConnectionListener;

public class CgPeerConnectionListener implements PeerConnectionListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void peerAdded(String arg0, boolean arg1) {
		log.info("Added: " +  arg0 + " " + arg1);
	}

	@Override
	public void peerConnected(String arg0) {
		log.info("Connected: " +  arg0 );
	}

	@Override
	public void peerDisconnected(String arg0, int arg1, int arg2) {
		log.info("Disconnected: " +  arg0 + " " + arg1);
	}

	@Override
	public void peerRemoved(String arg0) {
		log.info("Removed: " +  arg0 );
	}
}
