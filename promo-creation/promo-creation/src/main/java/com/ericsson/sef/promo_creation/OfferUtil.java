package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;

/**
 * Hello world!
 *
 */
public class OfferUtil 
{
	
	private static final Logger logger = LoggerFactory.getLogger(OfferUtil.class);
	
	private OfferManager offerManager = null;
	
	ArrayList<Resource> allResources = new ArrayList<Resource>();
	
	public OfferUtil() throws Exception {
		
		offerManager = new OfferManager();
		
	};

	public void addResource(ArrayList<Resource> list, Resource resource) {
		list.add(resource);
		allResources.add(resource);
	}

	public void createAllProductCatalog() throws Exception {
		Map<String, Resource>   lookupResourcesByName = new HashMap<String,Resource>();
		PromoHelper helper = new PromoHelper();

		// ----------------------------------------------------------
		// --- creating all the resources
		// ----------------------------------------------------------

		lookupResourcesByName.put("TimerOffer_1054", helper.createTimerProfile("TimerOffer_1054",1054));
		lookupResourcesByName.put("TimerOffer_1003", helper.createTimerProfile("TimerOffer_1003",1003));
		lookupResourcesByName.put("GaanAllInOne15DedicatedAccount_54", helper.createDA ("GaanAllInOne15DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne15DedicatedAccount_3", helper.createDA ("GaanAllInOne15DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne15Refill_1035", helper.createRefill("GaanAllInOne15Refill_1035", "1035", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("EnhancedGA15RegionalDedicatedAccount_54", helper.createDA ("EnhancedGA15RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("EnhancedGA15RegionalDedicatedAccount_3", helper.createDA ("EnhancedGA15RegionalDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("EnhancedGA15RegionalRefill_1059", helper.createRefill("EnhancedGA15RegionalRefill_1059", "1059", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("GaanAllInOne20DedicatedAccount_54", helper.createDA ("GaanAllInOne20DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne20DedicatedAccount_3", helper.createDA ("GaanAllInOne20DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne20Refill_1036", helper.createRefill("GaanAllInOne20Refill_1036", "1036", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("EnhancedGA20RegionalDedicatedAccount_54", helper.createDA ("EnhancedGA20RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("EnhancedGA20RegionalDedicatedAccount_3", helper.createDA ("EnhancedGA20RegionalDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("EnhancedGA20RegionalRefill_1060", helper.createRefill("EnhancedGA20RegionalRefill_1060", "1060", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("GaanAllInOne15RegionalDedicatedAccount_54", helper.createDA ("GaanAllInOne15RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne15RegionalDedicatedAccount_3", helper.createDA ("GaanAllInOne15RegionalDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne15RegionalRefill_1037", helper.createRefill("GaanAllInOne15RegionalRefill_1037", "1037", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("GaanAllInOne20RegionalDedicatedAccount_54", helper.createDA ("GaanAllInOne20RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne20RegionalDedicatedAccount_3", helper.createDA ("GaanAllInOne20RegionalDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne20RegionalRefill_1038", helper.createRefill("GaanAllInOne20RegionalRefill_1038", "1038", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("TimerOffer_1052", helper.createTimerProfile("TimerOffer_1052",1052));
		lookupResourcesByName.put("TokenGaanAllInOne55SMSDedicatedAccount_54", helper.createDA ("TokenGaanAllInOne55SMSDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("TokenGaanAllInOne55SMSDedicatedAccount_52", helper.createDA ("TokenGaanAllInOne55SMSDedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("TokenGaanAllInOne55SMSDedicatedAccount_3", helper.createDA ("TokenGaanAllInOne55SMSDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("TokenGaanAllInOne55SMSRefill_1061", helper.createRefill("TokenGaanAllInOne55SMSRefill_1061", "1061", 1, "1", CurrencyCode.PHP, new String [] {"1054","1052","1003"}));
		lookupResourcesByName.put("TokenGaanAllInOne55VoiceDedicatedAccount_54", helper.createDA ("TokenGaanAllInOne55VoiceDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("TokenGaanAllInOne55VoiceDedicatedAccount_3", helper.createDA ("TokenGaanAllInOne55VoiceDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("TokenGaanAllInOne55VoiceRefill_1062", helper.createRefill("TokenGaanAllInOne55VoiceRefill_1062", "1062", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("TimerOffer_2101", helper.createTimerProfile("TimerOffer_2101",2101));
		lookupResourcesByName.put("TokenGaanUnliTxtPlus55VoiceDedicatedAccount_3", helper.createDA ("TokenGaanUnliTxtPlus55VoiceDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("Pam_2", helper.createPAM("Pam_2",1, 2));
		lookupResourcesByName.put("TokenGaanUnliTxtPlus55VoiceRefill_1063", helper.createRefill("TokenGaanUnliTxtPlus55VoiceRefill_1063", "1063", 1, "1", CurrencyCode.PHP, new String [] {"2101","1003"}));
		lookupResourcesByName.put("GaanAllInOne30DedicatedAccount_54", helper.createDA ("GaanAllInOne30DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne30DedicatedAccount_3", helper.createDA ("GaanAllInOne30DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne30Refill_1064", helper.createRefill("GaanAllInOne30Refill_1064", "1064", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("GaanAllInOne55DedicatedAccount_54", helper.createDA ("GaanAllInOne55DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne55DedicatedAccount_3", helper.createDA ("GaanAllInOne55DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne55Refill_1065", helper.createRefill("GaanAllInOne55Refill_1065", "1065", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("GaanAllInOne99DedicatedAccount_54", helper.createDA ("GaanAllInOne99DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne99DedicatedAccount_3", helper.createDA ("GaanAllInOne99DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanAllInOne99Refill_1066", helper.createRefill("GaanAllInOne99Refill_1066", "1066", 1, "1", CurrencyCode.PHP, new String [] {"1054","1003"}));
		lookupResourcesByName.put("SangkatuTok15DedicatedAccount_3", helper.createDA ("SangkatuTok15DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("SangkatuTok15Refill_1039", helper.createRefill("SangkatuTok15Refill_1039", "1039", 1, "1", CurrencyCode.PHP, new String [] {"1003"}));
		lookupResourcesByName.put("SangkatuTok30DedicatedAccount_3", helper.createDA ("SangkatuTok30DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("SangkatuTok30Refill_1040", helper.createRefill("SangkatuTok30Refill_1040", "1040", 1, "1", CurrencyCode.PHP, new String [] {"1003"}));
		lookupResourcesByName.put("BonggaTxt5DedicatedAccount_54", helper.createDA ("BonggaTxt5DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("BonggaTxt5DedicatedAccount_52", helper.createDA ("BonggaTxt5DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("BonggaTxt5Refill_1033", helper.createRefill("BonggaTxt5Refill_1033", "1033", 1, "1", CurrencyCode.PHP, new String [] {"1052","1054"}));
		lookupResourcesByName.put("TimerOffer_2001", helper.createTimerProfile("TimerOffer_2001",2001));
		lookupResourcesByName.put("TimerOffer_2103", helper.createTimerProfile("TimerOffer_2103",2103));
		lookupResourcesByName.put("UnliTxtAllUnliTropaCall20Refill_1023", helper.createRefill("UnliTxtAllUnliTropaCall20Refill_1023", "1023", 1, "1", CurrencyCode.PHP, new String [] {"2001","2103"}));
		lookupResourcesByName.put("UnliCombo10RegionalRefill_1027", helper.createRefill("UnliCombo10RegionalRefill_1027", "1027", 1, "1", CurrencyCode.PHP, new String [] {"2001","2101"}));
		lookupResourcesByName.put("RegionalCombo10DedicatedAccount_3", helper.createDA ("RegionalCombo10DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("RegionalCombo10DedicatedAccount_54", helper.createDA ("RegionalCombo10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("RegionalCombo10Refill_1052", helper.createRefill("RegionalCombo10Refill_1052", "1052", 1, "1", CurrencyCode.PHP, new String [] {"1003","1054","2101"}));
		lookupResourcesByName.put("UnliSingkoRegionalRefill_1028", helper.createRefill("UnliSingkoRegionalRefill_1028", "1028", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxtPlus10DedicatedAccount_54", helper.createDA ("UnliTxtPlus10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("UnliTxtPlus10Refill_1034", helper.createRefill("UnliTxtPlus10Refill_1034", "1034", 1, "1", CurrencyCode.PHP, new String [] {"1054","2101"}));
		lookupResourcesByName.put("UnliTxt15RegionalRefill_1014", helper.createRefill("UnliTxt15RegionalRefill_1014", "1014", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxt5For12HrsRegionalRefill_1015", helper.createRefill("UnliTxt5For12HrsRegionalRefill_1015", "1015", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxt20RegionalRefill_1016", helper.createRefill("UnliTxt20RegionalRefill_1016", "1016", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("TimerOffer_2102", helper.createTimerProfile("TimerOffer_2102",2102));
		lookupResourcesByName.put("UnliTxtTrioCombo10DedicatedAccount_54", helper.createDA ("UnliTxtTrioCombo10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("UnliTxtTrioCombo10Refill_1055", helper.createRefill("UnliTxtTrioCombo10Refill_1055", "1055", 1, "1", CurrencyCode.PHP, new String [] {"1054","2102"}));
		lookupResourcesByName.put("UnliTxtTrioCombo15DedicatedAccount_3", helper.createDA ("UnliTxtTrioCombo15DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("UnliTxtTrioCombo15Refill_1056", helper.createRefill("UnliTxtTrioCombo15Refill_1056", "1056", 1, "1", CurrencyCode.PHP, new String [] {"1003","2102"}));
		lookupResourcesByName.put("UnliTxtTrioCombo20DedicatedAccount_3", helper.createDA ("UnliTxtTrioCombo20DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("UnliTxtTrioCombo20Refill_1057", helper.createRefill("UnliTxtTrioCombo20Refill_1057", "1057", 1, "1", CurrencyCode.PHP, new String [] {"1003","2102"}));
		lookupResourcesByName.put("UnliTxtTrioCombo30DedicatedAccount_3", helper.createDA ("UnliTxtTrioCombo30DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("UnliTxtTrioCombo30Refill_1058", helper.createRefill("UnliTxtTrioCombo30Refill_1058", "1058", 1, "1", CurrencyCode.PHP, new String [] {"1003","2102"}));
		lookupResourcesByName.put("TimerOffer_1101", helper.createTimerProfile("TimerOffer_1101",1101));
		lookupResourcesByName.put("PaTokOTex10DedicatedAccount_101", helper.createDA ("PaTokOTex10DedicatedAccount_101", 101, "1", "PHP"));
		lookupResourcesByName.put("PaTokOTex10Refill_1019", helper.createRefill("PaTokOTex10Refill_1019", "1019", 1, "1", CurrencyCode.PHP, new String [] {"1101"}));
		lookupResourcesByName.put("PaTokOTex15DedicatedAccount_101", helper.createDA ("PaTokOTex15DedicatedAccount_101", 101, "1", "PHP"));
		lookupResourcesByName.put("PaTokOTex15Refill_1020", helper.createRefill("PaTokOTex15Refill_1020", "1020", 1, "1", CurrencyCode.PHP, new String [] {"1101"}));
		lookupResourcesByName.put("PaTokOTex20DedicatedAccount_101", helper.createDA ("PaTokOTex20DedicatedAccount_101", 101, "1", "PHP"));
		lookupResourcesByName.put("PaTokOTex20Refill_1021", helper.createRefill("PaTokOTex20Refill_1021", "1021", 1, "1", CurrencyCode.PHP, new String [] {"1101"}));
		lookupResourcesByName.put("PaTokOTex30DedicatedAccount_101", helper.createDA ("PaTokOTex30DedicatedAccount_101", 101, "1", "PHP"));
		lookupResourcesByName.put("PaTokOTex30Refill_1022", helper.createRefill("PaTokOTex30Refill_1022", "1022", 1, "1", CurrencyCode.PHP, new String [] {"1101"}));
		lookupResourcesByName.put("TimerOffer_1001", helper.createTimerProfile("TimerOffer_1001",1001));
		lookupResourcesByName.put("EmergencyLoadSMSDedicatedAccount_1", helper.createDA ("EmergencyLoadSMSDedicatedAccount_1", 1, "400", "PHP"));
		lookupResourcesByName.put("EmergencyLoadSMSDedicatedAccount_52", helper.createDA ("EmergencyLoadSMSDedicatedAccount_52", 52, "400", "PHP"));
		lookupResourcesByName.put("EmergencyLoadSMSRefill_EL01", helper.createRefill("EmergencyLoadSMSRefill_EL01", "EL01", 1, "400", CurrencyCode.PHP, new String [] {"1001","1052"}));
		lookupResourcesByName.put("EmergencyLoadVoiceDedicatedAccount_1", helper.createDA ("EmergencyLoadVoiceDedicatedAccount_1", 1, "650", "PHP"));
		lookupResourcesByName.put("EmergencyLoadVoiceDedicatedAccount_3", helper.createDA ("EmergencyLoadVoiceDedicatedAccount_3", 3, "650", "PHP"));
		lookupResourcesByName.put("EmergencyLoadVoiceRefill_EL01", helper.createRefill("EmergencyLoadVoiceRefill_EL01", "EL01", 1, "650", CurrencyCode.PHP, new String [] {"1001","1052"}));
		lookupResourcesByName.put("EmergencyLoadSMSAllNetSMSDedicatedAccount_1", helper.createDA ("EmergencyLoadSMSAllNetSMSDedicatedAccount_1", 1, "500", "PHP"));
		lookupResourcesByName.put("EmergencyLoadSMSAllNetSMSDedicatedAccount_54", helper.createDA ("EmergencyLoadSMSAllNetSMSDedicatedAccount_54", 54, "500", "PHP"));
		lookupResourcesByName.put("EmergencyLoadSMSAllNetSMSRefill_EL01", helper.createRefill("EmergencyLoadSMSAllNetSMSRefill_EL01", "EL01", 1, "500", CurrencyCode.PHP, new String [] {"1001","1052"}));
		lookupResourcesByName.put("CorporateQuickLoad100DedicatedAccount_1", helper.createDA ("CorporateQuickLoad100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad100DedicatedAccount_54", helper.createDA ("CorporateQuickLoad100DedicatedAccount_54", 54, "10000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad100Refill_CQ01", helper.createRefill("CorporateQuickLoad100Refill_CQ01", "CQ01", 1, "10000", CurrencyCode.PHP, new String [] {"1001","1054"}));
		lookupResourcesByName.put("CorporateQuickLoad200DedicatedAccount_1", helper.createDA ("CorporateQuickLoad200DedicatedAccount_1", 1, "20000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad200DedicatedAccount_54", helper.createDA ("CorporateQuickLoad200DedicatedAccount_54", 54, "20000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad200Refill_CQ01", helper.createRefill("CorporateQuickLoad200Refill_CQ01", "CQ01", 1, "20000", CurrencyCode.PHP, new String [] {"1001","1054"}));
		lookupResourcesByName.put("CorporateQuickLoad300DedicatedAccount_1", helper.createDA ("CorporateQuickLoad300DedicatedAccount_1", 1, "30000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad300DedicatedAccount_54", helper.createDA ("CorporateQuickLoad300DedicatedAccount_54", 54, "30000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad300Refill_CQ01", helper.createRefill("CorporateQuickLoad300Refill_CQ01", "CQ01", 1, "30000", CurrencyCode.PHP, new String [] {"1001","1054"}));
		lookupResourcesByName.put("CorporateQuickLoad500DedicatedAccount_1", helper.createDA ("CorporateQuickLoad500DedicatedAccount_1", 1, "50000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad500DedicatedAccount_54", helper.createDA ("CorporateQuickLoad500DedicatedAccount_54", 54, "50000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad500Refill_CQ01", helper.createRefill("CorporateQuickLoad500Refill_CQ01", "CQ01", 1, "50000", CurrencyCode.PHP, new String [] {"1001","1054"}));
		lookupResourcesByName.put("CorporateQuickLoad1000DedicatedAccount_1", helper.createDA ("CorporateQuickLoad1000DedicatedAccount_1", 1, "100000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad1000DedicatedAccount_54", helper.createDA ("CorporateQuickLoad1000DedicatedAccount_54", 54, "100000", "PHP"));
		lookupResourcesByName.put("CorporateQuickLoad1000Refill_CQ01", helper.createRefill("CorporateQuickLoad1000Refill_CQ01", "CQ01", 1, "100000", CurrencyCode.PHP, new String [] {"1001","1054"}));
		lookupResourcesByName.put("PresyongPinoyLoad30DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad30DedicatedAccount_1", 1, "3000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad30Refill_PP01", helper.createRefill("PresyongPinoyLoad30Refill_PP01", "PP01", 1, "3000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad60DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad60DedicatedAccount_1", 1, "6000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad60Refill_PP01", helper.createRefill("PresyongPinoyLoad60Refill_PP01", "PP01", 1, "6000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad115DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad115DedicatedAccount_1", 1, "11500", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad115Refill_PP01", helper.createRefill("PresyongPinoyLoad115Refill_PP01", "PP01", 1, "11500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad300DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad300DedicatedAccount_1", 1, "30000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad300DedicatedAccount_54", helper.createDA ("PresyongPinoyLoad300DedicatedAccount_54", 54, "30000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad300Refill_PP01", helper.createRefill("PresyongPinoyLoad300Refill_PP01", "PP01", 1, "30000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad500DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad500DedicatedAccount_1", 1, "50000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad500DedicatedAccount_54", helper.createDA ("PresyongPinoyLoad500DedicatedAccount_54", 54, "50000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad500Refill_PP01", helper.createRefill("PresyongPinoyLoad500Refill_PP01", "PP01", 1, "50000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad1000DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad1000DedicatedAccount_1", 1, "100000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad1000DedicatedAccount_54", helper.createDA ("PresyongPinoyLoad1000DedicatedAccount_54", 54, "100000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad1000Refill_PP01", helper.createRefill("PresyongPinoyLoad1000Refill_PP01", "PP01", 1, "100000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad200DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad200DedicatedAccount_1", 1, "20000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad200DedicatedAccount_54", helper.createDA ("PresyongPinoyLoad200DedicatedAccount_54", 54, "20000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad200Refill_PP01", helper.createRefill("PresyongPinoyLoad200Refill_PP01", "PP01", 1, "20000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PresyongPinoyLoad100DedicatedAccount_1", helper.createDA ("PresyongPinoyLoad100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("PresyongPinoyLoad100Refill_PP01", helper.createRefill("PresyongPinoyLoad100Refill_PP01", "PP01", 1, "10000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("TimerOffer_1012", helper.createTimerProfile("TimerOffer_1012",1012));
		lookupResourcesByName.put("TokTok15DedicatedAccount_12", helper.createDA ("TokTok15DedicatedAccount_12", 12, "1", "PHP"));
		lookupResourcesByName.put("TokTok15Refill_1042", helper.createRefill("TokTok15Refill_1042", "1042", 1, "1", CurrencyCode.PHP, new String [] {"1012"}));
		lookupResourcesByName.put("TokTok10DedicatedAccount_12", helper.createDA ("TokTok10DedicatedAccount_12", 12, "1", "PHP"));
		lookupResourcesByName.put("TokTok10Refill_1043", helper.createRefill("TokTok10Refill_1043", "1043", 1, "1", CurrencyCode.PHP, new String [] {"1012"}));
		lookupResourcesByName.put("SangkatuTex30DedicatedAccount_52", helper.createDA ("SangkatuTex30DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("SangkatuTex30Refill_1026", helper.createRefill("SangkatuTex30Refill_1026", "1026", 1, "1", CurrencyCode.PHP, new String [] {"1052"}));
		lookupResourcesByName.put("SangkatuTex15DedicatedAccount_52", helper.createDA ("SangkatuTex15DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("SangkatuTex15Refill_1025", helper.createRefill("SangkatuTex15Refill_1025", "1025", 1, "1", CurrencyCode.PHP, new String [] {"1052"}));
		lookupResourcesByName.put("Takatak3DedicatedAccount_1", helper.createDA ("Takatak3DedicatedAccount_1", 1, "300", "PHP"));
		lookupResourcesByName.put("Takatak3Refill_TK01", helper.createRefill("Takatak3Refill_TK01", "TK01", 1, "300", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("Takatak6DedicatedAccount_1", helper.createDA ("Takatak6DedicatedAccount_1", 1, "600", "PHP"));
		lookupResourcesByName.put("Takatak6Refill_TK01", helper.createRefill("Takatak6Refill_TK01", "TK01", 1, "600", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("Takatak12DedicatedAccount_1", helper.createDA ("Takatak12DedicatedAccount_1", 1, "1200", "PHP"));
		lookupResourcesByName.put("Takatak12Refill_TK01", helper.createRefill("Takatak12Refill_TK01", "TK01", 1, "1200", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("SMARTLoad500DedicatedAccount_54", helper.createDA ("SMARTLoad500DedicatedAccount_54", 54, "50000", "PHP"));
		lookupResourcesByName.put("SMARTLoad500DedicatedAccount_1", helper.createDA ("SMARTLoad500DedicatedAccount_1", 1, "50000", "PHP"));
		lookupResourcesByName.put("SMARTLoad500Refill_L001", helper.createRefill("SMARTLoad500Refill_L001", "L001", 1, "50000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad1000DedicatedAccount_54", helper.createDA ("SMARTLoad1000DedicatedAccount_54", 54, "100000", "PHP"));
		lookupResourcesByName.put("SMARTLoad1000DedicatedAccount_1", helper.createDA ("SMARTLoad1000DedicatedAccount_1", 1, "100000", "PHP"));
		lookupResourcesByName.put("SMARTLoad1000Refill_L001", helper.createRefill("SMARTLoad1000Refill_L001", "L001", 1, "100000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad200DedicatedAccount_54", helper.createDA ("SMARTLoad200DedicatedAccount_54", 54, "20000", "PHP"));
		lookupResourcesByName.put("SMARTLoad200DedicatedAccount_1", helper.createDA ("SMARTLoad200DedicatedAccount_1", 1, "20000", "PHP"));
		lookupResourcesByName.put("SMARTLoad200Refill_L001", helper.createRefill("SMARTLoad200Refill_L001", "L001", 1, "20000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad300DedicatedAccount_54", helper.createDA ("SMARTLoad300DedicatedAccount_54", 54, "30000", "PHP"));
		lookupResourcesByName.put("SMARTLoad300DedicatedAccount_1", helper.createDA ("SMARTLoad300DedicatedAccount_1", 1, "30000", "PHP"));
		lookupResourcesByName.put("SMARTLoad300Refill_L001", helper.createRefill("SMARTLoad300Refill_L001", "L001", 1, "30000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad15DedicatedAccount_1", helper.createDA ("SMARTLoad15DedicatedAccount_1", 1, "1500", "PHP"));
		lookupResourcesByName.put("SMARTLoad15Refill_L001", helper.createRefill("SMARTLoad15Refill_L001", "L001", 1, "1500", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad50DedicatedAccount_1", helper.createDA ("SMARTLoad50DedicatedAccount_1", 1, "5000", "PHP"));
		lookupResourcesByName.put("SMARTLoad50Refill_L001", helper.createRefill("SMARTLoad50Refill_L001", "L001", 1, "5000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad100DedicatedAccount_1", helper.createDA ("SMARTLoad100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("SMARTLoad100Refill_L001", helper.createRefill("SMARTLoad100Refill_L001", "L001", 1, "10000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad250DedicatedAccount_1", helper.createDA ("SMARTLoad250DedicatedAccount_1", 1, "25000", "PHP"));
		lookupResourcesByName.put("SMARTLoad250Refill_L001", helper.createRefill("SMARTLoad250Refill_L001", "L001", 1, "25000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("SMARTLoad20DedicatedAccount_1", helper.createDA ("SMARTLoad20DedicatedAccount_1", 1, "2000", "PHP"));
		lookupResourcesByName.put("SMARTLoad20Refill_L001", helper.createRefill("SMARTLoad20Refill_L001", "L001", 1, "2000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("CTCLoad100DedicatedAccount_1", helper.createDA ("CTCLoad100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("CTCLoad100Refill_CTC1", helper.createRefill("CTCLoad100Refill_CTC1", "CTC1", 1, "10000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("CTCLoad300DedicatedAccount_1", helper.createDA ("CTCLoad300DedicatedAccount_1", 1, "30000", "PHP"));
		lookupResourcesByName.put("CTCLoad300DedicatedAccount_54", helper.createDA ("CTCLoad300DedicatedAccount_54", 54, "30000", "PHP"));
		lookupResourcesByName.put("CTCLoad300Refill_CTC1", helper.createRefill("CTCLoad300Refill_CTC1", "CTC1", 1, "30000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("CTCLoad500DedicatedAccount_1", helper.createDA ("CTCLoad500DedicatedAccount_1", 1, "50000", "PHP"));
		lookupResourcesByName.put("CTCLoad500DedicatedAccount_54", helper.createDA ("CTCLoad500DedicatedAccount_54", 54, "50000", "PHP"));
		lookupResourcesByName.put("CTCLoad500Refill_CTC1", helper.createRefill("CTCLoad500Refill_CTC1", "CTC1", 1, "50000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("CTCLoad1000DedicatedAccount_1", helper.createDA ("CTCLoad1000DedicatedAccount_1", 1, "100000", "PHP"));
		lookupResourcesByName.put("CTCLoad1000Refill_CTC1", helper.createRefill("CTCLoad1000Refill_CTC1", "CTC1", 1, "100000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("UnliTxt2All15RegionalRefill_1001", helper.createRefill("UnliTxt2All15RegionalRefill_1001", "1001", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("UnliAllText10RegionalDedicatedAccount_54", helper.createDA ("UnliAllText10RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("UnliAllText10RegionalRefill_1053", helper.createRefill("UnliAllText10RegionalRefill_1053", "1053", 1, "1", CurrencyCode.PHP, new String [] {"1054"}));
		lookupResourcesByName.put("GaanUnliTxtPlus15DedicatedAccount_3", helper.createDA ("GaanUnliTxtPlus15DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanUnliTxtPlus15Refill_1002", helper.createRefill("GaanUnliTxtPlus15Refill_1002", "1002", 1, "1", CurrencyCode.PHP, new String [] {"1003","2101"}));
		lookupResourcesByName.put("GaanUnliTxtPlus20DedicatedAccount_3", helper.createDA ("GaanUnliTxtPlus20DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanUnliTxtPlus20Refill_1003", helper.createRefill("GaanUnliTxtPlus20Refill_1003", "1003", 1, "1", CurrencyCode.PHP, new String [] {"1003","2101"}));
		lookupResourcesByName.put("GaanUnliTxtPlus30DedicatedAccount_3", helper.createDA ("GaanUnliTxtPlus30DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanUnliTxtPlus30Refill_1004", helper.createRefill("GaanUnliTxtPlus30Refill_1004", "1004", 1, "1", CurrencyCode.PHP, new String [] {"1003","2101"}));
		lookupResourcesByName.put("UnliTalkPlus20Refill_1005", helper.createRefill("UnliTalkPlus20Refill_1005", "1005", 1, "1", CurrencyCode.PHP, new String [] {"2001","2101"}));
		lookupResourcesByName.put("UnliTalkPlus100Refill_1006", helper.createRefill("UnliTalkPlus100Refill_1006", "1006", 1, "10", CurrencyCode.PHP, new String [] {"2001","2101"}));
		lookupResourcesByName.put("UnliTextExtra30DedicatedAccount_3", helper.createDA ("UnliTextExtra30DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("UnliTextExtra30Refill_1007", helper.createRefill("UnliTextExtra30Refill_1007", "1007", 1, "1", CurrencyCode.PHP, new String [] {"1003","2101"}));
		lookupResourcesByName.put("GaanTxt10DedicatedAccount_54", helper.createDA ("GaanTxt10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanTxt10DedicatedAccount_52", helper.createDA ("GaanTxt10DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("GaanTxt10Refill_1008", helper.createRefill("GaanTxt10Refill_1008", "1008", 1, "1", CurrencyCode.PHP, new String [] {"1054","1052"}));
		lookupResourcesByName.put("GaanTxt20DedicatedAccount_54", helper.createDA ("GaanTxt20DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("GaanTxt20Refill_1009", helper.createRefill("GaanTxt20Refill_1009", "1009", 1, "1", CurrencyCode.PHP, new String [] {"1054"}));
		lookupResourcesByName.put("UnliTxt10Refill_1010", helper.createRefill("UnliTxt10Refill_1010", "1010", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxt150Refill_1011", helper.createRefill("UnliTxt150Refill_1011", "1011", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxt5Refill_1012", helper.createRefill("UnliTxt5Refill_1012", "1012", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		lookupResourcesByName.put("UnliTxt2All20Refill_1013", helper.createRefill("UnliTxt2All20Refill_1013", "1013", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("Regular60DedicatedAccount_1", helper.createDA ("Regular60DedicatedAccount_1", 1, "6000", "PHP"));
		lookupResourcesByName.put("Regular60Refill_L001", helper.createRefill("Regular60Refill_L001", "L001", 1, "6000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("Extra115DedicatedAccount_1", helper.createDA ("Extra115DedicatedAccount_1", 1, "11500", "PHP"));
		lookupResourcesByName.put("Extra115Refill_L001", helper.createRefill("Extra115Refill_L001", "L001", 1, "11500", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("Economy30DedicatedAccount_1", helper.createDA ("Economy30DedicatedAccount_1", 1, "3000", "PHP"));
		lookupResourcesByName.put("Economy30Refill_L001", helper.createRefill("Economy30Refill_L001", "L001", 1, "3000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("AllText10DedicatedAccount_54", helper.createDA ("AllText10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("AllText10Refill_1017", helper.createRefill("AllText10Refill_1017", "1017", 1, "1", CurrencyCode.PHP, new String [] {"1054"}));
		lookupResourcesByName.put("AllText20Refill_1018", helper.createRefill("AllText20Refill_1018", "1018", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("TimerOffer_1005", helper.createTimerProfile("TimerOffer_1005",1005));
		lookupResourcesByName.put("TimerOffer_1141", helper.createTimerProfile("TimerOffer_1141",1141));
		lookupResourcesByName.put("EnhancedAllText20DedicatedAccount_5", helper.createDA ("EnhancedAllText20DedicatedAccount_5", 5, "1", "PHP"));
		lookupResourcesByName.put("EnhancedAllText20DedicatedAccount_141", helper.createDA ("EnhancedAllText20DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("EnhancedAllText20Refill_1018", helper.createRefill("EnhancedAllText20Refill_1018", "1018", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("EnhancedAllText30DedicatedAccount_5", helper.createDA ("EnhancedAllText30DedicatedAccount_5", 5, "1", "PHP"));
		lookupResourcesByName.put("EnhancedAllText30DedicatedAccount_141", helper.createDA ("EnhancedAllText30DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("EnhancedAllText30Refill_1069", helper.createRefill("EnhancedAllText30Refill_1069", "1069", 1, "1", CurrencyCode.PHP, new String [] {"2103","1005","1141"}));
		lookupResourcesByName.put("GaanUnliTxtPlus15RegionalDedicatedAccount_3", helper.createDA ("GaanUnliTxtPlus15RegionalDedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("GaanUnliTxtPlus15RegionalRefill_1024", helper.createRefill("GaanUnliTxtPlus15RegionalRefill_1024", "1024", 1, "1", CurrencyCode.PHP, new String [] {"1003","2101"}));
		lookupResourcesByName.put("Txt2All5DedicatedAccount_54", helper.createDA ("Txt2All5DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("Txt2All5Refill_1041", helper.createRefill("Txt2All5Refill_1041", "1041", 1, "1", CurrencyCode.PHP, new String [] {"1054"}));
		lookupResourcesByName.put("PantawidLoad1BucketDedicatedAccount_52", helper.createDA ("PantawidLoad1BucketDedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("PantawidLoad1BucketRefill_1045", helper.createRefill("PantawidLoad1BucketRefill_1045", "1045", 1, "1", CurrencyCode.PHP, new String [] {"1052"}));
		lookupResourcesByName.put("PantawidLoad1ELoadDedicatedAccount_1", helper.createDA ("PantawidLoad1ELoadDedicatedAccount_1", 1, "1", "PHP"));
		lookupResourcesByName.put("PantawidLoad1ELoadRefill_1070", helper.createRefill("PantawidLoad1ELoadRefill_1070", "1070", 1, "1", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("UnliTxt2AllPlus10DedicatedAccount_3", helper.createDA ("UnliTxt2AllPlus10DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("UnliTxt2AllPlus10Refill_1046", helper.createRefill("UnliTxt2AllPlus10Refill_1046", "1046", 1, "1", CurrencyCode.PHP, new String [] {"1003","2103"}));
		lookupResourcesByName.put("UnliText2All300Refill_1047", helper.createRefill("UnliText2All300Refill_1047", "1047", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("TimerOffer_1060", helper.createTimerProfile("TimerOffer_1060",1060));
		lookupResourcesByName.put("ArawArawText20DedicatedAccount_60", helper.createDA ("ArawArawText20DedicatedAccount_60", 60, "100", "PHP"));
		lookupResourcesByName.put("ArawArawText20Refill_1044", helper.createRefill("ArawArawText20Refill_1044", "1044", 1, "100", CurrencyCode.PHP, new String [] {"1060"}));
		lookupResourcesByName.put("TimerOffer_1143", helper.createTimerProfile("TimerOffer_1143",1143));
		lookupResourcesByName.put("ArawArawLoad30DedicatedAccount_3", helper.createDA ("ArawArawLoad30DedicatedAccount_3", 3, "100", "PHP"));
		lookupResourcesByName.put("ArawArawLoad30DedicatedAccount_143", helper.createDA ("ArawArawLoad30DedicatedAccount_143", 143, "100", "PHP"));
		lookupResourcesByName.put("ArawArawLoad30Refill_1049", helper.createRefill("ArawArawLoad30Refill_1049", "1049", 1, "100", CurrencyCode.PHP, new String [] {"1003","1143","2103"}));
		lookupResourcesByName.put("ArawArawLoad60DedicatedAccount_3", helper.createDA ("ArawArawLoad60DedicatedAccount_3", 3, "100", "PHP"));
		lookupResourcesByName.put("ArawArawLoad60DedicatedAccount_143", helper.createDA ("ArawArawLoad60DedicatedAccount_143", 143, "100", "PHP"));
		lookupResourcesByName.put("ArawArawLoad60Refill_1050", helper.createRefill("ArawArawLoad60Refill_1050", "1050", 1, "100", CurrencyCode.PHP, new String [] {"1003","1143","2103"}));
		lookupResourcesByName.put("TimerOffer_1151", helper.createTimerProfile("TimerOffer_1151",1151));
		lookupResourcesByName.put("ICHampDedicatedAccount_52", helper.createDA ("ICHampDedicatedAccount_52", 52, "100", "PHP"));
		lookupResourcesByName.put("ICHampDedicatedAccount_151", helper.createDA ("ICHampDedicatedAccount_151", 151, "100", "PHP"));
		lookupResourcesByName.put("ICHampRefill_1051", helper.createRefill("ICHampRefill_1051", "1051", 1, "100", CurrencyCode.PHP, new String [] {"1052","1151"}));
		lookupResourcesByName.put("TimerOffer_1059", helper.createTimerProfile("TimerOffer_1059",1059));
		lookupResourcesByName.put("KamusText20DedicatedAccount_59", helper.createDA ("KamusText20DedicatedAccount_59", 59, "1", "PHP"));
		lookupResourcesByName.put("KamusText20Refill_3001", helper.createRefill("KamusText20Refill_3001", "3001", 1, "1", CurrencyCode.PHP, new String [] {"1059"}));
		lookupResourcesByName.put("TimerOffer_1102", helper.createTimerProfile("TimerOffer_1102",1102));
		lookupResourcesByName.put("FlexiCallandTextAbroad30DedicatedAccount_102", helper.createDA ("FlexiCallandTextAbroad30DedicatedAccount_102", 102, "1", "PHP"));
		lookupResourcesByName.put("FlexiCallandTextAbroad30Refill_3002", helper.createRefill("FlexiCallandTextAbroad30Refill_3002", "3002", 1, "1", CurrencyCode.PHP, new String [] {"1102"}));
		lookupResourcesByName.put("TimerOffer_1103", helper.createTimerProfile("TimerOffer_1103",1103));
		lookupResourcesByName.put("FlexiCallandTextAbroad50DedicatedAccount_103", helper.createDA ("FlexiCallandTextAbroad50DedicatedAccount_103", 103, "1", "PHP"));
		lookupResourcesByName.put("FlexiCallandTextAbroad50Refill_3003", helper.createRefill("FlexiCallandTextAbroad50Refill_3003", "3003", 1, "1", CurrencyCode.PHP, new String [] {"1103"}));
		lookupResourcesByName.put("TimerOffer_1008", helper.createTimerProfile("TimerOffer_1008",1008));
		lookupResourcesByName.put("TalkALot300DedicatedAccount_8", helper.createDA ("TalkALot300DedicatedAccount_8", 8, "1", "PHP"));
		lookupResourcesByName.put("TalkALot300Refill_3004", helper.createRefill("TalkALot300Refill_3004", "3004", 1, "1", CurrencyCode.PHP, new String [] {"1008"}));
		lookupResourcesByName.put("TimerOffer_1057", helper.createTimerProfile("TimerOffer_1057",1057));
		lookupResourcesByName.put("TextALot35DedicatedAccount_52", helper.createDA ("TextALot35DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("TextALot35DedicatedAccount_54", helper.createDA ("TextALot35DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("TextALot35DedicatedAccount_57", helper.createDA ("TextALot35DedicatedAccount_57", 57, "1", "PHP"));
		lookupResourcesByName.put("TextALot35Refill_3005", helper.createRefill("TextALot35Refill_3005", "3005", 1, "1", CurrencyCode.PHP, new String [] {"1052","1054","1057"}));
		lookupResourcesByName.put("TextALot60DedicatedAccount_52", helper.createDA ("TextALot60DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("TextALot60DedicatedAccount_54", helper.createDA ("TextALot60DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("TextALot60DedicatedAccount_57", helper.createDA ("TextALot60DedicatedAccount_57", 57, "1", "PHP"));
		lookupResourcesByName.put("TextALot60Refill_3006", helper.createRefill("TextALot60Refill_3006", "3006", 1, "1", CurrencyCode.PHP, new String [] {"1052","1054","1057"}));
		lookupResourcesByName.put("TimerOffer_1056", helper.createTimerProfile("TimerOffer_1056",1056));
		lookupResourcesByName.put("TextTipid100DedicatedAccount_56", helper.createDA ("TextTipid100DedicatedAccount_56", 56, "1", "PHP"));
		lookupResourcesByName.put("TextTipid100Refill_3007", helper.createRefill("TextTipid100Refill_3007", "3007", 1, "1", CurrencyCode.PHP, new String [] {"1056"}));
		lookupResourcesByName.put("TextTipid150DedicatedAccount_56", helper.createDA ("TextTipid150DedicatedAccount_56", 56, "1", "PHP"));
		lookupResourcesByName.put("TextTipid150Refill_3008", helper.createRefill("TextTipid150Refill_3008", "3008", 1, "1", CurrencyCode.PHP, new String [] {"1056"}));
		lookupResourcesByName.put("TextTipid200DedicatedAccount_56", helper.createDA ("TextTipid200DedicatedAccount_56", 56, "1", "PHP"));
		lookupResourcesByName.put("TextTipid200Refill_3009", helper.createRefill("TextTipid200Refill_3009", "3009", 1, "1", CurrencyCode.PHP, new String [] {"1056"}));
		lookupResourcesByName.put("TextTipid300DedicatedAccount_56", helper.createDA ("TextTipid300DedicatedAccount_56", 56, "1", "PHP"));
		lookupResourcesByName.put("TextTipid300Refill_3010", helper.createRefill("TextTipid300Refill_3010", "3010", 1, "1", CurrencyCode.PHP, new String [] {"1056"}));
		lookupResourcesByName.put("TimerOffer_2314", helper.createTimerProfile("TimerOffer_2314",2314));
		lookupResourcesByName.put("UnliSurf500Refill_3011", helper.createRefill("UnliSurf500Refill_3011", "3011", 1, "1", CurrencyCode.PHP, new String [] {"2314"}));
		lookupResourcesByName.put("TimerOffer_1014", helper.createTimerProfile("TimerOffer_1014",1014));
		lookupResourcesByName.put("TimerOffer_1058", helper.createTimerProfile("TimerOffer_1058",1058));
		lookupResourcesByName.put("TalkTipid100DedicatedAccount_14", helper.createDA ("TalkTipid100DedicatedAccount_14", 14, "1", "PHP"));
		lookupResourcesByName.put("TalkTipid100DedicatedAccount_58", helper.createDA ("TalkTipid100DedicatedAccount_58", 58, "1", "PHP"));
		lookupResourcesByName.put("TalkTipid100Refill_3012", helper.createRefill("TalkTipid100Refill_3012", "3012", 1, "1", CurrencyCode.PHP, new String [] {"1014","1058"}));
		lookupResourcesByName.put("TalkTipid200DedicatedAccount_14", helper.createDA ("TalkTipid200DedicatedAccount_14", 14, "1", "PHP"));
		lookupResourcesByName.put("TalkTipid200DedicatedAccount_58", helper.createDA ("TalkTipid200DedicatedAccount_58", 58, "1", "PHP"));
		lookupResourcesByName.put("TalkTipid200Refill_3013", helper.createRefill("TalkTipid200Refill_3013", "3013", 1, "1", CurrencyCode.PHP, new String [] {"1014","1058"}));
		lookupResourcesByName.put("TimerOffer_2309", helper.createTimerProfile("TimerOffer_2309",2309));
		lookupResourcesByName.put("UnliSurf50Refill_5001", helper.createRefill("UnliSurf50Refill_5001", "5001", 1, "1", CurrencyCode.PHP, new String [] {"2309"}));
		lookupResourcesByName.put("UnliSurf300Refill_5002", helper.createRefill("UnliSurf300Refill_5002", "5002", 1, "1", CurrencyCode.PHP, new String [] {"2309"}));
		lookupResourcesByName.put("UnliSurf1200Refill_5003", helper.createRefill("UnliSurf1200Refill_5003", "5003", 1, "1", CurrencyCode.PHP, new String [] {"2309"}));
		lookupResourcesByName.put("TimerOffer_2315", helper.createTimerProfile("TimerOffer_2315",2315));
		lookupResourcesByName.put("SurfNet30Refill_5004", helper.createRefill("SurfNet30Refill_5004", "5004", 1, "1", CurrencyCode.PHP, new String [] {"2315"}));
		lookupResourcesByName.put("TimerOffer_1142", helper.createTimerProfile("TimerOffer_1142",1142));
		lookupResourcesByName.put("TNTPaidSamplerDedicatedAccount_142", helper.createDA ("TNTPaidSamplerDedicatedAccount_142", 142, "1", "PHP"));
		lookupResourcesByName.put("TNTPaidSamplerRefill_5005", helper.createRefill("TNTPaidSamplerRefill_5005", "5005", 1, "1", CurrencyCode.PHP, new String [] {"1142"}));
		lookupResourcesByName.put("AlwaysOn10DedicatedAccount_141", helper.createDA ("AlwaysOn10DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn10Refill_5006", helper.createRefill("AlwaysOn10Refill_5006", "5006", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn20DedicatedAccount_141", helper.createDA ("AlwaysOn20DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn20Refill_5007", helper.createRefill("AlwaysOn20Refill_5007", "5007", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn30DedicatedAccount_141", helper.createDA ("AlwaysOn30DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn30Refill_5008", helper.createRefill("AlwaysOn30Refill_5008", "5008", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn99DedicatedAccount_141", helper.createDA ("AlwaysOn99DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn99Refill_5009", helper.createRefill("AlwaysOn99Refill_5009", "5009", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn199DedicatedAccount_141", helper.createDA ("AlwaysOn199DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn199Refill_5010", helper.createRefill("AlwaysOn199Refill_5010", "5010", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn299DedicatedAccount_141", helper.createDA ("AlwaysOn299DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn299Refill_5011", helper.createRefill("AlwaysOn299Refill_5011", "5011", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn499DedicatedAccount_141", helper.createDA ("AlwaysOn499DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn499Refill_5012", helper.createRefill("AlwaysOn499Refill_5012", "5012", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("AlwaysOn995DedicatedAccount_141", helper.createDA ("AlwaysOn995DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("AlwaysOn995Refill_5013", helper.createRefill("AlwaysOn995Refill_5013", "5013", 1, "1", CurrencyCode.PHP, new String [] {"1141"}));
		lookupResourcesByName.put("TimerOffer_2301", helper.createTimerProfile("TimerOffer_2301",2301));
		lookupResourcesByName.put("TimerOffer_2316", helper.createTimerProfile("TimerOffer_2316",2316));
		lookupResourcesByName.put("Facebook10PantherRefill_5014", helper.createRefill("Facebook10PantherRefill_5014", "5014", 1, "1", CurrencyCode.PHP, new String [] {"2301","2316"}));
		lookupResourcesByName.put("TimerOffer_2302", helper.createTimerProfile("TimerOffer_2302",2302));
		lookupResourcesByName.put("TimerOffer_2317", helper.createTimerProfile("TimerOffer_2317",2317));
		lookupResourcesByName.put("Yahoo15PantherRefill_5015", helper.createRefill("Yahoo15PantherRefill_5015", "5015", 1, "1", CurrencyCode.PHP, new String [] {"2302","2317"}));
		lookupResourcesByName.put("TimerOffer_2313", helper.createTimerProfile("TimerOffer_2313",2313));
		lookupResourcesByName.put("BlackberryDaily50Refill_5016", helper.createRefill("BlackberryDaily50Refill_5016", "5016", 1, "1", CurrencyCode.PHP, new String [] {"2309","2313"}));
		lookupResourcesByName.put("BlackberryWeekly300Refill_5017", helper.createRefill("BlackberryWeekly300Refill_5017", "5017", 1, "1", CurrencyCode.PHP, new String [] {"2309","2313"}));
		lookupResourcesByName.put("BlackberryMonthly599Refill_5018", helper.createRefill("BlackberryMonthly599Refill_5018", "5018", 1, "1", CurrencyCode.PHP, new String [] {"2309","2313"}));
		lookupResourcesByName.put("BlackberryLiteEmailDaily35Refill_5019", helper.createRefill("BlackberryLiteEmailDaily35Refill_5019", "5019", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("BlackberryLiteEmailMonthly299Refill_5020", helper.createRefill("BlackberryLiteEmailMonthly299Refill_5020", "5020", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("BlackberrySocialDaily35Refill_5021", helper.createRefill("BlackberrySocialDaily35Refill_5021", "5021", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("BlackberrySocialMonthly299Refill_5022", helper.createRefill("BlackberrySocialMonthly299Refill_5022", "5022", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("BlackberryMessengerDaily15DedicatedAccount_3", helper.createDA ("BlackberryMessengerDaily15DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("BlackberryMessengerDaily15Refill_5023", helper.createRefill("BlackberryMessengerDaily15Refill_5023", "5023", 1, "1", CurrencyCode.PHP, new String [] {"1003","2313"}));
		lookupResourcesByName.put("BlackberryMessengerMonthly99DedicatedAccount_3", helper.createDA ("BlackberryMessengerMonthly99DedicatedAccount_3", 3, "1", "PHP"));
		lookupResourcesByName.put("BlackberryMessengerMonthly99Refill_5024", helper.createRefill("BlackberryMessengerMonthly99Refill_5024", "5024", 1, "1", CurrencyCode.PHP, new String [] {"1003","2313"}));
		lookupResourcesByName.put("BlackberryMonthly599MicrowarehouseRefill_5025", helper.createRefill("BlackberryMonthly599MicrowarehouseRefill_5025", "5025", 1, "1", CurrencyCode.PHP, new String [] {"2309","2313"}));
		lookupResourcesByName.put("BlackberryLiteEmailMonthly299MicrowarehouseRefill_5026", helper.createRefill("BlackberryLiteEmailMonthly299MicrowarehouseRefill_5026", "5026", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("BlackberrySocialMonthly299MicrowarehouseRefill_5027", helper.createRefill("BlackberrySocialMonthly299MicrowarehouseRefill_5027", "5027", 1, "1", CurrencyCode.PHP, new String [] {"2313"}));
		lookupResourcesByName.put("SnapperDaily10Refill_5028", helper.createRefill("SnapperDaily10Refill_5028", "5028", 1, "1", CurrencyCode.PHP, new String [] {"2301"}));
		lookupResourcesByName.put("P1TNTMicrosachetFBRefill_5029", helper.createRefill("P1TNTMicrosachetFBRefill_5029", "5029", 1, "1", CurrencyCode.PHP, new String [] {"2301"}));
		lookupResourcesByName.put("P5TNTMicrosachetFBRefill_5030", helper.createRefill("P5TNTMicrosachetFBRefill_5030", "5030", 1, "1", CurrencyCode.PHP, new String [] {"2301"}));
		lookupResourcesByName.put("TimerOffer_2303", helper.createTimerProfile("TimerOffer_2303",2303));
		lookupResourcesByName.put("OperaSurf15Refill_5031", helper.createRefill("OperaSurf15Refill_5031", "5031", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("OperaSurf60Refill_5032", helper.createRefill("OperaSurf60Refill_5032", "5032", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("OperaSurf80Refill_5033", helper.createRefill("OperaSurf80Refill_5033", "5033", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("OperaSurf160Refill_5034", helper.createRefill("OperaSurf160Refill_5034", "5034", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("OperaSurf299Refill_5035", helper.createRefill("OperaSurf299Refill_5035", "5035", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("OnPeakAccountIDDedicatedAccount_1", helper.createDA ("OnPeakAccountIDDedicatedAccount_1", 1, "", "PHP"));
		lookupResourcesByName.put("BucketMocOnDedicatedAccount_3", helper.createDA ("BucketMocOnDedicatedAccount_3", 3, "", "PHP"));
		lookupResourcesByName.put("BucketMocIntDedicatedAccount_8", helper.createDA ("BucketMocIntDedicatedAccount_8", 8, "", "PHP"));
		lookupResourcesByName.put("BucketTokMocDedicatedAccount_12", helper.createDA ("BucketTokMocDedicatedAccount_12", 12, "", "PHP"));
		lookupResourcesByName.put("BucketTkTpdMocDedicatedAccount_14", helper.createDA ("BucketTkTpdMocDedicatedAccount_14", 14, "", "PHP"));
		lookupResourcesByName.put("BucketSmsOnDedicatedAccount_52", helper.createDA ("BucketSmsOnDedicatedAccount_52", 52, "", "PHP"));
		lookupResourcesByName.put("BucketSmsGblDedicatedAccount_54", helper.createDA ("BucketSmsGblDedicatedAccount_54", 54, "", "PHP"));
		lookupResourcesByName.put("BucketSmsRoamDedicatedAccount_56", helper.createDA ("BucketSmsRoamDedicatedAccount_56", 56, "", "PHP"));
		lookupResourcesByName.put("BucketIntlS4SmsDedicatedAccount_57", helper.createDA ("BucketIntlS4SmsDedicatedAccount_57", 57, "", "PHP"));
		lookupResourcesByName.put("BucketTkTpdSmsDedicatedAccount_58", helper.createDA ("BucketTkTpdSmsDedicatedAccount_58", 58, "", "PHP"));
		lookupResourcesByName.put("BucketKamusTxtSmsDedicatedAccount_59", helper.createDA ("BucketKamusTxtSmsDedicatedAccount_59", 59, "", "PHP"));
		lookupResourcesByName.put("TimerOffer_2311", helper.createTimerProfile("TimerOffer_2311",2311));
		lookupResourcesByName.put("DataVolFbcDedicatedAccount_141", helper.createDA ("DataVolFbcDedicatedAccount_141", 141, "", "PHP"));
		lookupResourcesByName.put("DataMinFbcDedicatedAccount_142", helper.createDA ("DataMinFbcDedicatedAccount_142", 142, "", "PHP"));
		lookupResourcesByName.put("DataArawFbcDedicatedAccount_143", helper.createDA ("DataArawFbcDedicatedAccount_143", 143, "", "PHP"));
		lookupResourcesByName.put("SnsDataFbcDedicatedAccount_151", helper.createDA ("SnsDataFbcDedicatedAccount_151", 151, "", "PHP"));
		lookupResourcesByName.put("ConsWalletAccDedicatedAccount_101", helper.createDA ("ConsWalletAccDedicatedAccount_101", 101, "", "PHP"));
		lookupResourcesByName.put("BucketIntlS1CboDedicatedAccount_102", helper.createDA ("BucketIntlS1CboDedicatedAccount_102", 102, "", "PHP"));
		lookupResourcesByName.put("BucketIntlS2CboDedicatedAccount_103", helper.createDA ("BucketIntlS2CboDedicatedAccount_103", 103, "", "PHP"));
		lookupResourcesByName.put("PasaloadP2DedicatedAccount_1", helper.createDA ("PasaloadP2DedicatedAccount_1", 1, "200", "PHP"));
		lookupResourcesByName.put("PasaloadP2Refill_PL01", helper.createRefill("PasaloadP2Refill_PL01", "PL01", 1, "200", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP10DedicatedAccount_1", helper.createDA ("PasaloadP10DedicatedAccount_1", 1, "1000", "PHP"));
		lookupResourcesByName.put("PasaloadP10Refill_PL01", helper.createRefill("PasaloadP10Refill_PL01", "PL01", 1, "1000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP20DedicatedAccount_1", helper.createDA ("PasaloadP20DedicatedAccount_1", 1, "2000", "PHP"));
		lookupResourcesByName.put("PasaloadP20Refill_PL01", helper.createRefill("PasaloadP20Refill_PL01", "PL01", 1, "2000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP35DedicatedAccount_1", helper.createDA ("PasaloadP35DedicatedAccount_1", 1, "3500", "PHP"));
		lookupResourcesByName.put("PasaloadP35DedicatedAccount_54", helper.createDA ("PasaloadP35DedicatedAccount_54", 54, "3500", "PHP"));
		lookupResourcesByName.put("PasaloadP35Refill_PL01", helper.createRefill("PasaloadP35Refill_PL01", "PL01", 1, "3500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP60DedicatedAccount_1", helper.createDA ("PasaloadP60DedicatedAccount_1", 1, "6000", "PHP"));
		lookupResourcesByName.put("PasaloadP60Refill_PL01", helper.createRefill("PasaloadP60Refill_PL01", "PL01", 1, "6000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP200DedicatedAccount_1", helper.createDA ("PasaloadP200DedicatedAccount_1", 1, "20000", "PHP"));
		lookupResourcesByName.put("PasaloadP200Refill_PL01", helper.createRefill("PasaloadP200Refill_PL01", "PL01", 1, "20000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP100DedicatedAccount_1", helper.createDA ("PasaloadP100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("PasaloadP100Refill_PL01", helper.createRefill("PasaloadP100Refill_PL01", "PL01", 1, "10000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP15DedicatedAccount_1", helper.createDA ("PasaloadP15DedicatedAccount_1", 1, "1500", "PHP"));
		lookupResourcesByName.put("PasaloadP15Refill_PL01", helper.createRefill("PasaloadP15Refill_PL01", "PL01", 1, "1500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP30DedicatedAccount_1", helper.createDA ("PasaloadP30DedicatedAccount_1", 1, "3000", "PHP"));
		lookupResourcesByName.put("PasaloadP30Refill_PL01", helper.createRefill("PasaloadP30Refill_PL01", "PL01", 1, "3000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("PasaloadP5DedicatedAccount_1", helper.createDA ("PasaloadP5DedicatedAccount_1", 1, "500", "PHP"));
		lookupResourcesByName.put("PasaloadP5Refill_PL01", helper.createRefill("PasaloadP5Refill_PL01", "PL01", 1, "500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("TimerOffer_1241", helper.createTimerProfile("TimerOffer_1241",1241));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad15DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad15DedicatedAccount_241", 241, "1500", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad15Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad15Refill_AL01", "AL01", 1, "1500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad30DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad30DedicatedAccount_241", 241, "3000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad30Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad30Refill_AL01", "AL01", 1, "3000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad50DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad50DedicatedAccount_241", 241, "5000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad50Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad50Refill_AL01", "AL01", 1, "5000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad100DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad100DedicatedAccount_241", 241, "10000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad100Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad100Refill_AL01", "AL01", 1, "10000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad150DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad150DedicatedAccount_241", 241, "15000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad150Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad150Refill_AL01", "AL01", 1, "15000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad300DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad300DedicatedAccount_241", 241, "30000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad300Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad300Refill_AL01", "AL01", 1, "30000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad500DedicatedAccount_241", helper.createDA ("AlkansyaLoadWalletLoad500DedicatedAccount_241", 241, "50000", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadWalletLoad500Refill_AL01", helper.createRefill("AlkansyaLoadWalletLoad500Refill_AL01", "AL01", 1, "50000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP1DedicatedAccount_1", helper.createDA ("PasariliP1DedicatedAccount_1", 1, "100", "PHP"));
		lookupResourcesByName.put("PasariliP1Refill_AL01", helper.createRefill("PasariliP1Refill_AL01", "AL01", 1, "100", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP1.50DedicatedAccount_1", helper.createDA ("PasariliP1.50DedicatedAccount_1", 1, "150", "PHP"));
		lookupResourcesByName.put("PasariliP1.50Refill_AL01", helper.createRefill("PasariliP1.50Refill_AL01", "AL01", 1, "150", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP2DedicatedAccount_1", helper.createDA ("PasariliP2DedicatedAccount_1", 1, "200", "PHP"));
		lookupResourcesByName.put("PasariliP2Refill_AL01", helper.createRefill("PasariliP2Refill_AL01", "AL01", 1, "200", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP2.50DedicatedAccount_1", helper.createDA ("PasariliP2.50DedicatedAccount_1", 1, "250", "PHP"));
		lookupResourcesByName.put("PasariliP2.50Refill_AL01", helper.createRefill("PasariliP2.50Refill_AL01", "AL01", 1, "250", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP3DedicatedAccount_1", helper.createDA ("PasariliP3DedicatedAccount_1", 1, "300", "PHP"));
		lookupResourcesByName.put("PasariliP3Refill_AL01", helper.createRefill("PasariliP3Refill_AL01", "AL01", 1, "300", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP10DedicatedAccount_1", helper.createDA ("PasariliP10DedicatedAccount_1", 1, "1000", "PHP"));
		lookupResourcesByName.put("PasariliP10Refill_AL01", helper.createRefill("PasariliP10Refill_AL01", "AL01", 1, "1000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP15DedicatedAccount_1", helper.createDA ("PasariliP15DedicatedAccount_1", 1, "1500", "PHP"));
		lookupResourcesByName.put("PasariliP15Refill_AL01", helper.createRefill("PasariliP15Refill_AL01", "AL01", 1, "1500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP30DedicatedAccount_1", helper.createDA ("PasariliP30DedicatedAccount_1", 1, "3000", "PHP"));
		lookupResourcesByName.put("PasariliP30Refill_AL01", helper.createRefill("PasariliP30Refill_AL01", "AL01", 1, "3000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP60DedicatedAccount_1", helper.createDA ("PasariliP60DedicatedAccount_1", 1, "6000", "PHP"));
		lookupResourcesByName.put("PasariliP60Refill_AL01", helper.createRefill("PasariliP60Refill_AL01", "AL01", 1, "6000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP100DedicatedAccount_1", helper.createDA ("PasariliP100DedicatedAccount_1", 1, "10000", "PHP"));
		lookupResourcesByName.put("PasariliP100Refill_AL01", helper.createRefill("PasariliP100Refill_AL01", "AL01", 1, "10000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP115DedicatedAccount_1", helper.createDA ("PasariliP115DedicatedAccount_1", 1, "11500", "PHP"));
		lookupResourcesByName.put("PasariliP115Refill_AL01", helper.createRefill("PasariliP115Refill_AL01", "AL01", 1, "11500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP200DedicatedAccount_1", helper.createDA ("PasariliP200DedicatedAccount_1", 1, "20000", "PHP"));
		lookupResourcesByName.put("PasariliP200DedicatedAccount_54", helper.createDA ("PasariliP200DedicatedAccount_54", 54, "20000", "PHP"));
		lookupResourcesByName.put("PasariliP200Refill_AL01", helper.createRefill("PasariliP200Refill_AL01", "AL01", 1, "20000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP300DedicatedAccount_1", helper.createDA ("PasariliP300DedicatedAccount_1", 1, "30000", "PHP"));
		lookupResourcesByName.put("PasariliP300DedicatedAccount_54", helper.createDA ("PasariliP300DedicatedAccount_54", 54, "30000", "PHP"));
		lookupResourcesByName.put("PasariliP300Refill_AL01", helper.createRefill("PasariliP300Refill_AL01", "AL01", 1, "30000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliP500DedicatedAccount_1", helper.createDA ("PasariliP500DedicatedAccount_1", 1, "50000", "PHP"));
		lookupResourcesByName.put("PasariliP500DedicatedAccount_54", helper.createDA ("PasariliP500DedicatedAccount_54", 54, "50000", "PHP"));
		lookupResourcesByName.put("PasariliP500Refill_AL01", helper.createRefill("PasariliP500Refill_AL01", "AL01", 1, "50000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP1DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP1DedicatedAccount_52", 52, "100", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP1Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP1Refill_AL01", "AL01", 1, "100", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP2DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP2DedicatedAccount_52", 52, "200", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP2Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP2Refill_AL01", "AL01", 1, "200", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP3DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP3DedicatedAccount_52", 52, "300", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP3Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP3Refill_AL01", "AL01", 1, "300", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP4DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP4DedicatedAccount_52", 52, "400", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP4Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP4Refill_AL01", "AL01", 1, "400", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP5DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP5DedicatedAccount_52", 52, "500", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP5Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP5Refill_AL01", "AL01", 1, "500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP6DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP6DedicatedAccount_52", 52, "600", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP6Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP6Refill_AL01", "AL01", 1, "600", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP7DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP7DedicatedAccount_52", 52, "700", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP7Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP7Refill_AL01", "AL01", 1, "700", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP8DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP8DedicatedAccount_52", 52, "800", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP8Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP8Refill_AL01", "AL01", 1, "800", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP9DedicatedAccount_52", helper.createDA ("PasariliAlkansyaInstantTextP9DedicatedAccount_52", 52, "900", "PHP"));
		lookupResourcesByName.put("PasariliAlkansyaInstantTextP9Refill_AL01", helper.createRefill("PasariliAlkansyaInstantTextP9Refill_AL01", "AL01", 1, "900", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliUnliTxt5Refill_AL01", helper.createRefill("PasariliUnliTxt5Refill_AL01", "AL01", 1, "500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliUnliTxt10Refill_AL01", helper.createRefill("PasariliUnliTxt10Refill_AL01", "AL01", 1, "1000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliPatok10DedicatedAccount_101", helper.createDA ("PasariliPatok10DedicatedAccount_101", 101, "1000", "PHP"));
		lookupResourcesByName.put("PasariliPatok10Refill_AL01", helper.createRefill("PasariliPatok10Refill_AL01", "AL01", 1, "1000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliUpsize35DedicatedAccount_1", helper.createDA ("PasariliUpsize35DedicatedAccount_1", 1, "3500", "PHP"));
		lookupResourcesByName.put("PasariliUpsize35DedicatedAccount_52", helper.createDA ("PasariliUpsize35DedicatedAccount_52", 52, "3500", "PHP"));
		lookupResourcesByName.put("PasariliUpsize35Refill_AL01", helper.createRefill("PasariliUpsize35Refill_AL01", "AL01", 1, "3500", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("PasariliUpsize20DedicatedAccount_1", helper.createDA ("PasariliUpsize20DedicatedAccount_1", 1, "2000", "PHP"));
		lookupResourcesByName.put("PasariliUpsize20DedicatedAccount_52", helper.createDA ("PasariliUpsize20DedicatedAccount_52", 52, "2000", "PHP"));
		lookupResourcesByName.put("PasariliUpsize20Refill_AL01", helper.createRefill("PasariliUpsize20Refill_AL01", "AL01", 1, "2000", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaLoadToMeIponDedicatedAccount_1", helper.createDA ("AlkansyaLoadToMeIponDedicatedAccount_1", 1, "1", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadToMeIponDedicatedAccount_241", helper.createDA ("AlkansyaLoadToMeIponDedicatedAccount_241", 241, "1", "PHP"));
		lookupResourcesByName.put("AlkansyaLoadToMeIponRefill_AL01", helper.createRefill("AlkansyaLoadToMeIponRefill_AL01", "AL01", 1, "1", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaActivateFirstTimeDedicatedAccount_241", helper.createDA ("AlkansyaActivateFirstTimeDedicatedAccount_241", 241, "1", "PHP"));
		lookupResourcesByName.put("AlkansyaActivateFirstTimeRefill_AL01", helper.createRefill("AlkansyaActivateFirstTimeRefill_AL01", "AL01", 1, "1", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("UnivWalletAccDedicatedAccount_241", helper.createDA ("UnivWalletAccDedicatedAccount_241", 241, "", "PHP"));
		lookupResourcesByName.put("TimerOffer_2019", helper.createTimerProfile("TimerOffer_2019",2019));
		lookupResourcesByName.put("TimerOffer_2015", helper.createTimerProfile("TimerOffer_2015",2015));
		lookupResourcesByName.put("Katok10PromoRefill_K10", helper.createRefill("Katok10PromoRefill_K10", "K10", 1, "1000", CurrencyCode.PHP, new String [] {"2015"}));
		lookupResourcesByName.put("Katok10FreebiePromoRefill_{UpdateOffer}", helper.createRefill("Katok10FreebiePromoRefill_{UpdateOffer}", "{UpdateOffer}", 1, "100", CurrencyCode.PHP, new String [] {"2019"}));
		lookupResourcesByName.put("TimerOffer_2016", helper.createTimerProfile("TimerOffer_2016",2016));
		lookupResourcesByName.put("Katok15PromoRefill_K15", helper.createRefill("Katok15PromoRefill_K15", "K15", 1, "1500", CurrencyCode.PHP, new String [] {"2016"}));
		lookupResourcesByName.put("TimerOffer_2017", helper.createTimerProfile("TimerOffer_2017",2017));
		lookupResourcesByName.put("KatokAttTex25PromoRefill_KT25", helper.createRefill("KatokAttTex25PromoRefill_KT25", "KT25", 1, "2500", CurrencyCode.PHP, new String [] {"2017"}));
		lookupResourcesByName.put("TimerOffer_2018", helper.createTimerProfile("TimerOffer_2018",2018));
		lookupResourcesByName.put("KatokAttTex35PromoRefill_KT35", helper.createRefill("KatokAttTex35PromoRefill_KT35", "KT35", 1, "3500", CurrencyCode.PHP, new String [] {"2018"}));
		lookupResourcesByName.put("ProjectLucky13DedicatedAccount_52", helper.createDA ("ProjectLucky13DedicatedAccount_52", 52, "1", "PHP"));
		lookupResourcesByName.put("ProjectLucky13Refill_1054", helper.createRefill("ProjectLucky13Refill_1054", "1054", 1, "1", CurrencyCode.PHP, new String [] {"1052"}));
		lookupResourcesByName.put("TimerOffer_1041", helper.createTimerProfile("TimerOffer_1041",1041));
		lookupResourcesByName.put("Trinet300DedicatedAccount_5", helper.createDA ("Trinet300DedicatedAccount_5", 5, "1", "PHP"));
		lookupResourcesByName.put("Trinet300DedicatedAccount_141", helper.createDA ("Trinet300DedicatedAccount_141", 141, "1", "PHP"));
		lookupResourcesByName.put("Trinet300Refill_1067", helper.createRefill("Trinet300Refill_1067", "1067", 1, "1", CurrencyCode.PHP, new String [] {"1005","1041","2102"}));
		lookupResourcesByName.put("IDDol20DedicatedAccount_8", helper.createDA ("IDDol20DedicatedAccount_8", 8, "1", "PHP"));
		lookupResourcesByName.put("IDDol20Refill_3014", helper.createRefill("IDDol20Refill_3014", "3014", 1, "1", CurrencyCode.PHP, new String [] {"1008"}));
		lookupResourcesByName.put("IDDol25DedicatedAccount_8", helper.createDA ("IDDol25DedicatedAccount_8", 8, "1", "PHP"));
		lookupResourcesByName.put("IDDol25Refill_3015", helper.createRefill("IDDol25Refill_3015", "3015", 1, "1", CurrencyCode.PHP, new String [] {"1008"}));
		lookupResourcesByName.put("IDDol40DedicatedAccount_8", helper.createDA ("IDDol40DedicatedAccount_8", 8, "1", "PHP"));
		lookupResourcesByName.put("IDDol40Refill_3016", helper.createRefill("IDDol40Refill_3016", "3016", 1, "1", CurrencyCode.PHP, new String [] {"1008"}));
		lookupResourcesByName.put("IDDol100DedicatedAccount_8", helper.createDA ("IDDol100DedicatedAccount_8", 8, "1", "PHP"));
		lookupResourcesByName.put("IDDol100Refill_3017", helper.createRefill("IDDol100Refill_3017", "3017", 1, "1", CurrencyCode.PHP, new String [] {"1008"}));
		lookupResourcesByName.put("TimerOffer_8001", helper.createTimerProfile("TimerOffer_8001",8001));
		lookupResourcesByName.put("IDDSaleRefill_3018", helper.createRefill("IDDSaleRefill_3018", "3018", 1, "1", CurrencyCode.PHP, new String [] {"8001"}));
		lookupResourcesByName.put("TimerOffer_2319", helper.createTimerProfile("TimerOffer_2319",2319));
		lookupResourcesByName.put("EmailPackage5Refill_5036", helper.createRefill("EmailPackage5Refill_5036", "5036", 1, "1", CurrencyCode.PHP, new String [] {"2319"}));
		lookupResourcesByName.put("EmailPackage10Refill_5037", helper.createRefill("EmailPackage10Refill_5037", "5037", 1, "1", CurrencyCode.PHP, new String [] {"2319"}));
		lookupResourcesByName.put("TimerOffer_2320", helper.createTimerProfile("TimerOffer_2320",2320));
		lookupResourcesByName.put("ChatPackage5Refill_5038", helper.createRefill("ChatPackage5Refill_5038", "5038", 1, "1", CurrencyCode.PHP, new String [] {"2320"}));
		lookupResourcesByName.put("ChatPackage10Refill_5039", helper.createRefill("ChatPackage10Refill_5039", "5039", 1, "1", CurrencyCode.PHP, new String [] {"2320"}));
		lookupResourcesByName.put("TimerOffer_2321", helper.createTimerProfile("TimerOffer_2321",2321));
		lookupResourcesByName.put("PhotoPackage10Refill_5040", helper.createRefill("PhotoPackage10Refill_5040", "5040", 1, "1", CurrencyCode.PHP, new String [] {"2321"}));
		lookupResourcesByName.put("PhotoPackage20Refill_5041", helper.createRefill("PhotoPackage20Refill_5041", "5041", 1, "1", CurrencyCode.PHP, new String [] {"2321"}));
		lookupResourcesByName.put("TimerOffer_2322", helper.createTimerProfile("TimerOffer_2322",2322));
		lookupResourcesByName.put("SocialPackageRefill_5042", helper.createRefill("SocialPackageRefill_5042", "5042", 1, "1", CurrencyCode.PHP, new String [] {"2322"}));
		lookupResourcesByName.put("TimerOffer_2325", helper.createTimerProfile("TimerOffer_2325",2325));
		lookupResourcesByName.put("UnliPackage15Refill_5043", helper.createRefill("UnliPackage15Refill_5043", "5043", 1, "1", CurrencyCode.PHP, new String [] {"2325"}));
		lookupResourcesByName.put("UnliPackage30Refill_5044", helper.createRefill("UnliPackage30Refill_5044", "5044", 1, "1", CurrencyCode.PHP, new String [] {"2325"}));
		lookupResourcesByName.put("TimerOffer_2323", helper.createTimerProfile("TimerOffer_2323",2323));
		lookupResourcesByName.put("SpeedBoostRefill_5045", helper.createRefill("SpeedBoostRefill_5045", "5045", 1, "1", CurrencyCode.PHP, new String [] {"2323"}));
		lookupResourcesByName.put("UnliOperaMini7Refill_5033", helper.createRefill("UnliOperaMini7Refill_5033", "5033", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("UnliOperaMini15Refill_5034", helper.createRefill("UnliOperaMini15Refill_5034", "5034", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("UnliOperaMini30Refill_5035", helper.createRefill("UnliOperaMini30Refill_5035", "5035", 1, "1", CurrencyCode.PHP, new String [] {"2303"}));
		lookupResourcesByName.put("TimerOffer_2318", helper.createTimerProfile("TimerOffer_2318",2318));
		lookupResourcesByName.put("UnliNokiaXpress1DayRefill_5049", helper.createRefill("UnliNokiaXpress1DayRefill_5049", "5049", 1, "1", CurrencyCode.PHP, new String [] {"2318"}));
		lookupResourcesByName.put("UnliNokiaXpress7Refill_5050", helper.createRefill("UnliNokiaXpress7Refill_5050", "5050", 1, "1", CurrencyCode.PHP, new String [] {"2318"}));
		lookupResourcesByName.put("UnliNokiaXpress15Refill_5051", helper.createRefill("UnliNokiaXpress15Refill_5051", "5051", 1, "1", CurrencyCode.PHP, new String [] {"2318"}));
		lookupResourcesByName.put("UnliNokiaXpress30Refill_5052", helper.createRefill("UnliNokiaXpress30Refill_5052", "5052", 1, "1", CurrencyCode.PHP, new String [] {"2318"}));
		lookupResourcesByName.put("UnliSurf299Refill_5002", helper.createRefill("UnliSurf299Refill_5002", "5002", 1, "1", CurrencyCode.PHP, new String [] {"2309"}));
		lookupResourcesByName.put("UnliSurf999Refill_5003", helper.createRefill("UnliSurf999Refill_5003", "5003", 1, "1", CurrencyCode.PHP, new String [] {"2309"}));
		lookupResourcesByName.put("BlackberryMessengerDaily10Refill_5023", helper.createRefill("BlackberryMessengerDaily10Refill_5023", "5023", 1, "1", CurrencyCode.PHP, new String [] {"1003","2313"}));
		lookupResourcesByName.put("TimerOffer_1155", helper.createTimerProfile("TimerOffer_1155",1155));
		lookupResourcesByName.put("MessagingUnliChatAllDedicatedAccount_155", helper.createDA ("MessagingUnliChatAllDedicatedAccount_155", 155, "1", "PHP"));
		lookupResourcesByName.put("MessagingUnliChatAllRefill_5056", helper.createRefill("MessagingUnliChatAllRefill_5056", "5056", 1, "1", CurrencyCode.PHP, new String [] {"1155"}));
		lookupResourcesByName.put("TimerOffer_2326", helper.createTimerProfile("TimerOffer_2326",2326));
		lookupResourcesByName.put("BlaastPackageRefill_5057", helper.createRefill("BlaastPackageRefill_5057", "5057", 1, "1", CurrencyCode.PHP, new String [] {"2326"}));
		lookupResourcesByName.put("EloadPromo5000DedicatedAccount_1", helper.createDA ("EloadPromo5000DedicatedAccount_1", 1, "500000", "PHP"));
		lookupResourcesByName.put("EloadPromo5000Refill_VS01", helper.createRefill("EloadPromo5000Refill_VS01", "VS01", 1, "500000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("EloadPromo10000DedicatedAccount_1", helper.createDA ("EloadPromo10000DedicatedAccount_1", 1, "1000000", "PHP"));
		lookupResourcesByName.put("EloadPromo10000Refill_VS01", helper.createRefill("EloadPromo10000Refill_VS01", "VS01", 1, "1000000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("KaPartnerRewards01DedicatedAccount_52", helper.createDA ("KaPartnerRewards01DedicatedAccount_52", 52, "30000", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards01Refill_VS01", helper.createRefill("KaPartnerRewards01Refill_VS01", "VS01", 1, "30000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("KaPartnerRewards10DedicatedAccount_1", helper.createDA ("KaPartnerRewards10DedicatedAccount_1", 1, "2000", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards10Refill_VS01", helper.createRefill("KaPartnerRewards10Refill_VS01", "VS01", 1, "2000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("KaPartnerRewards11DedicatedAccount_54", helper.createDA ("KaPartnerRewards11DedicatedAccount_54", 54, "1500", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards11DedicatedAccount_3", helper.createDA ("KaPartnerRewards11DedicatedAccount_3", 3, "1500", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards11Refill_VS01", helper.createRefill("KaPartnerRewards11Refill_VS01", "VS01", 1, "1500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("KaPartnerRewards12DedicatedAccount_54", helper.createDA ("KaPartnerRewards12DedicatedAccount_54", 54, "1000", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards12DedicatedAccount_52", helper.createDA ("KaPartnerRewards12DedicatedAccount_52", 52, "1000", "PHP"));
		lookupResourcesByName.put("KaPartnerRewards12Refill_VS01", helper.createRefill("KaPartnerRewards12Refill_VS01", "VS01", 1, "1000", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("DBBuildingProfilingProgram01DedicatedAccount_52", helper.createDA ("DBBuildingProfilingProgram01DedicatedAccount_52", 52, "100", "PHP"));
		lookupResourcesByName.put("DBBuildingProfilingProgram01Refill_VS01", helper.createRefill("DBBuildingProfilingProgram01Refill_VS01", "VS01", 1, "100", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("UnileverReward01DedicatedAccount_52", helper.createDA ("UnileverReward01DedicatedAccount_52", 52, "2500", "PHP"));
		lookupResourcesByName.put("UnileverReward01Refill_VS01", helper.createRefill("UnileverReward01Refill_VS01", "VS01", 1, "2500", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("SmartLoadIOSSMSJapanDedicatedAccount_1", helper.createDA ("SmartLoadIOSSMSJapanDedicatedAccount_1", 1, "200", "PHP"));
		lookupResourcesByName.put("SmartLoadIOSSMSJapanDedicatedAccount_52", helper.createDA ("SmartLoadIOSSMSJapanDedicatedAccount_52", 52, "200", "PHP"));
		lookupResourcesByName.put("SmartLoadIOSSMSJapanRefill_VS01", helper.createRefill("SmartLoadIOSSMSJapanRefill_VS01", "VS01", 1, "200", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("SMARTLoad1DedicatedAccount_1", helper.createDA ("SMARTLoad1DedicatedAccount_1", 1, "100", "PHP"));
		lookupResourcesByName.put("SMARTLoad1Refill_L001", helper.createRefill("SMARTLoad1Refill_L001", "L001", 1, "100", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("BucketIM6VolFbcDedicatedAccount_155", helper.createDA ("BucketIM6VolFbcDedicatedAccount_155", 155, "", "PHP"));
		lookupResourcesByName.put("BucketMocTriDedicatedAccount_5", helper.createDA ("BucketMocTriDedicatedAccount_5", 5, "", "PHP"));
		lookupResourcesByName.put("TimerOffer_1013", helper.createTimerProfile("TimerOffer_1013",1013));
		lookupResourcesByName.put("BucketArawMocDedicatedAccount_13", helper.createDA ("BucketArawMocDedicatedAccount_13", 13, "", "PHP"));
		lookupResourcesByName.put("AlkansyaPasaTropaDedicatedAccount_1", helper.createDA ("AlkansyaPasaTropaDedicatedAccount_1", 1, "", "PHP"));
		lookupResourcesByName.put("AlkansyaPasaTropaRefill_AL01", helper.createRefill("AlkansyaPasaTropaRefill_AL01", "AL01", 1, "", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("AlkansyaPasaTropaAlkansyaDedicatedAccount_1", helper.createDA ("AlkansyaPasaTropaAlkansyaDedicatedAccount_1", 1, "", "PHP"));
		lookupResourcesByName.put("AlkansyaPasaTropaAlkansyaRefill_AL01", helper.createRefill("AlkansyaPasaTropaAlkansyaRefill_AL01", "AL01", 1, "", CurrencyCode.PHP, new String [] {"1241"}));
		lookupResourcesByName.put("Regular10DedicatedAccount_1", helper.createDA ("Regular10DedicatedAccount_1", 1, "1000", "PHP"));
		lookupResourcesByName.put("Regular10Refill_L001", helper.createRefill("Regular10Refill_L001", "L001", 1, "1000", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("Regular5DedicatedAccount_1", helper.createDA ("Regular5DedicatedAccount_1", 1, "500", "PHP"));
		lookupResourcesByName.put("Regular5Refill_L001", helper.createRefill("Regular5Refill_L001", "L001", 1, "500", CurrencyCode.PHP, new String [] {"1054","1001"}));
		lookupResourcesByName.put("UnliTrio20PlusDedicatedAccount_5", helper.createDA ("UnliTrio20PlusDedicatedAccount_5", 5, "1", "PHP"));
		lookupResourcesByName.put("UnliTrio20PlusRefill_1071", helper.createRefill("UnliTrio20PlusRefill_1071", "1071", 1, "1", CurrencyCode.PHP, new String [] {"1005","2102","2301"}));
		lookupResourcesByName.put("UnliTx2All10Refill_1072", helper.createRefill("UnliTx2All10Refill_1072", "1072", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("TimerOffer_2002", helper.createTimerProfile("TimerOffer_2002",2002));
		lookupResourcesByName.put("UnliCombo10Refill_1073", helper.createRefill("UnliCombo10Refill_1073", "1073", 1, "1", CurrencyCode.PHP, new String [] {"2002","2102"}));
		lookupResourcesByName.put("UnliSingkoRefill_1074", helper.createRefill("UnliSingkoRefill_1074", "1074", 1, "1", CurrencyCode.PHP, new String [] {"2102"}));
		lookupResourcesByName.put("CallText10DedicatedAccount_5", helper.createDA ("CallText10DedicatedAccount_5", 5, "1", "PHP"));
		lookupResourcesByName.put("CallText10Refill_1075", helper.createRefill("CallText10Refill_1075", "1075", 1, "1", CurrencyCode.PHP, new String [] {"1005","2103"}));
		lookupResourcesByName.put("UnliAllText10Refill_1076", helper.createRefill("UnliAllText10Refill_1076", "1076", 1, "1", CurrencyCode.PHP, new String [] {"2103"}));
		lookupResourcesByName.put("Katok25FreebiePromoRefill_KF25", helper.createRefill("Katok25FreebiePromoRefill_KF25", "KF25", 1, "1", CurrencyCode.PHP, new String [] {"2101"}));
		
		// Non Commercial Offer
		
		//lookupResourcesByName.put("DnsUpdateAF_Cmd", helper.createDnsUpdateProfile("DnsUpdateAF_Cmd", "DNS Update Profile"));
		lookupResourcesByName.put("CreateSubscriber", helper.createCreateSubscriberProfile("CreateSubscriber", "Create a new Subscriber"));

		lookupResourcesByName.put("GetAccountDetails_Cmd", helper.createGetAccountDetailsProfile("GetAccountDetails_Cmd", "Get Account Details"));
		lookupResourcesByName.put("GetBalanceAndDate_Cmd", helper.createGetBalanceAndDateProfile("GetBalanceAndDate_Cmd", "Get Balance and Date"));
		
		lookupResourcesByName.put("UpdateSubscriberSegmentation_Cmd", helper.createGetAccountDetailsProfile("UpdateSubscriberSegmentation_Cmd", "Update Subscriber Segmentation"));

		lookupResourcesByName.put("PreloadUpdateServiceClass_Cmd", helper.createUpdateServiceClassProfile("PreloadUpdateServiceClass_Cmd", "Get Account Details"));
		lookupResourcesByName.put("PreloadAddPAM_Cmd", helper.createAddPeriodicAccountManagementProfile("PreloadAddPAM_Cmd", "Get Balance and Date"));
		lookupResourcesByName.put("PreloadUpdSubsSeg_Cmd", helper.createUpdateSubscriberSegmentationProfile("PreloadUpdSubsSeg_Cmd", "Get Account Details"));

		lookupResourcesByName.put("SIMShelfLifeExtUpdSubsSeg_Cmd", helper.createUpdateSubscriberSegmentationProfile("SIMShelfLifeExtUpdSubsSeg_Cmd", "SIM Shelf Life Ext Upd Subs Seg"));
		
		lookupResourcesByName.put("EntireDeleteDeleteDns_Cmd", helper.createDeleteDnsEntryProfile("EntireDeleteDeleteDns_Cmd", "Delete DNS Entry"));
		lookupResourcesByName.put("EntireDeleteDeleteSubs_Cmd", helper.createDeleteSuscriberProfile("EntireDeleteDeleteSubs_Cmd", "Delete Subscriber"));
		
		lookupResourcesByName.put("BarUpdSubsSeg_Cmd", helper.createUpdateSubscriberSegmentationProfile("BarUpdSubsSeg_Cmd", "Bar Update Subs Segmentation"));
		
		// ----------------------------------------------------------
		// --- add resources to all registry
		// ----------------------------------------------------------
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		for (Map.Entry<String, Resource> entry : lookupResourcesByName.entrySet()) {
			Resource resource = entry.getValue ();
			serviceRegistry.createResource(resource);
		}

		// ----------------------------------------------------------
		// --- creating all the offer products
		// ----------------------------------------------------------
		ArrayList<Resource>  offerResources;
		Offer  offer;
		HashSet<String> planCodes = new HashSet<String>();
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB461");
		planCodes.add("GAO15");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15Refill_1035"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15DedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EnhancedGA15Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN07");
		planCodes.add("YTN44");
		offerResources.add(lookupResourcesByName.get("EnhancedGA15RegionalRefill_1059"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("EnhancedGA15RegionalDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("EnhancedGA15RegionalDedicatedAccount_3"));
		offer = helper.createCommercialOffer("EnhancedGA15Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB462");
		planCodes.add("GAO20");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20Refill_1036"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20DedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EnhancedGA20Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN08");
		planCodes.add("YTN45");
		offerResources.add(lookupResourcesByName.get("EnhancedGA20RegionalRefill_1060"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("EnhancedGA20RegionalDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("EnhancedGA20RegionalDedicatedAccount_3"));
		offer = helper.createCommercialOffer("EnhancedGA20Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne15Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB526");
		planCodes.add("GAM15");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15RegionalRefill_1037"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15RegionalDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne15RegionalDedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne15Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne20Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB527");
		planCodes.add("GAM20");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20RegionalRefill_1038"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20RegionalDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne20RegionalDedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne20Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TokenGaanAllInOne55SMS
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB529");
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55SMSRefill_1061"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55SMSDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55SMSDedicatedAccount_52"));
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55SMSDedicatedAccount_3"));
		offer = helper.createCommercialOffer("TokenGaanAllInOne55SMS", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TokenGaanAllInOne55Voice
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB530");
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55VoiceRefill_1062"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55VoiceDedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("TokenGaanAllInOne55VoiceDedicatedAccount_3"));
		offer = helper.createCommercialOffer("TokenGaanAllInOne55Voice", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TokenGaanUnliTxtPlus55Voice
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB583");
		offerResources.add(lookupResourcesByName.get("TokenGaanUnliTxtPlus55VoiceRefill_1063"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TokenGaanUnliTxtPlus55VoiceDedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("TokenGaanUnliTxtPlus55Voice", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB457");
		planCodes.add("GA30");
		planCodes.add("SM152");
		planCodes.add("PlanGAIO30");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne30Refill_1064"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne30DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne30DedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne55
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB458");
		planCodes.add("GA55");
		planCodes.add("SM153");
		planCodes.add("PlanGAI055");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne55Refill_1065"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne55DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne55DedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne55", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanAllInOne99
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB459");
		planCodes.add("GA99");
		planCodes.add("SM154");
		planCodes.add("PlanGAIO99");
		offerResources.add(lookupResourcesByName.get("GaanAllInOne99Refill_1066"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne99DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanAllInOne99DedicatedAccount_3"));
		offer = helper.createCommercialOffer("GaanAllInOne99", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SangkatuTok15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB338");
		planCodes.add("SNGK4");
		offerResources.add(lookupResourcesByName.get("SangkatuTok15Refill_1039"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("SangkatuTok15DedicatedAccount_3"));
		offer = helper.createCommercialOffer("SangkatuTok15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SangkatuTok30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB339");
		planCodes.add("SNGK5");
		offerResources.add(lookupResourcesByName.get("SangkatuTok30Refill_1040"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("SangkatuTok30DedicatedAccount_3"));
		offer = helper.createCommercialOffer("SangkatuTok30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BonggaTxt5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB270");
		planCodes.add("TPW01");
		offerResources.add(lookupResourcesByName.get("BonggaTxt5Refill_1033"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("BonggaTxt5DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("BonggaTxt5DedicatedAccount_52"));
		offer = helper.createCommercialOffer("BonggaTxt5", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtAllUnliTropaCall20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB531");
		planCodes.add("CSW01");
		offerResources.add(lookupResourcesByName.get("UnliTxtAllUnliTropaCall20Refill_1023"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtAllUnliTropaCall20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliCombo10Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UCT37");
		planCodes.add("TUAS3");
		offerResources.add(lookupResourcesByName.get("UnliCombo10RegionalRefill_1027"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliCombo10Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=RegionalCombo10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB549");
		planCodes.add("SHP10");
		offerResources.add(lookupResourcesByName.get("RegionalCombo10Refill_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("RegionalCombo10DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("RegionalCombo10DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("RegionalCombo10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSingkoRegional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF170");
		planCodes.add("TUAS2");
		offerResources.add(lookupResourcesByName.get("UnliSingkoRegionalRefill_1028"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSingkoRegional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtPlus10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB271");
		planCodes.add("TPW02");
		offerResources.add(lookupResourcesByName.get("UnliTxtPlus10Refill_1034"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("UnliTxtPlus10DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtPlus10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt15Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF166");
		planCodes.add("UPK05");
		offerResources.add(lookupResourcesByName.get("UnliTxt15RegionalRefill_1014"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt15Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt5For12HrsRegional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF165");
		planCodes.add("UPK04");
		offerResources.add(lookupResourcesByName.get("UnliTxt5For12HrsRegionalRefill_1015"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt5For12HrsRegional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt20Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF235");
		planCodes.add("UNL20");
		offerResources.add(lookupResourcesByName.get("UnliTxt20RegionalRefill_1016"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt20Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtTrioCombo10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN03");
		planCodes.add("YTN40");
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo10Refill_1055"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo10DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtTrioCombo10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtTrioCombo15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN04");
		planCodes.add("YTN41");
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo15Refill_1056"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo15DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtTrioCombo15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtTrioCombo20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN05");
		planCodes.add("YTN42");
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo20Refill_1057"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo20DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtTrioCombo20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxtTrioCombo30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN06");
		planCodes.add("YTN43");
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo30Refill_1058"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("UnliTxtTrioCombo30DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxtTrioCombo30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PaTokOTex10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB95");
		planCodes.add("FMX1");
		offerResources.add(lookupResourcesByName.get("PaTokOTex10Refill_1019"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1101"));
		offerResources.add(lookupResourcesByName.get("PaTokOTex10DedicatedAccount_101"));
		offer = helper.createCommercialOffer("PaTokOTex10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PaTokOTex15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB460");
		planCodes.add("PTT15");
		offerResources.add(lookupResourcesByName.get("PaTokOTex15Refill_1020"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1101"));
		offerResources.add(lookupResourcesByName.get("PaTokOTex15DedicatedAccount_101"));
		offer = helper.createCommercialOffer("PaTokOTex15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PaTokOTex20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB96");
		planCodes.add("FMX2");
		offerResources.add(lookupResourcesByName.get("PaTokOTex20Refill_1021"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1101"));
		offerResources.add(lookupResourcesByName.get("PaTokOTex20DedicatedAccount_101"));
		offer = helper.createCommercialOffer("PaTokOTex20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PaTokOTex30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB97");
		planCodes.add("FMX3");
		offerResources.add(lookupResourcesByName.get("PaTokOTex30Refill_1022"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1101"));
		offerResources.add(lookupResourcesByName.get("PaTokOTex30DedicatedAccount_101"));
		offer = helper.createCommercialOffer("PaTokOTex30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EmergencyLoadSMS
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PBB");
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSRefill_EL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSDedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSDedicatedAccount_52"));
		offer = helper.createCommercialOffer("EmergencyLoadSMS", "", "PHP", 400, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EmergencyLoadVoice
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("CBBV1");
		offerResources.add(lookupResourcesByName.get("EmergencyLoadVoiceRefill_EL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadVoiceDedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadVoiceDedicatedAccount_3"));
		offer = helper.createCommercialOffer("EmergencyLoadVoice", "", "PHP", 650, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EmergencyLoadSMSAllNetSMS
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PBB3");
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSAllNetSMSRefill_EL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSAllNetSMSDedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("EmergencyLoadSMSAllNetSMSDedicatedAccount_54"));
		offer = helper.createCommercialOffer("EmergencyLoadSMSAllNetSMS", "", "PHP", 500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CorporateQuickLoad100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("B1");
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad100Refill_CQ01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad100DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad100DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CorporateQuickLoad100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CorporateQuickLoad200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("XA");
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad200Refill_CQ01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad200DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad200DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CorporateQuickLoad200", "", "PHP", 20000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CorporateQuickLoad300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("X6");
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad300Refill_CQ01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad300DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad300DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CorporateQuickLoad300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CorporateQuickLoad500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("X7");
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad500Refill_CQ01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad500DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad500DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CorporateQuickLoad500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CorporateQuickLoad1000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("X8");
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad1000Refill_CQ01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad1000DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CorporateQuickLoad1000DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CorporateQuickLoad1000", "", "PHP", 100000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR007");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad30Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad30DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad30", "", "PHP", 3000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR008");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad60Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad60DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad60", "", "PHP", 6000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad115
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR002");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad115Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad115DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad115", "", "PHP", 11500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR004");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad300Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad300DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad300DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR005");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad500Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad500DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad500DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad1000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR006");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad1000Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad1000DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad1000DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad1000", "", "PHP", 100000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR003");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad200Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad200DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad200DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad200", "", "PHP", 20000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PresyongPinoyLoad100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BR001");
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad100Refill_PP01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PresyongPinoyLoad100DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PresyongPinoyLoad100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TokTok15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB603");
		offerResources.add(lookupResourcesByName.get("TokTok15Refill_1042"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1012"));
		offerResources.add(lookupResourcesByName.get("TokTok15DedicatedAccount_12"));
		offer = helper.createCommercialOffer("TokTok15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TokTok10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB602");
		offerResources.add(lookupResourcesByName.get("TokTok10Refill_1043"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1012"));
		offerResources.add(lookupResourcesByName.get("TokTok10DedicatedAccount_12"));
		offer = helper.createCommercialOffer("TokTok10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SangkatuTex30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB289");
		planCodes.add("SNGK2");
		offerResources.add(lookupResourcesByName.get("SangkatuTex30Refill_1026"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("SangkatuTex30DedicatedAccount_52"));
		offer = helper.createCommercialOffer("SangkatuTex30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SangkatuTex15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB288");
		planCodes.add("SNGK1");
		offerResources.add(lookupResourcesByName.get("SangkatuTex15Refill_1025"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("SangkatuTex15DedicatedAccount_52"));
		offer = helper.createCommercialOffer("SangkatuTex15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Takatak3
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("TK1");
		offerResources.add(lookupResourcesByName.get("Takatak3Refill_TK01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Takatak3DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Takatak3", "", "PHP", 300, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Takatak6
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("TK2");
		offerResources.add(lookupResourcesByName.get("Takatak6Refill_TK01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Takatak6DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Takatak6", "", "PHP", 600, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Takatak12
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("TK3");
		offerResources.add(lookupResourcesByName.get("Takatak12Refill_TK01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Takatak12DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Takatak12", "", "PHP", 1200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("E");
		planCodes.add("T5");
		planCodes.add("SM8");
		planCodes.add("SM17");
		offerResources.add(lookupResourcesByName.get("SMARTLoad500Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad500DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad500DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad1000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("F");
		planCodes.add("T6");
		planCodes.add("SM9");
		planCodes.add("SM18");
		offerResources.add(lookupResourcesByName.get("SMARTLoad1000Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad1000DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad1000DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad1000", "", "PHP", 100000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T7");
		planCodes.add("G");
		planCodes.add("SM6");
		planCodes.add("SM15");
		offerResources.add(lookupResourcesByName.get("SMARTLoad200Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad200DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad200DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad200", "", "PHP", 20000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T4");
		planCodes.add("SM7");
		planCodes.add("SM16");
		planCodes.add("D");
		offerResources.add(lookupResourcesByName.get("SMARTLoad300Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad300DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad300DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("U");
		planCodes.add("T17");
		planCodes.add("SM1");
		planCodes.add("SM11");
		offerResources.add(lookupResourcesByName.get("SMARTLoad15Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad15DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad15", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("X1");
		planCodes.add("T19");
		planCodes.add("TKT01");
		planCodes.add("SM3");
		planCodes.add("SM13");
		offerResources.add(lookupResourcesByName.get("SMARTLoad50Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad50DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad50", "", "PHP", 5000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T20");
		planCodes.add("X2");
		planCodes.add("SM5");
		planCodes.add("SM14");
		offerResources.add(lookupResourcesByName.get("SMARTLoad100Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad100DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad250
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RA001");
		offerResources.add(lookupResourcesByName.get("SMARTLoad250Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad250DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad250", "", "PHP", 25000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BD5");
		planCodes.add("T10");
		offerResources.add(lookupResourcesByName.get("SMARTLoad20Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad20DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad20", "", "PHP", 2000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CTCLoad100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("100");
		offerResources.add(lookupResourcesByName.get("CTCLoad100Refill_CTC1"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("CTCLoad100DedicatedAccount_1"));
		offer = helper.createCommercialOffer("CTCLoad100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CTCLoad300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("300");
		offerResources.add(lookupResourcesByName.get("CTCLoad300Refill_CTC1"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CTCLoad300DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CTCLoad300DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CTCLoad300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CTCLoad500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("500");
		offerResources.add(lookupResourcesByName.get("CTCLoad500Refill_CTC1"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("CTCLoad500DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("CTCLoad500DedicatedAccount_54"));
		offer = helper.createCommercialOffer("CTCLoad500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CTCLoad1000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("1000");
		offerResources.add(lookupResourcesByName.get("CTCLoad1000Refill_CTC1"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("CTCLoad1000DedicatedAccount_1"));
		offer = helper.createCommercialOffer("CTCLoad1000", "", "PHP", 100000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt2All15Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB328");
		planCodes.add("PWIL3");
		offerResources.add(lookupResourcesByName.get("UnliTxt2All15RegionalRefill_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt2All15Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliAllText10Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB550");
		planCodes.add("TXA10");
		offerResources.add(lookupResourcesByName.get("UnliAllText10RegionalRefill_1053"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("UnliAllText10RegionalDedicatedAccount_54"));
		offer = helper.createCommercialOffer("UnliAllText10Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanUnliTxtPlus15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB410");
		planCodes.add("GTX15");
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus15Refill_1002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus15DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("GaanUnliTxtPlus15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanUnliTxtPlus20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB46");
		planCodes.add("GT20T");
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus20Refill_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus20DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("GaanUnliTxtPlus20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanUnliTxtPlus30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB47");
		planCodes.add("GT30T");
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus30Refill_1004"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus30DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("GaanUnliTxtPlus30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTalkPlus20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UCT17");
		planCodes.add("UP20");
		offerResources.add(lookupResourcesByName.get("UnliTalkPlus20Refill_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTalkPlus20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTalkPlus100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UCT4");
		planCodes.add("T22");
		offerResources.add(lookupResourcesByName.get("UnliTalkPlus100Refill_1006"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTalkPlus100", "", "PHP", 10, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTextExtra30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB510");
		planCodes.add("PJST1");
		offerResources.add(lookupResourcesByName.get("UnliTextExtra30Refill_1007"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("UnliTextExtra30DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTextExtra30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanTxt10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB60");
		planCodes.add("Q");
		planCodes.add("GTX10");
		offerResources.add(lookupResourcesByName.get("GaanTxt10Refill_1008"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("GaanTxt10DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("GaanTxt10DedicatedAccount_52"));
		offer = helper.createCommercialOffer("GaanTxt10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanTxt20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB323");
		planCodes.add("X5");
		planCodes.add("GTX20");
		offerResources.add(lookupResourcesByName.get("GaanTxt20Refill_1009"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("GaanTxt20DedicatedAccount_54"));
		offer = helper.createCommercialOffer("GaanTxt20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF57");
		offerResources.add(lookupResourcesByName.get("UnliTxt10Refill_1010"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt150
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF137");
		planCodes.add("PWIL1");
		offerResources.add(lookupResourcesByName.get("UnliTxt150Refill_1011"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt150", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF58");
		offerResources.add(lookupResourcesByName.get("UnliTxt5Refill_1012"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt5", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt2All20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB511");
		planCodes.add("PJST2");
		offerResources.add(lookupResourcesByName.get("UnliTxt2All20Refill_1013"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt2All20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Regular60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("B");
		planCodes.add("T2");
		offerResources.add(lookupResourcesByName.get("Regular60Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Regular60DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Regular60", "", "PHP", 6000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Extra115
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("C");
		planCodes.add("T3");
		offerResources.add(lookupResourcesByName.get("Extra115Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Extra115DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Extra115", "", "PHP", 11500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Economy30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("A");
		planCodes.add("T1");
		planCodes.add("SM2");
		planCodes.add("SM12");
		offerResources.add(lookupResourcesByName.get("Economy30Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Economy30DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Economy30", "", "PHP", 3000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AllText10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("J");
		offerResources.add(lookupResourcesByName.get("AllText10Refill_1017"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("AllText10DedicatedAccount_54"));
		offer = helper.createCommercialOffer("AllText10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AllText20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("K");
		offerResources.add(lookupResourcesByName.get("AllText20Refill_1018"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("AllText20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EnhancedAllText20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("K");
		planCodes.add("SM10");
		planCodes.add("PlanA20");
		offerResources.add(lookupResourcesByName.get("EnhancedAllText20Refill_1018"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("EnhancedAllText20DedicatedAccount_5"));
		offerResources.add(lookupResourcesByName.get("EnhancedAllText20DedicatedAccount_141"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("EnhancedAllText20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EnhancedAllText30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALT30");
		planCodes.add("PlanA30");
		offerResources.add(lookupResourcesByName.get("EnhancedAllText30Refill_1069"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("EnhancedAllText30DedicatedAccount_5"));
		offerResources.add(lookupResourcesByName.get("EnhancedAllText30DedicatedAccount_141"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("EnhancedAllText30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=GaanUnliTxtPlus15Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB537");
		planCodes.add("GAU15");
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus15RegionalRefill_1024"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("GaanUnliTxtPlus15RegionalDedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("GaanUnliTxtPlus15Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Txt2All5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB325");
		planCodes.add("OUT05");
		offerResources.add(lookupResourcesByName.get("Txt2All5Refill_1041"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("Txt2All5DedicatedAccount_54"));
		offer = helper.createCommercialOffer("Txt2All5", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PantawidLoad1Bucket
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB501");
		offerResources.add(lookupResourcesByName.get("PantawidLoad1BucketRefill_1045"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PantawidLoad1BucketDedicatedAccount_52"));
		offer = helper.createCommercialOffer("PantawidLoad1Bucket", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PantawidLoad1ELoad
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RPL01");
		offerResources.add(lookupResourcesByName.get("PantawidLoad1ELoadRefill_1070"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PantawidLoad1ELoadDedicatedAccount_1"));
		offer = helper.createCommercialOffer("PantawidLoad1ELoad", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTxt2AllPlus10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB536");
		offerResources.add(lookupResourcesByName.get("UnliTxt2AllPlus10Refill_1046"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("UnliTxt2AllPlus10DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTxt2AllPlus10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliText2All300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB339");
		offerResources.add(lookupResourcesByName.get("UnliText2All300Refill_1047"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliText2All300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ArawArawText20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB281");
		offerResources.add(lookupResourcesByName.get("ArawArawText20Refill_1044"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1060"));
		offerResources.add(lookupResourcesByName.get("ArawArawText20DedicatedAccount_60"));
		offer = helper.createCommercialOffer("ArawArawText20", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ArawArawLoad30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GR028");
		planCodes.add("ZTN10");
		offerResources.add(lookupResourcesByName.get("ArawArawLoad30Refill_1049"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1143"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("ArawArawLoad30DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("ArawArawLoad30DedicatedAccount_143"));
		offer = helper.createCommercialOffer("ArawArawLoad30", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ArawArawLoad60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GR029");
		planCodes.add("ZTN11");
		offerResources.add(lookupResourcesByName.get("ArawArawLoad60Refill_1050"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1143"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("ArawArawLoad60DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("ArawArawLoad60DedicatedAccount_143"));
		offer = helper.createCommercialOffer("ArawArawLoad60", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ICHamp
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("CMP01");
		offerResources.add(lookupResourcesByName.get("ICHampRefill_1051"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1151"));
		offerResources.add(lookupResourcesByName.get("ICHampDedicatedAccount_52"));
		offerResources.add(lookupResourcesByName.get("ICHampDedicatedAccount_151"));
		offer = helper.createCommercialOffer("ICHamp", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KamusText20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB353");
		offerResources.add(lookupResourcesByName.get("KamusText20Refill_3001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1059"));
		offerResources.add(lookupResourcesByName.get("KamusText20DedicatedAccount_59"));
		offer = helper.createCommercialOffer("KamusText20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=FlexiCallandTextAbroad30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB354");
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad30Refill_3002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1102"));
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad30DedicatedAccount_102"));
		offer = helper.createCommercialOffer("FlexiCallandTextAbroad30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=FlexiCallandTextAbroad50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB355");
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad50Refill_3003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1103"));
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad50DedicatedAccount_103"));
		offer = helper.createCommercialOffer("FlexiCallandTextAbroad50", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TalkALot300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB352");
		offerResources.add(lookupResourcesByName.get("TalkALot300Refill_3004"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1008"));
		offerResources.add(lookupResourcesByName.get("TalkALot300DedicatedAccount_8"));
		offer = helper.createCommercialOffer("TalkALot300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextALot35
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB350");
		offerResources.add(lookupResourcesByName.get("TextALot35Refill_3005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1057"));
		offerResources.add(lookupResourcesByName.get("TextALot35DedicatedAccount_52"));
		offerResources.add(lookupResourcesByName.get("TextALot35DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("TextALot35DedicatedAccount_57"));
		offer = helper.createCommercialOffer("TextALot35", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextALot60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB351");
		offerResources.add(lookupResourcesByName.get("TextALot60Refill_3006"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1057"));
		offerResources.add(lookupResourcesByName.get("TextALot60DedicatedAccount_52"));
		offerResources.add(lookupResourcesByName.get("TextALot60DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("TextALot60DedicatedAccount_57"));
		offer = helper.createCommercialOffer("TextALot60", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextTipid100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB313");
		offerResources.add(lookupResourcesByName.get("TextTipid100Refill_3007"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1056"));
		offerResources.add(lookupResourcesByName.get("TextTipid100DedicatedAccount_56"));
		offer = helper.createCommercialOffer("TextTipid100", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextTipid150
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB314");
		offerResources.add(lookupResourcesByName.get("TextTipid150Refill_3008"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1056"));
		offerResources.add(lookupResourcesByName.get("TextTipid150DedicatedAccount_56"));
		offer = helper.createCommercialOffer("TextTipid150", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextTipid200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB315");
		offerResources.add(lookupResourcesByName.get("TextTipid200Refill_3009"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1056"));
		offerResources.add(lookupResourcesByName.get("TextTipid200DedicatedAccount_56"));
		offer = helper.createCommercialOffer("TextTipid200", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TextTipid300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB316");
		offerResources.add(lookupResourcesByName.get("TextTipid300Refill_3010"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1056"));
		offerResources.add(lookupResourcesByName.get("TextTipid300DedicatedAccount_56"));
		offer = helper.createCommercialOffer("TextTipid300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB374");
		planCodes.add("SB375");
		planCodes.add("SB376");
		planCodes.add("SB377");
		planCodes.add("SB378");
		planCodes.add("SB379");
		planCodes.add("SB380");
		planCodes.add("SB381");
		planCodes.add("SB382");
		planCodes.add("SB383");
		planCodes.add("SB384");
		planCodes.add("SB385");
		offerResources.add(lookupResourcesByName.get("UnliSurf500Refill_3011"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2314"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf500", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TalkTipid100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB341");
		planCodes.add("SB345");
		offerResources.add(lookupResourcesByName.get("TalkTipid100Refill_3012"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1014"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1058"));
		offerResources.add(lookupResourcesByName.get("TalkTipid100DedicatedAccount_14"));
		offerResources.add(lookupResourcesByName.get("TalkTipid100DedicatedAccount_58"));
		offer = helper.createCommercialOffer("TalkTipid100", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TalkTipid200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB342");
		planCodes.add("SB346");
		offerResources.add(lookupResourcesByName.get("TalkTipid200Refill_3013"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1014"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1058"));
		offerResources.add(lookupResourcesByName.get("TalkTipid200DedicatedAccount_14"));
		offerResources.add(lookupResourcesByName.get("TalkTipid200DedicatedAccount_58"));
		offer = helper.createCommercialOffer("TalkTipid200", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB80");
		offerResources.add(lookupResourcesByName.get("UnliSurf50Refill_5001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf50", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB81");
		offerResources.add(lookupResourcesByName.get("UnliSurf300Refill_5002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf1200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB82");
		offerResources.add(lookupResourcesByName.get("UnliSurf1200Refill_5003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf1200", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SurfNet30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB185");
		offerResources.add(lookupResourcesByName.get("SurfNet30Refill_5004"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2315"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("SurfNet30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=TNTPaidSampler
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB606");
		offerResources.add(lookupResourcesByName.get("TNTPaidSamplerRefill_5005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1142"));
		offerResources.add(lookupResourcesByName.get("TNTPaidSamplerDedicatedAccount_142"));
		offer = helper.createCommercialOffer("TNTPaidSampler", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB397");
		offerResources.add(lookupResourcesByName.get("AlwaysOn10Refill_5006"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn10DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB521");
		offerResources.add(lookupResourcesByName.get("AlwaysOn20Refill_5007"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn20DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB522");
		offerResources.add(lookupResourcesByName.get("AlwaysOn30Refill_5008"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn30DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn99
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB497");
		offerResources.add(lookupResourcesByName.get("AlwaysOn99Refill_5009"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn99DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn99", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn199
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB580");
		offerResources.add(lookupResourcesByName.get("AlwaysOn199Refill_5010"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn199DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn199", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn299
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB581");
		offerResources.add(lookupResourcesByName.get("AlwaysOn299Refill_5011"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn299DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn299", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn499
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB525");
		offerResources.add(lookupResourcesByName.get("AlwaysOn499Refill_5012"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn499DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn499", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlwaysOn995
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB404");
		offerResources.add(lookupResourcesByName.get("AlwaysOn995Refill_5013"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1141"));
		offerResources.add(lookupResourcesByName.get("AlwaysOn995DedicatedAccount_141"));
		offer = helper.createCommercialOffer("AlwaysOn995", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Facebook10Panther
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GW698");
		offerResources.add(lookupResourcesByName.get("Facebook10PantherRefill_5014"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2301"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2316"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("Facebook10Panther", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Yahoo15Panther
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GW719");
		offerResources.add(lookupResourcesByName.get("Yahoo15PantherRefill_5015"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2302"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2317"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("Yahoo15Panther", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryDaily50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB65");
		offerResources.add(lookupResourcesByName.get("BlackberryDaily50Refill_5016"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryDaily50", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryWeekly300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB66");
		offerResources.add(lookupResourcesByName.get("BlackberryWeekly300Refill_5017"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryWeekly300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryMonthly599
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB108");
		offerResources.add(lookupResourcesByName.get("BlackberryMonthly599Refill_5018"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryMonthly599", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryLiteEmailDaily35
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB259");
		offerResources.add(lookupResourcesByName.get("BlackberryLiteEmailDaily35Refill_5019"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryLiteEmailDaily35", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryLiteEmailMonthly299
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB258");
		offerResources.add(lookupResourcesByName.get("BlackberryLiteEmailMonthly299Refill_5020"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryLiteEmailMonthly299", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberrySocialDaily35
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB257");
		offerResources.add(lookupResourcesByName.get("BlackberrySocialDaily35Refill_5021"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberrySocialDaily35", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberrySocialMonthly299
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB256");
		offerResources.add(lookupResourcesByName.get("BlackberrySocialMonthly299Refill_5022"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberrySocialMonthly299", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryMessengerDaily15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB559");
		offerResources.add(lookupResourcesByName.get("BlackberryMessengerDaily15Refill_5023"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("BlackberryMessengerDaily15DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryMessengerDaily15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryMessengerMonthly99
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB560");
		offerResources.add(lookupResourcesByName.get("BlackberryMessengerMonthly99Refill_5024"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("BlackberryMessengerMonthly99DedicatedAccount_3"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryMessengerMonthly99", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryMonthly599Microwarehouse
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BBP1");
		planCodes.add("BB1200F");
		offerResources.add(lookupResourcesByName.get("BlackberryMonthly599MicrowarehouseRefill_5025"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryMonthly599Microwarehouse", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryLiteEmailMonthly299Microwarehouse
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BBP3");
		planCodes.add("BB650L");
		offerResources.add(lookupResourcesByName.get("BlackberryLiteEmailMonthly299MicrowarehouseRefill_5026"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryLiteEmailMonthly299Microwarehouse", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberrySocialMonthly299Microwarehouse
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BBP2");
		planCodes.add("BBS650S");
		offerResources.add(lookupResourcesByName.get("BlackberrySocialMonthly299MicrowarehouseRefill_5027"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberrySocialMonthly299Microwarehouse", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SnapperDaily10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB457");
		planCodes.add("SB460");
		offerResources.add(lookupResourcesByName.get("SnapperDaily10Refill_5028"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2301"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("SnapperDaily10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=P1TNTMicrosachetFB
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB604");
		offerResources.add(lookupResourcesByName.get("P1TNTMicrosachetFBRefill_5029"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2301"));
		offer = helper.createCommercialOffer("P1TNTMicrosachetFB", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=P5TNTMicrosachetFB
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB605");
		offerResources.add(lookupResourcesByName.get("P5TNTMicrosachetFBRefill_5030"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2301"));
		offer = helper.createCommercialOffer("P5TNTMicrosachetFB", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=OperaSurf15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB597");
		offerResources.add(lookupResourcesByName.get("OperaSurf15Refill_5031"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("OperaSurf15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=OperaSurf60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SB598");
		offerResources.add(lookupResourcesByName.get("OperaSurf60Refill_5032"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("OperaSurf60", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=OperaSurf80
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA003");
		offerResources.add(lookupResourcesByName.get("OperaSurf80Refill_5033"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("OperaSurf80", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=OperaSurf160
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA004");
		offerResources.add(lookupResourcesByName.get("OperaSurf160Refill_5034"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("OperaSurf160", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=OperaSurf299
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA005");
		offerResources.add(lookupResourcesByName.get("OperaSurf299Refill_5035"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("OperaSurf299", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);

		// ----------------------------------------------------------
		// Offer.name=PasaloadP2
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("2");
		planCodes.add("O1");
		planCodes.add("W1");
		offerResources.add(lookupResourcesByName.get("PasaloadP2Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP2DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP2", "", "PHP", 200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("3");
		planCodes.add("W3");
		offerResources.add(lookupResourcesByName.get("PasaloadP10Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP10DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP10", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("20");
		planCodes.add("O5");
		planCodes.add("W5");
		offerResources.add(lookupResourcesByName.get("PasaloadP20Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP20DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP20", "", "PHP", 2000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP35
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("35T");
		planCodes.add("O8");
		planCodes.add("Y2");
		offerResources.add(lookupResourcesByName.get("PasaloadP35Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP35DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasaloadP35DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PasaloadP35", "", "PHP", 3500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("60");
		planCodes.add("O9");
		planCodes.add("W7");
		offerResources.add(lookupResourcesByName.get("PasaloadP60Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP60DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP60", "", "PHP", 6000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("2H");
		planCodes.add("2O");
		planCodes.add("W9");
		offerResources.add(lookupResourcesByName.get("PasaloadP200Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP200DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP200", "", "PHP", 20000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("1H");
		planCodes.add("W8");
		offerResources.add(lookupResourcesByName.get("PasaloadP100Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP100DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("O4");
		planCodes.add("W4");
		offerResources.add(lookupResourcesByName.get("PasaloadP15Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP15DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP15", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("O7");
		planCodes.add("W6");
		offerResources.add(lookupResourcesByName.get("PasaloadP30Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP30DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP30", "", "PHP", 3000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("O2");
		planCodes.add("W2");
		offerResources.add(lookupResourcesByName.get("PasaloadP5Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP5DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP5", "", "PHP", 500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK01");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad15Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad15DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad15", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL1");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad30Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad30DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad30", "", "PHP", 3000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL2");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad50Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad50DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad50", "", "PHP", 5000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL3");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad100Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad100DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad150
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL4");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad150Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad150DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad150", "", "PHP", 15000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL5");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad300Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad300DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadWalletLoad500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("WL6");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad500Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadWalletLoad500DedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadWalletLoad500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP1
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK02");
		offerResources.add(lookupResourcesByName.get("PasariliP1Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP1DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP1", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP1.50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK03");
		offerResources.add(lookupResourcesByName.get("PasariliP1.50Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP1.50DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP1.50", "", "PHP", 150, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP2
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK04");
		offerResources.add(lookupResourcesByName.get("PasariliP2Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP2DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP2", "", "PHP", 200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP2.50
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK05");
		offerResources.add(lookupResourcesByName.get("PasariliP2.50Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP2.50DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP2.50", "", "PHP", 250, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP3
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK06");
		offerResources.add(lookupResourcesByName.get("PasariliP3Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP3DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP3", "", "PHP", 300, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GL10");
		offerResources.add(lookupResourcesByName.get("PasariliP10Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP10DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP10", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T17");
		offerResources.add(lookupResourcesByName.get("PasariliP15Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP15DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP15", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T1");
		offerResources.add(lookupResourcesByName.get("PasariliP30Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP30DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP30", "", "PHP", 3000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP60
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T2");
		offerResources.add(lookupResourcesByName.get("PasariliP60Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP60DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP60", "", "PHP", 6000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T20");
		offerResources.add(lookupResourcesByName.get("PasariliP100Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP100DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP100", "", "PHP", 10000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP115
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T3");
		offerResources.add(lookupResourcesByName.get("PasariliP115Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasariliP115DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasariliP115", "", "PHP", 11500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP200
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T7");
		offerResources.add(lookupResourcesByName.get("PasariliP200Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PasariliP200DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasariliP200DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PasariliP200", "", "PHP", 20000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T4");
		offerResources.add(lookupResourcesByName.get("PasariliP300Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PasariliP300DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasariliP300DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PasariliP300", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliP500
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T5");
		offerResources.add(lookupResourcesByName.get("PasariliP500Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("PasariliP500DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasariliP500DedicatedAccount_54"));
		offer = helper.createCommercialOffer("PasariliP500", "", "PHP", 50000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP1
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK07");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP1Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP1DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP1", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP2
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK08");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP2Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP2DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP2", "", "PHP", 200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP3
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK09");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP3Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP3DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP3", "", "PHP", 300, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP4
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK10");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP4Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP4DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP4", "", "PHP", 400, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK11");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP5Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP5DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP5", "", "PHP", 500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP6
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK12");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP6Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP6DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP6", "", "PHP", 600, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP7
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK13");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP7Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP7DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP7", "", "PHP", 700, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP8
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK14");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP8Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP8DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP8", "", "PHP", 800, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliAlkansyaInstantTextP9
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK15");
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP9Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliAlkansyaInstantTextP9DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliAlkansyaInstantTextP9", "", "PHP", 900, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliUnliTxt5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK16");
		offerResources.add(lookupResourcesByName.get("PasariliUnliTxt5Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("PasariliUnliTxt5", "", "PHP", 500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliUnliTxt10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK17");
		offerResources.add(lookupResourcesByName.get("PasariliUnliTxt10Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("PasariliUnliTxt10", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliPatok10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ALK18");
		offerResources.add(lookupResourcesByName.get("PasariliPatok10Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1101"));
		offerResources.add(lookupResourcesByName.get("PasariliPatok10DedicatedAccount_101"));
		offer = helper.createCommercialOffer("PasariliPatok10", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliUpsize35
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T11");
		offerResources.add(lookupResourcesByName.get("PasariliUpsize35Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliUpsize35DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasariliUpsize35DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliUpsize35", "", "PHP", 3500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasariliUpsize20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("T13");
		offerResources.add(lookupResourcesByName.get("PasariliUpsize20Refill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("PasariliUpsize20DedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("PasariliUpsize20DedicatedAccount_52"));
		offer = helper.createCommercialOffer("PasariliUpsize20", "", "PHP", 2000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaLoadToMeIpon
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("FD1");
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadToMeIponRefill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadToMeIponDedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("AlkansyaLoadToMeIponDedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaLoadToMeIpon", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaActivateFirstTime
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("YTN11");
		offerResources.add(lookupResourcesByName.get("AlkansyaActivateFirstTimeRefill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1241"));
		offerResources.add(lookupResourcesByName.get("AlkansyaActivateFirstTimeDedicatedAccount_241"));
		offer = helper.createCommercialOffer("AlkansyaActivateFirstTime", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);

		// ----------------------------------------------------------
		// Offer.name=Katok10Promo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB318");
		offerResources.add(lookupResourcesByName.get("Katok10PromoRefill_K10"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2015"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("Katok10Promo", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Katok10FreebiePromo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PK10");
		offerResources.add(lookupResourcesByName.get("Katok10FreebiePromoRefill_{UpdateOffer}"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2019"));
		offer = helper.createCommercialOffer("Katok10FreebiePromo", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Katok15Promo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB319");
		offerResources.add(lookupResourcesByName.get("Katok15PromoRefill_K15"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2016"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("Katok15Promo", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KatokAttTex25Promo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB506");
		offerResources.add(lookupResourcesByName.get("KatokAttTex25PromoRefill_KT25"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2017"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("KatokAttTex25Promo", "", "PHP", 2500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KatokAttTex35Promo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB507");
		offerResources.add(lookupResourcesByName.get("KatokAttTex35PromoRefill_KT35"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2018"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("KatokAttTex35Promo", "", "PHP", 3500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ProjectLucky13
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PRL13");
		planCodes.add("ZTN09");
		offerResources.add(lookupResourcesByName.get("ProjectLucky13Refill_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("ProjectLucky13DedicatedAccount_52"));
		offer = helper.createCommercialOffer("ProjectLucky13", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Trinet300
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GR027");
		offerResources.add(lookupResourcesByName.get("Trinet300Refill_1067"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1041"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("Trinet300DedicatedAccount_5"));
		offerResources.add(lookupResourcesByName.get("Trinet300DedicatedAccount_141"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("Trinet300", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=IDDol20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB541");
		offerResources.add(lookupResourcesByName.get("IDDol20Refill_3014"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1008"));
		offerResources.add(lookupResourcesByName.get("IDDol20DedicatedAccount_8"));
		offer = helper.createCommercialOffer("IDDol20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=IDDol25
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB542");
		offerResources.add(lookupResourcesByName.get("IDDol25Refill_3015"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1008"));
		offerResources.add(lookupResourcesByName.get("IDDol25DedicatedAccount_8"));
		offer = helper.createCommercialOffer("IDDol25", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=IDDol40
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB543");
		offerResources.add(lookupResourcesByName.get("IDDol40Refill_3016"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1008"));
		offerResources.add(lookupResourcesByName.get("IDDol40DedicatedAccount_8"));
		offer = helper.createCommercialOffer("IDDol40", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=IDDol100
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB544");
		offerResources.add(lookupResourcesByName.get("IDDol100Refill_3017"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1008"));
		offerResources.add(lookupResourcesByName.get("IDDol100DedicatedAccount_8"));
		offer = helper.createCommercialOffer("IDDol100", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=IDDSale
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU16");
		offerResources.add(lookupResourcesByName.get("IDDSaleRefill_3018"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_8001"));
		offer = helper.createCommercialOffer("IDDSale", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EmailPackage5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU17");
		offerResources.add(lookupResourcesByName.get("EmailPackage5Refill_5036"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2319"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("EmailPackage5", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EmailPackage10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU18");
		offerResources.add(lookupResourcesByName.get("EmailPackage10Refill_5037"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2319"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("EmailPackage10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ChatPackage5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU19");
		offerResources.add(lookupResourcesByName.get("ChatPackage5Refill_5038"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2320"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("ChatPackage5", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=ChatPackage10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU20");
		offerResources.add(lookupResourcesByName.get("ChatPackage10Refill_5039"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2320"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("ChatPackage10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PhotoPackage10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU21");
		offerResources.add(lookupResourcesByName.get("PhotoPackage10Refill_5040"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2321"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("PhotoPackage10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PhotoPackage20
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU22");
		offerResources.add(lookupResourcesByName.get("PhotoPackage20Refill_5041"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2321"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("PhotoPackage20", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SocialPackage
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("YBU23");
		offerResources.add(lookupResourcesByName.get("SocialPackageRefill_5042"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2322"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("SocialPackage", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliPackage15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU24");
		offerResources.add(lookupResourcesByName.get("UnliPackage15Refill_5043"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2325"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliPackage15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliPackage30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU25");
		offerResources.add(lookupResourcesByName.get("UnliPackage30Refill_5044"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2325"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliPackage30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SpeedBoost
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU26");
		offerResources.add(lookupResourcesByName.get("SpeedBoostRefill_5045"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2323"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("SpeedBoost", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliOperaMini7
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA003");
		offerResources.add(lookupResourcesByName.get("UnliOperaMini7Refill_5033"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliOperaMini7", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliOperaMini15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA004");
		offerResources.add(lookupResourcesByName.get("UnliOperaMini15Refill_5034"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliOperaMini15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliOperaMini30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DA005");
		offerResources.add(lookupResourcesByName.get("UnliOperaMini30Refill_5035"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2303"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliOperaMini30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliNokiaXpress1Day
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU07");
		offerResources.add(lookupResourcesByName.get("UnliNokiaXpress1DayRefill_5049"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2318"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliNokiaXpress1Day", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliNokiaXpress7
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU08");
		offerResources.add(lookupResourcesByName.get("UnliNokiaXpress7Refill_5050"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2318"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliNokiaXpress7", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliNokiaXpress15
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU09");
		offerResources.add(lookupResourcesByName.get("UnliNokiaXpress15Refill_5051"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2318"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliNokiaXpress15", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliNokiaXpress30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU10");
		offerResources.add(lookupResourcesByName.get("UnliNokiaXpress30Refill_5052"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2318"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliNokiaXpress30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf299
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU12");
		offerResources.add(lookupResourcesByName.get("UnliSurf299Refill_5002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf299", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSurf999
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU13");
		offerResources.add(lookupResourcesByName.get("UnliSurf999Refill_5003"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2309"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSurf999", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlackberryMessengerDaily10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU04");
		offerResources.add(lookupResourcesByName.get("BlackberryMessengerDaily10Refill_5023"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2313"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlackberryMessengerDaily10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=MessagingUnliChatAll
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU11");
		offerResources.add(lookupResourcesByName.get("MessagingUnliChatAllRefill_5056"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1155"));
		offerResources.add(lookupResourcesByName.get("MessagingUnliChatAllDedicatedAccount_155"));
		offer = helper.createCommercialOffer("MessagingUnliChatAll", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=BlaastPackage
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZBU28");
		offerResources.add(lookupResourcesByName.get("BlaastPackageRefill_5057"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2326"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("BlaastPackage", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EloadPromo5000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ELP01");
		offerResources.add(lookupResourcesByName.get("EloadPromo5000Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("EloadPromo5000DedicatedAccount_1"));
		offer = helper.createCommercialOffer("EloadPromo5000", "", "PHP", 500000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EloadPromo10000
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ELP02");
		offerResources.add(lookupResourcesByName.get("EloadPromo10000Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("EloadPromo10000DedicatedAccount_1"));
		offer = helper.createCommercialOffer("EloadPromo10000", "", "PHP", 1000000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KaPartnerRewards01
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("KPR01");
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards01Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards01DedicatedAccount_52"));
		offer = helper.createCommercialOffer("KaPartnerRewards01", "", "PHP", 30000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KaPartnerRewards10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("KPR10");
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards10Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards10DedicatedAccount_1"));
		offer = helper.createCommercialOffer("KaPartnerRewards10", "", "PHP", 2000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KaPartnerRewards11
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("KPR11");
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards11Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1003"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards11DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards11DedicatedAccount_3"));
		offer = helper.createCommercialOffer("KaPartnerRewards11", "", "PHP", 1500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=KaPartnerRewards12
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("KPR12");
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards12Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards12DedicatedAccount_54"));
		offerResources.add(lookupResourcesByName.get("KaPartnerRewards12DedicatedAccount_52"));
		offer = helper.createCommercialOffer("KaPartnerRewards12", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=DBBuildingProfilingProgram01
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DBP01");
		offerResources.add(lookupResourcesByName.get("DBBuildingProfilingProgram01Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("DBBuildingProfilingProgram01DedicatedAccount_52"));
		offer = helper.createCommercialOffer("DBBuildingProfilingProgram01", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnileverReward01
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UNI01");
		offerResources.add(lookupResourcesByName.get("UnileverReward01Refill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("UnileverReward01DedicatedAccount_52"));
		offer = helper.createCommercialOffer("UnileverReward01", "", "PHP", 2500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SmartLoadIOSSMSJapan
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("I");
		offerResources.add(lookupResourcesByName.get("SmartLoadIOSSMSJapanRefill_VS01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1052"));
		offerResources.add(lookupResourcesByName.get("SmartLoadIOSSMSJapanDedicatedAccount_1"));
		offerResources.add(lookupResourcesByName.get("SmartLoadIOSSMSJapanDedicatedAccount_52"));
		offer = helper.createCommercialOffer("SmartLoadIOSSMSJapan", "", "PHP", 200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SMARTLoad1
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("1PTST");
		offerResources.add(lookupResourcesByName.get("SMARTLoad1Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("SMARTLoad1DedicatedAccount_1"));
		offer = helper.createCommercialOffer("SMARTLoad1", "", "PHP", 100, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=#PaskonaloTxtPromo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		// ----------------------------------------------------------
		// Offer.name=AlkansyaPasaTropa
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PSA01");
		offerResources.add(lookupResourcesByName.get("AlkansyaPasaTropaRefill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("AlkansyaPasaTropaDedicatedAccount_1"));
		offer = helper.createCommercialOffer("AlkansyaPasaTropa", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AlkansyaPasaTropaAlkansya
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("PSA02");
		offerResources.add(lookupResourcesByName.get("AlkansyaPasaTropaAlkansyaRefill_AL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("AlkansyaPasaTropaAlkansyaDedicatedAccount_1"));
		offer = helper.createCommercialOffer("AlkansyaPasaTropaAlkansya", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Regular10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("TPW04");
		planCodes.add("Plan10");
		offerResources.add(lookupResourcesByName.get("Regular10Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Regular10DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Regular10", "", "PHP", 1000, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Regular5
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("Plan5");
		planCodes.add("RPL05");
		offerResources.add(lookupResourcesByName.get("Regular5Refill_L001"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("Regular5DedicatedAccount_1"));
		offer = helper.createCommercialOffer("Regular5", "", "PHP", 500, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTrio20Plus
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN13");
		planCodes.add("YTN46");
		offerResources.add(lookupResourcesByName.get("UnliTrio20PlusRefill_1071"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2301"));
		offerResources.add(lookupResourcesByName.get("UnliTrio20PlusDedicatedAccount_5"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTrio20Plus", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliTx2All10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("ZTN14");
		offerResources.add(lookupResourcesByName.get("UnliTx2All10Refill_1072"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliTx2All10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliCombo10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UCT37");
		planCodes.add("TUAS3");
		offerResources.add(lookupResourcesByName.get("UnliCombo10Refill_1073"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliCombo10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliSingko
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UF170");
		planCodes.add("TUAS2");
		offerResources.add(lookupResourcesByName.get("UnliSingkoRefill_1074"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2102"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliSingko", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CallText10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("YBU48");
		offerResources.add(lookupResourcesByName.get("CallText10Refill_1075"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1005"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("CallText10DedicatedAccount_5"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("CallText10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UnliAllText10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("YBU49");
		offerResources.add(lookupResourcesByName.get("UnliAllText10Refill_1076"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2103"));
		offerResources.add(lookupResourcesByName.get("Pam_2"));
		offer = helper.createCommercialOffer("UnliAllText10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=Katok25FreebiePromo
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("GR007");
		offerResources.add(lookupResourcesByName.get("Katok25FreebiePromoRefill_KF25"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_2101"));
		offer = helper.createCommercialOffer("Katok25FreebiePromo", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CREATE_SUBSCRIBER
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("CREATE_SUBSCRIBER");
		
		Resource dnsRes =  lookupResourcesByName.get("CreateSubscriber");
		//Resource acipRes = lookupResourcesByName.get("InstallSubscriberACIP_Cmd");
		offerResources.add(dnsRes);
		
		offer = helper.createNonCommercialOffer("CREATE_SUBSCRIBER", "Create Subscriber", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=READ_SUBSCRIBER
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("READ_SUBSCRIBER");
		offerResources.add(lookupResourcesByName.get("GetAccountDetails_Cmd"));
		offerResources.add(lookupResourcesByName.get("GetBalanceAndDate_Cmd"));
		offer = helper.createNonCommercialOffer("READ_SUBSCRIBER", "Read Subscriber", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UpdateSubscriberSegmentation
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UpdateSubscriberSegmentation");
		offerResources.add(lookupResourcesByName.get("UpdateSubscriberSegmentation_Cmd"));
		offer = helper.createNonCommercialOffer("UpdateSubscriberSegmentation", "Update Subscriber Segmentation", planCodes, offerResources);
		offerManager.createOffer(offer);	
		// ----------------------------------------------------------
		// Offer.name=SIMShelfLifeExt
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SIMShelfLifeExt");
		offerResources.add(lookupResourcesByName.get("SIMShelfLifeExtUpdSubsSeg_Cmd"));
		offer = helper.createNonCommercialOffer("SIMShelfLifeExt", "SIM Shelf Life Extension", planCodes, offerResources);
		offerManager.createOffer(offer);	
		// ----------------------------------------------------------
		// Offer.name=Preload
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("Preload");
		offerResources.add(lookupResourcesByName.get("PreloadUpdateServiceClass_Cmd"));
		offerResources.add(lookupResourcesByName.get("PreloadAddPAM_Cmd"));
		offerResources.add(lookupResourcesByName.get("PreloadUpdSubsSeg_Cmd"));
		offer = helper.createNonCommercialOffer("Preload", "Preload", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=EntireDelete
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("EntireDelete");
		offerResources.add(lookupResourcesByName.get("EntireDeleteDeleteDns_Cmd"));
		offerResources.add(lookupResourcesByName.get("EntireDeleteDeleteSubs_Cmd"));
		offer = helper.createNonCommercialOffer("EntireDelete", "Entire Delete", planCodes, offerResources);
		offerManager.createOffer(offer);		
//		BAR, BarUpdSubsSeg_Cmd
//		ExtendGrace, 
	
	}

	
    public static void main( String[] args ) throws Exception
    {
		OfferUtil offerUtil = new OfferUtil();

		System.out.println("Creating offer, profile, service catalog ...");
		offerUtil.createAllProductCatalog();
		
		System.out.println("Done catalog creation");
    }
}
