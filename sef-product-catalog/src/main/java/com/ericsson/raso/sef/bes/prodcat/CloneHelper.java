package com.ericsson.raso.sef.bes.prodcat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class CloneHelper {
	
	private static Logger logger = LoggerFactory.getLogger(CloneHelper.class);
	
	public static <T> T deepClone(T object) {
		T cloned = null;
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			oos.writeObject(object);
			byte[] serializedObject = baos.toByteArray();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
			ObjectInputStream ois = new ObjectInputStream(bais);
			cloned = (T) ois.readObject();
			
			oos.close();
			baos.close();
			ois.close();
			bais.close();
			
			oos = null;
			baos = null;
			ois = null;
			bais = null;

		} catch (IOException e) {
			//TODO: Logger - write what happened here
			logger.error("IOException while object clone: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			//TODO: Logger - write what happened here
			logger.error("ClassNotFoundError while object clone " + e.getMessage());
		}
		
		logger.debug("Cloned object " + cloned);
		
		return cloned;
	}

}
