package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.DateParser;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.DateSerializer;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.SAXException;

import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcInt;

public class SmTypeFactory extends TypeFactoryImpl {

	public SmTypeFactory(XmlRpcController pController) {
		super(pController);
	}

	private DateFormat newFormat() {
		return new SimpleDateFormat("yyyyMMdd'T'HH:mm:ssZ");
	}

	@Override
	public TypeParser getParser(XmlRpcStreamConfig pConfig, NamespaceContextImpl pContext, String pURI,
			String pLocalName) {
		if (DateSerializer.DATE_TAG.equals(pLocalName)) {
			return new DateParser(newFormat());
		} else {
			return super.getParser(pConfig, pContext, pURI, pLocalName);
		}
	}

	@Override
	public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
		if (pObject instanceof Date) {
			return new DateSerializer(newFormat());
		} else if(pObject instanceof XmlRpcInt) {
			return new IntSerializer();
		} else if(pObject instanceof String) {
			return new StringSerializer();
		} else {
			return super.getSerializer(pConfig, pObject);
		}
	}

}
