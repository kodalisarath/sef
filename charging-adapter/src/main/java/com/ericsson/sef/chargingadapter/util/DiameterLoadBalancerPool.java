package com.ericsson.sef.chargingadapter.util;

import java.util.List;

import com.ericsson.pps.diameter.rfcapi.base.PeerConnectionListener;
import com.ericsson.raso.sef.core.lb.Member;
import com.ericsson.raso.sef.core.lb.SefLoadBalancerPool;


public class DiameterLoadBalancerPool extends SefLoadBalancerPool implements PeerConnectionListener {

	public DiameterLoadBalancerPool(List<Member> routes) {
		super(routes);
	}
	
	@Override
	public void peerAdded(String uri, boolean flag) {
		logger.info("Peer Added: " + uri + " " + flag);
		addToPool(uri);
	}

	@Override
	public void peerConnected(String uri) {
		logger.info("Peer connected :: " + uri);
		//addToPool(uri);
	}

	@Override
	public void peerDisconnected(String uri,  int reason, int dprCause) {
		logger.info("Peer disconnected :: " + uri + " || Reason : " + reason + " || DPR Cause " + dprCause);
		//removeFromPool(uri);
	}
	
	@Override
	public void peerRemoved(String uri) {
		logger.info("Peer Removed :: " + uri);
		removeFromPool(uri);
	}

}
