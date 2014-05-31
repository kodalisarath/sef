package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class StringSerializer extends TypeSerializerImpl {

	public static final String STRING_TAG = "string";
	
	@Override
	public void write(ContentHandler pHandler, Object pObject)
			throws SAXException {
		write(pHandler, STRING_TAG, pObject.toString());
	}

}
