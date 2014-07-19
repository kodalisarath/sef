package com.ericsson.raso.sef.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public final class CloneHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloneHelper.class);
	
	private static Cloner cloner = new Cloner();
	
	public static <T> T deepClone(T object) {
		return cloner.deepClone(object);
		/*T cloned = null;
		
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
			LOGGER.error("Unable to clone" + object, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Unable to clone" + object, e);
		}
		catch (Exception e) {
			LOGGER.error("Unable to clone" + object, e);
		}

		return cloned;*/
	}

}
