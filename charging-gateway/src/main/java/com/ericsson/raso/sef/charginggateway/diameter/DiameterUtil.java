package com.ericsson.raso.sef.charginggateway.diameter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.EventTimestampAvp;


public class DiameterUtil {

	private static Logger logger = LoggerFactory.getLogger(DiameterUtil.class);
	public static Map<String, Object> toMap(Collection<Avp> avps) {
		try {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if (avps == null) {
				return map;
			}
			Map<Integer, DiameterAvpType> avpTypeMap = DiameterHelper.getAvpTypeMap();
			for (Avp avp : avps) {
				Collection<Avp> list = null;
				try {
					list = avp.getDataAsGroup();
				} catch (Exception e) {
					
					//logger.error("Exception Captured at DiameterUtil avp.getDataAsGroup() ", e);

				}
				if (list == null) {
					DiameterAvpType avpType = avpTypeMap.get(avp.getAvpCode());
					if (avpType == null) {
						//map.put(String.valueOf(avp.getAvpCode()), new String(avp.getData()));
					} else if (avpType.name().equals("OCTETSTRING")) {
						map.put(String.valueOf(avp.getAvpCode()), new String(avp.getData()));
					} else if (avpType.name().equals("INTEGER32")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsInt()));
					} else if (avpType.name().equals("UNSIGNED32")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsInt()));
					} else if (avpType.name().equals("UNSIGNED64")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsLong()));
					} else if (avpType.name().equals("FLOAT32")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsFloat()));
					} else if (avpType.name().equals("FLOAT64")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsFloat()));
					} else if (avpType.name().equals("UTF8STRING")) {
						map.put(String.valueOf(avp.getAvpCode()), avp.getAsUTF8String().trim());
					} else if (avpType.name().equals("INTEGER64")) {
						map.put(String.valueOf(avp.getAvpCode()), String.valueOf(avp.getAsInt()));
					} else if (avpType.name().equals("TIME_STAMP")) {
						map.put(String.valueOf(avp.getAvpCode()), new EventTimestampAvp(avp).getValue().toString());
					}
				} else {
						map.put(String.valueOf(avp.getAvpCode()), toMap(list));
				}
			}
			//logger.error("Map Returned from DiameterUtil ", map);
			return map;

		} catch (Exception e) {
			//logger.error("Generic Exception Captured at DiameterUtil ", e);
		}

		return null;
	}

}
