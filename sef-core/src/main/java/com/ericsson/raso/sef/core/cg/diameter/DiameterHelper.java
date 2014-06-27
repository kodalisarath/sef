package com.ericsson.raso.sef.core.cg.diameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.core.config.Section;
public class DiameterHelper {

	private static Logger log = LoggerFactory.getLogger(DiameterHelper.class);
	
	private static Map<Integer, DiameterAvpType> map;

	public static Map<Integer, DiameterAvpType> getAvpTypeMap() {
		if (map == null) {
			try {
				//Properties properties = SmCoreContext.getConfig().properties(IConfig.GLOBAL_COMPONENT, "avpTypes");
				Section section = SefCoreServiceResolver.getConfigService()
						.getSection("GLOBAL_avpTypes");
				List<Property> propertyList = SefCoreServiceResolver.getConfigService().getProperties(section);

				map = new HashMap<Integer, DiameterAvpType>();
				for (Property property : propertyList) {
					DiameterAvpType avpType = DiameterAvpType.valueOf(String
							.valueOf(property.getKey()));
					String[] avpCodes = StringUtils
							.commaDelimitedListToStringArray(String
									.valueOf(property.getValue()));
					for (String avpcode : avpCodes) {
						Integer avpCode = Integer.valueOf(avpcode);
						map.put(avpCode, avpType);
					}

				}
			} catch (Exception e) {
				log.error("DiameterHelper AVP Type Excption is  ",e);
			}
		}
		
		//log.debug("AVP Types returned from config files are "+map);
		return map;
	}

}
