package com.ericsson.raso.sef.auth.integration;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.policy.SPConstants;
import org.apache.ws.security.WSConstants;
import org.w3c.dom.Element;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class WSSByPassInterceptor extends AbstractSoapInterceptor {
	
	
	 private static final Set<QName> HEADERS = new HashSet<QName>();
	    static {
	        HEADERS.add(new QName(WSConstants.WSSE_NS, "Security"));
	        HEADERS.add(new QName(WSConstants.WSSE11_NS, "Security"));
	        HEADERS.add(new QName(WSConstants.WSU_NS, "UsernameToken"));
	    }
	    
	public WSSByPassInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}
	
	public WSSByPassInterceptor(String p) {
		super(p);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		for (Header header : message.getHeaders()) {
			
            if (header instanceof SoapHeader && header.getName().getLocalPart().equals("Security") && (header.getName().getNamespaceURI().equals(WSConstants.WSSE_NS) 
                    || header.getName().getNamespaceURI().equals(WSConstants.WSSE11_NS))) {
            	processUsernameToken((SoapHeader)header);
            	
            	System.out.println("Must understand!!!");
            	if(((SoapHeader)header).isMustUnderstand())
            	((SoapHeader)header).setMustUnderstand(false);
            }
            
		}
	}
	
	@Override
	public Set<QName> getUnderstoodHeaders() {
		return HEADERS;
	}
	
	private void processUsernameToken(SoapHeader h) {
        
		Actor actor = null;
        Element el = (Element)h.getObject();
        Element child = DOMUtils.getFirstElement(el);
        while (child != null) {
            if (SPConstants.USERNAME_TOKEN.equals(child.getLocalName())) {
                try  {
                	 org.apache.ws.security.message.token.UsernameToken ut = 
                	            new org.apache.ws.security.message.token.UsernameToken(child);
                	 if(authenticate(ut.getName(), ut.getPassword(), ut.getNonce())) {
                		 actor = enrichSecurityContext(ut.getName());
                	 }
                	 
                	 if(actor == null) throw new SecurityException();
                	
                	 RequestContext ctx = new RequestContext();
                	 ctx.setActor(actor);
                	 RequestContextLocalStore.put(ctx);
                	
                } catch (Exception ex) {
                    throw new SecurityException();
                }
            }
            child = DOMUtils.getNextElement(child);
        }
    }
	
	
	 private boolean authenticate(String name, String password, String nonce) {
		 //TODO: wire authetication logic
		 IUserStore userStore = SefCoreServiceResolver.getUserStore();
		 
		 return true;
	 }
	 
	 private Actor enrichSecurityContext(String name) {
		 
		 //TODO: Wire authorizaton logic and remove this mock code
		 
		 
		 return null;
	 }

}
