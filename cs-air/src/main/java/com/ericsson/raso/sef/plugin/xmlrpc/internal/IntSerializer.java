package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import org.apache.xmlrpc.serializer.TypeSerializer;
import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/** A {@link TypeSerializer} for int <int> values.
 */
public class IntSerializer extends TypeSerializerImpl {
    public static final String INT_TAG = "int";

	public void write(ContentHandler pHandler, Object pObject) throws SAXException {
        write(pHandler, INT_TAG, pObject.toString());
	}
}
