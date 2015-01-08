package com.ericsson.raso.sef.catalog.controller;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.OfferGroupManager;
import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.OwnerManager;
import com.ericsson.raso.sef.bes.prodcat.ResourceGroupManager;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.AbstractMinimumCommitment;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.CommitHardDate;
import com.ericsson.raso.sef.bes.prodcat.entities.CommitUntilNDays;
import com.ericsson.raso.sef.bes.prodcat.entities.CommitUntilNRenewals;
import com.ericsson.raso.sef.bes.prodcat.entities.Cost;
import com.ericsson.raso.sef.bes.prodcat.entities.DaysTime;
import com.ericsson.raso.sef.bes.prodcat.entities.EndUser;
import com.ericsson.raso.sef.bes.prodcat.entities.HardDateTime;
import com.ericsson.raso.sef.bes.prodcat.entities.HoursTime;
import com.ericsson.raso.sef.bes.prodcat.entities.ImmediateTermination;
import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
import com.ericsson.raso.sef.bes.prodcat.entities.LimitedQuota;
import com.ericsson.raso.sef.bes.prodcat.entities.Market;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.entities.NoCommitment;
import com.ericsson.raso.sef.bes.prodcat.entities.NoTermination;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Opco;
import com.ericsson.raso.sef.bes.prodcat.entities.OpcoGroup;
import com.ericsson.raso.sef.bes.prodcat.entities.Owner;
import com.ericsson.raso.sef.bes.prodcat.entities.Partner;
import com.ericsson.raso.sef.bes.prodcat.entities.Price;
import com.ericsson.raso.sef.bes.prodcat.entities.PricingPolicy;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.entities.SubscriberType;
import com.ericsson.raso.sef.bes.prodcat.entities.Tax;
import com.ericsson.raso.sef.bes.prodcat.entities.TaxAbsolute;
import com.ericsson.raso.sef.bes.prodcat.entities.TaxFree;
import com.ericsson.raso.sef.bes.prodcat.entities.TaxPercentage;
import com.ericsson.raso.sef.bes.prodcat.entities.TenantMvno;
import com.ericsson.raso.sef.bes.prodcat.entities.TerminateAfterNDays;
import com.ericsson.raso.sef.bes.prodcat.entities.TerminateAfterNRenewals;
import com.ericsson.raso.sef.bes.prodcat.entities.TerminateHardDate;
import com.ericsson.raso.sef.bes.prodcat.entities.UnlimitedQuota;
import com.ericsson.raso.sef.bes.prodcat.entities.smart.SmartLIfeCyclePricingPolicy;
import com.ericsson.raso.sef.bes.prodcat.entities.smart.SmartSimplePricingPolicy;
import com.ericsson.raso.sef.catalog.Exception.FrontEndException;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.client.air.request.OfferSelection;
import com.ericsson.raso.sef.client.air.request.PamInformation;
import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.core.DateUtil;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.profiles.FulfillmentProfile;
import com.ericsson.raso.sef.fulfillment.profiles.PamInformationList;
import com.ericsson.raso.sef.fulfillment.profiles.ProfileRegistry;
import com.ericsson.raso.sef.fulfillment.profiles.smart.DedicatedAccountReversal;
import com.ericsson.raso.sef.fulfillment.profiles.smart.TimerOfferReversal;
import com.ericsson.raso.sef.ruleengine.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.FilterBuilder;

@Controller
public class CatalogController {
	static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(CatalogController.class.getName());

	private ServiceRegistry serviceRegistry = null;
	private OfferManager offerManager = null;
	private ObjectMapper objectMapper = null;
	private ResourceGroupManager resourceGroupManager = null;
	private OfferGroupManager offerGroupManager = null;
	private OwnerManager ownerManager = null;
	private ProfileRegistry profileRegistry = null;

	public CatalogController() {
		this.serviceRegistry = new ServiceRegistry();
		this.offerManager = new OfferManager();
		this.objectMapper = new ObjectMapper();
		this.resourceGroupManager = new ResourceGroupManager();
		this.offerGroupManager = new OfferGroupManager();
		this.ownerManager = new OwnerManager();
		this.profileRegistry = new ProfileRegistry();
	}

	@RequestMapping(value = "/getAllProfileTypes", method = RequestMethod.GET)
	public @ResponseBody Object getAllProfileTypes() {
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false),
								new ResourcesScanner())
						.setUrls(
								ClasspathHelper.forClassLoader(classLoadersList
										.toArray(new ClassLoader[0])))
						.filterInputsBy(
								new FilterBuilder().include(FilterBuilder
										.prefix("com.ericsson.raso.sef.fulfillment.profiles"))));

		Set<Class<? extends FulfillmentProfile>> allClasses = reflections
				.getSubTypesOf(FulfillmentProfile.class);

		Map<String, Object> mapOfClasses = new HashMap<String, Object>();

		for (Class<? extends Object> classes : allClasses) {
			Constructor<?> cons;
			try {
				cons = classes.getConstructor(String.class);
			} catch (NoSuchMethodException e) {
				continue;
			} catch (SecurityException e) {
				continue;
			}
			int conModifier = cons.getModifiers();
			if (!Modifier.isPublic(conModifier)) {
				continue;
			}
			Map<String, String> privateFields = new HashMap<>();

			Field[] firstFields = classes.getDeclaredFields();

			Class<? extends Object> clc = classes;

			while (clc != null) {
				clc = clc.getSuperclass();
				if (clc.getSimpleName().equals("BlockingFulfillment")
						|| clc.getSimpleName()
								.equals("AsynchronousFulfillment")) {
					break;
				}

				Field[] secondFields = clc.getDeclaredFields();

				int length = firstFields.length;

				firstFields = Arrays.copyOf(firstFields, firstFields.length
						+ secondFields.length);
				System.arraycopy(secondFields, 0, firstFields, length,
						secondFields.length);
			}

			for (Field field : firstFields) {
				int modifier = field.getModifiers();
				if (!Modifier.isStatic(modifier) && !Modifier.isFinal(modifier)) {
					privateFields.put(field.getName(), field.getGenericType()
							.toString());
				}
			}
			mapOfClasses.put(classes.getName(), privateFields);
		}

		return mapOfClasses;
	}

	@RequestMapping(value = "/createOffer", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Response createOffer(@RequestBody String str) {
		Response response = new Response();
		response.setStatus(GuiConstants.SUCCESS);
		try {

			JSONObject json = new JSONObject(str.trim());
			Offer offer = new Offer((String) json.get("name"));
			offer.setOfferState(State.IN_CREATION);

			this.prepareOffer(json, offer);

			try {
				this.offerManager.createOffer(offer);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Offer Saved Successfully");
			} catch (CatalogException e) {
				e.printStackTrace();
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e.getMessage());
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		} catch (CatalogException ce) {
			ce.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(ce.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/getAllOffers", method = RequestMethod.GET)
	public @ResponseBody String getAllOffers(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "pageSize", required = true) int pageSize)
			throws FrameworkException {
		// getting all offers from ccm files
		TreeMap<String, TreeMap<Integer, Offer>> offers = this.offerManager
				.getAllOffers();

		if (search != null && !search.equals("")) {
			TreeMap<String, TreeMap<Integer, Offer>> newOffers = (TreeMap<String, TreeMap<Integer, Offer>>) offers
					.clone();

			Iterator<Entry<String, TreeMap<Integer, Offer>>> iter = newOffers
					.entrySet().iterator();

			Map.Entry<String, TreeMap<Integer, Offer>> entry = null;

			while (iter.hasNext()) {
				entry = iter.next();
				if (!Pattern
						.compile(Pattern.quote(search),
								Pattern.CASE_INSENSITIVE)
						.matcher(entry.getKey()).find()) {
					iter.remove();
				}
			}
			offers = newOffers;
		}

		int startIndex = (pageNumber - 1) * pageSize;

		int lastIndex = startIndex + pageSize;

		List<TreeMap<Integer, Offer>> listOffers = new ArrayList<TreeMap<Integer, Offer>>(
				offers.values());

		if (lastIndex > listOffers.size()) {
			lastIndex = listOffers.size();
		}

		List<TreeMap<Integer, Offer>> subListOffers = listOffers.subList(
				startIndex, lastIndex);

		Map<String, List<TreeMap<Integer, Offer>>> offersMap = new HashMap<>();

		offersMap.put("offers", subListOffers);

		String offerList = null;

		ObjectMapper mapper = new ObjectMapper();

		try {
			offerList = mapper.writeValueAsString(offersMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offerList;
	}

	@RequestMapping(value = "/readOffer", method = RequestMethod.GET)
	public @ResponseBody String readOffer(ModelMap model,
			@RequestParam(value = "offerId", required = true) String offerId)
			throws FrameworkException {
		String jsonResource = null;
		Offer offer = null;
		offer = this.offerManager.getOfferById(offerId);
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonResource = mapper.writeValueAsString(offer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResource;
	}

	@RequestMapping(value = "/updateOffer", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Response updateOffer(@RequestBody String str) {
		Response response = new Response();
		try {
			JSONObject json = new JSONObject(str.trim());
			Offer offer = this.offerManager.getOfferById((String) json
					.get("name"));

			List<AtomicProduct> atomicProducts = offer.getAllAtomicProducts();
			for (AtomicProduct atomicProduct : atomicProducts) {
				offer.removeProduct(atomicProduct);
			}

			Set<String> externalHandlers = offer.getExternalHandles();

			if (externalHandlers != null) {
				for (String externalHandler : externalHandlers) {
					offer.removeExternalHandle(externalHandler);
				}
			}

			this.prepareOffer(json, offer);

			try {
				offer.validate(true);
				this.offerManager.updateOffer(offer);
				response.setStatus(GuiConstants.SUCCESS);
			} catch (CatalogException e) {
				e.printStackTrace();
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e.getMessage());
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		} catch (CatalogException ce) {
			ce.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(ce.getMessage());
		}
		return response;
	}

	private void prepareOffer(JSONObject json, Offer offer)
			throws CatalogException {

		offer.setDescription((String) json.get("description"));
		offer.setOfferGroup((String) json.get("offerGroup"));

		boolean recurrent = false;

		if (!json.isNull("recurrent")
				&& (json.getString("recurrent").equalsIgnoreCase("true") || json
						.getString("recurrent").equalsIgnoreCase("false"))) {
			recurrent = json.getBoolean("recurrent");
			offer.setRecurrent(recurrent);
		}

		if (!json.isNull("renewalPeriod")) {

			if (json.getString("renewalPeriod").equalsIgnoreCase("infinite")) {
				if (recurrent) {
					throw new CatalogException(
							"Recurrence is enabled but validity period is infnite!!");
				} else {
					offer.setRenewalPeriod(new InfiniteTime());
				}
			} else if (json.getString("renewalPeriod").equalsIgnoreCase("days")) {

				if (json.get("renewalDays").equals("")) {
					throw new CatalogException(
							"Renewals days can not be empty!!");
				}

				offer.setRenewalPeriod(new DaysTime(json.getInt("renewalDays")));
			} else if (json.getString("renewalPeriod")
					.equalsIgnoreCase("hours")) {
				if (json.get("renewalHours").equals("")) {
					throw new CatalogException(
							"Renewals hours can not be empty!!");
				}

				if (json.getInt("renewalHours") < 0
						|| json.getInt("renewalHours") > 24) {
					throw new CatalogException(
							"Renewals hours should be greater than Zero and less than 24!!");
				}

				offer.setRenewalPeriod(new HoursTime(Byte.valueOf(json
						.getString("renewalHours"))));
			} else if (json.getString("renewalPeriod").equalsIgnoreCase("date")) {
				if (json.get("renewalDate").equals("")) {
					throw new CatalogException(
							"Renewals date can not be empty!!");
				}
				String renewalDateString = json.getString("renewalDate");
				Date renewal_date;
				try {
					renewal_date = DateUtil.toDate(renewalDateString,
							"yyyy-MM-dd");
					Calendar calendar1 = Calendar.getInstance();
					Calendar calendar2 = Calendar.getInstance();
					calendar1.setTime(new Date());
					calendar2.setTime(renewal_date);
					long milliseconds1 = calendar1.getTimeInMillis();
					long milliseconds2 = calendar2.getTimeInMillis();
					long diff = milliseconds2 - milliseconds1;
					int days = (int) (diff / (24 * 60 * 60 * 1000));
					offer.setRenewalPeriod(new DaysTime(days));
				} catch (ParseException e) {
					throw new CatalogException(e);
				}
			}

		}

		if (!json.isNull("commercial")
				&& (json.getString("commercial").equalsIgnoreCase("true") || json
						.getString("commercial").equalsIgnoreCase("false"))) {
			offer.setCommercial(json.getBoolean("commercial"));

			if (json.getBoolean("commercial")) {

				if (json.get("cost").equals("")) {
					throw new CatalogException(
							"Cost can not be empty under price!!");
				}

				if (json.getInt("cost") <= 0) {
					throw new CatalogException(
							"Cost should be greater than Zero under price!!");
				}

				Price price = new Price(json.getString("currency"),
						json.getLong("cost"));

				// TODO: policies concept is not clear need to be add

				if (json.getString("pricePolicy").equals("")) {
					throw new CatalogException(
							"Enter Pricing Policy name under price!!");
				}

				if (json.getString("rule").equals("")) {
					throw new CatalogException("Select Rule under price!!");
				}

				PricingPolicy pricingPolicy = null;

				if (json.getString("policyType").equals(
						"SmartLIfeCyclePricingPolicy")) {

					if (json.get("purchaseCost").equals("")) {
						throw new CatalogException(
								"Purchase Cost can not be empty under price!!");
					}

					if (json.getInt("purchaseCost") <= 0) {
						throw new CatalogException(
								"Purchase Cost should be greater than Zero under price!!");
					}

					if (json.get("renewalCost").equals("")) {
						throw new CatalogException(
								"Renewal Cost can not be empty under price!!");
					}

					if (json.getInt("renewalCost") <= 0) {
						throw new CatalogException(
								"Renewal Cost should be greater than Zero under price!!");
					}

					pricingPolicy = new SmartLIfeCyclePricingPolicy(
							json.getString("pricePolicy"), new Cost(
									json.getString("currency"),
									json.getLong("purchaseCost")), new Cost(
									json.getString("currency"),
									json.getLong("renewalCost")));

				} else if (json.getString("policyType").equals(
						"SmartSimplePricingPolicy")) {

					pricingPolicy = new SmartSimplePricingPolicy(
							json.getString("pricePolicy"));

				} else {
					throw new CatalogException(
							"Select Policy Type under price!!");
				}

				pricingPolicy.setRule(new Rule(json.getString("rule")));

				price.addRatingRule(pricingPolicy);

				List<Tax> taxList = new ArrayList<Tax>();

				if (json.isNull("taxes")) {
					throw new CatalogException("Add Taxes under price!!");
				}

				JSONArray taxes = json.getJSONArray("taxes");
				for (int i = 0; i < taxes.length(); i++) {
					JSONObject tax = (JSONObject) taxes.get(i);
					if (tax.getString("taxType").equals("Absolute")) {

						if (tax.getString("value").equals("")) {
							throw new CatalogException(
									"Absolute Tax can not be empty under price!!");
						}

						TaxAbsolute taxAbsolute = new TaxAbsolute();

						taxAbsolute.setTaxAbsolute(tax.getLong("value"));

						taxList.add(taxAbsolute);

					} else if (tax.getString("taxType").equals("Free")) {
						taxList.add(new TaxFree());
					} else if (tax.getString("taxType").equals("Percentage")) {
						if (tax.get("value").equals("")) {
							throw new CatalogException(
									"Percentage Tax can not be empty under price!!");
						}

						if (tax.getInt("value") <= 0
								|| tax.getInt("value") >= 100) {
							throw new CatalogException(
									"Percentage Tax should be between Zero and 100 under price!!");
						}

						TaxPercentage taxPercentage = new TaxPercentage();

						taxPercentage.setTaxPercentile(Byte.valueOf(tax
								.getString("value")));

						taxList.add(taxPercentage);
					}
				}

				price.setTaxes(taxList);

				offer.setPrice(price);
			}
		}

		Owner owner = null;

		if (!json.isNull("ownerType")) {
			if (json.getString("ownerType").equalsIgnoreCase("EndUser")) {
				owner = new EndUser((String) json.get("ownerName"));
			} else if (json.getString("ownerType").equalsIgnoreCase("Market")) {
				owner = new Market((String) json.get("ownerName"));
			} else if (json.getString("ownerType").equalsIgnoreCase("Opco")) {
				owner = new Opco((String) json.get("ownerName"));
			} else if (json.getString("ownerType")
					.equalsIgnoreCase("OpcoGroup")) {
				owner = new OpcoGroup((String) json.get("ownerName"));
			} else if (json.getString("ownerType").equalsIgnoreCase("Partner")) {
				owner = new Partner((String) json.get("ownerName"));
			} else if (json.getString("ownerType")
					.equalsIgnoreCase("TenantMVO")) {
				owner = new TenantMvno((String) json.get("ownerName"));
			}

			offer.setOwner(owner);
		}

		// TODO: AbstractAccumulationPolicy need to be add

		// TODO: AbstractSwitchPolicy need to be add

		// NO_TERMINATION,
		// AFTER_X_DAYS,
		// AFTER_X_RENEWALS,
		// HARD_STOP;

		if (!json.isNull("autoTermination")
				&& json.get("autoTermination") != null) {
			if (json.get("autoTermination").equals("NO_TERMINATION")) {
				offer.setAutoTermination(new NoTermination());
			} else if (json.get("autoTermination").equals("AFTER_X_DAYS")) {
				if (json.get("autoTerminationDays").equals("")) {
					throw new CatalogException(
							"Auto Termination days can not be empty!!");
				}
				offer.setAutoTermination(new TerminateAfterNDays(json
						.getInt("autoTerminationDays")));
			} else if (json.get("autoTermination").equals("AFTER_X_RENEWALS")) {
				if (json.get("autoTerminationRenewals").equals("")) {
					throw new CatalogException(
							"Auto Termination Renewals can not be empty!!");
				}
				offer.setAutoTermination(new TerminateAfterNRenewals(json
						.getInt("autoTerminationRenewals")));
			} else if (json.get("autoTermination").equals("HARD_STOP")) {

				if (json.get("autoTerminationDate").equals("")) {
					throw new CatalogException(
							"Auto Termination date can not be empty!!");
				}

				String startDateString = json.getString("autoTerminationDate");
				Date hard_stop_date = null;
				try {
					hard_stop_date = DateUtil.toDate(startDateString,
							"yyyy-MM-dd");
					offer.setAutoTermination(new TerminateHardDate(
							hard_stop_date));
				} catch (ParseException e1) {
					e1.printStackTrace();
					throw new CatalogException(e1);
				}
			}
		}

		// NO_COMMITMENT,
		// UNTIL_X_DAYS,
		// UNTIL_X_RENEWALS,
		// HARD_LIMIT;

		if (!json.isNull("minimumCommitment")
				&& json.get("minimumCommitment") != null) {
			if (json.get("minimumCommitment").equals("NO_COMMITMENT")) {
				offer.setMinimumCommitment(new NoCommitment(
						AbstractMinimumCommitment.Type.NO_COMMITMENT));
			} else if (json.get("minimumCommitment").equals("UNTIL_X_DAYS")) {

				if (json.get("minimumCommitmentDays").equals("")) {
					throw new CatalogException(
							"Commitment days can not be empty!!");
				}
				offer.setMinimumCommitment(new CommitUntilNDays(json
						.getInt("minimumCommitmentDays")));
			} else if (json.get("minimumCommitment").equals("UNTIL_X_RENEWALS")) {
				if (json.get("minimumCommitmentRenewals").equals("")) {
					throw new CatalogException(
							"Commitment Renewals can not be empty!!");
				}
				offer.setMinimumCommitment(new CommitUntilNRenewals(json
						.getInt("minimumCommitmentRenewals")));
			} else if (json.get("minimumCommitment").equals("HARD_LIMIT")) {
				if (json.get("minimumCommitmentDate").equals("")) {
					throw new CatalogException(
							"Commitment Date can not be empty!!");
				}

				String commitmentDateString = json
						.getString("minimumCommitmentDate");
				Date hard_limit_date = null;

				try {
					hard_limit_date = DateUtil.toDate(commitmentDateString,
							"yyyy-MM-dd");
					offer.setMinimumCommitment(new CommitHardDate(
							hard_limit_date));
				} catch (ParseException e1) {
					e1.printStackTrace();
					throw new CatalogException(e1);
				}

			}
		}

		if (!json.isNull("trialPeriod")) {
			if (json.getString("trialPeriod").equalsIgnoreCase("infinite")) {
				throw new CatalogException("Trial Period cannot be inifite!!");
			} else if (json.getString("trialPeriod").equalsIgnoreCase("days")) {

				if (json.get("trialDays").equals("")) {
					throw new CatalogException("Trial days can not be empty!!");
				}

				offer.setTrialPeriod(new DaysTime(json.getInt("trialDays")));
			} else if (json.getString("trialPeriod").equalsIgnoreCase("hours")) {

				if (json.get("trialHours").equals("")) {
					throw new CatalogException("Trial hours can not be empty!!");
				}

				if (json.getInt("trialHours") < 0
						|| json.getInt("trialHours") > 24) {
					throw new CatalogException(
							"Trial hours should be greater than Zero and less than 24!!");
				}

				offer.setTrialPeriod(new HoursTime(Byte.valueOf(json
						.getString("trialHours"))));
			} else if (json.getString("trialPeriod").equalsIgnoreCase("date")) {

				if (json.get("trialDate").equals("")) {
					throw new CatalogException("Trial Date can not be empty!!");
				}

				String trialDateString = json.getString("trialDate");
				Date trial_date;
				try {
					trial_date = DateUtil.toDate(trialDateString, "yyyy-MM-dd");
					Calendar calendar1 = Calendar.getInstance();
					Calendar calendar2 = Calendar.getInstance();
					calendar1.setTime(new Date());
					calendar2.setTime(trial_date);
					long milliseconds1 = calendar1.getTimeInMillis();
					long milliseconds2 = calendar2.getTimeInMillis();
					long diff = milliseconds2 - milliseconds1;
					int days = (int) (diff / (24 * 60 * 60 * 1000));
					offer.setTrialPeriod(new DaysTime(days));
				} catch (ParseException e) {
					e.printStackTrace();
					throw new CatalogException(e);
				}
			}
		}

		ImmediateTermination it = new ImmediateTermination();
		if (!json.isNull("PREPAID")
				&& json.getString("PREPAID").equalsIgnoreCase("true"))
			it.setIsAllowed(SubscriberType.PREPAID, true);
		else
			it.setIsAllowed(SubscriberType.PREPAID, false);

		if (!json.isNull("POSTPAID")
				&& json.getString("POSTPAID").equalsIgnoreCase("true"))
			it.setIsAllowed(SubscriberType.POSTPAID, true);
		else
			it.setIsAllowed(SubscriberType.POSTPAID, false);

		if (!json.isNull("HYBRID")
				&& json.getString("HYBRID").equalsIgnoreCase("true"))
			it.setIsAllowed(SubscriberType.HYBRID, true);
		else
			it.setIsAllowed(SubscriberType.HYBRID, false);

		if (!json.isNull("CONVERGANT")
				&& json.getString("CONVERGANT").equalsIgnoreCase("true"))
			it.setIsAllowed(SubscriberType.CONVERGANT, true);
		else
			it.setIsAllowed(SubscriberType.CONVERGANT, false);

		offer.setImmediateTermination(it);

		if (offer.getOfferState().name().equals("TESTING")
				|| offer.getOfferState().name().equals("PUBLISHED")) {
			if (json.isNull("products")
					|| !(json.get("products") instanceof JSONArray)
					|| json.getJSONArray("products").length() < 1) {
				throw new CatalogException("Products can not be empty!!");
			}
		}

		if (!json.isNull("products")
				&& (json.get("products") instanceof JSONArray)) {

			JSONArray products = json.getJSONArray("products");
			int productSize = products.length();

			for (int i = 0; i < productSize; i++) {

				JSONObject jsonProduct = (JSONObject) products.get(i);

				AtomicProduct atomicProduct = new AtomicProduct(
						jsonProduct.getString("name"));

				if (!jsonProduct.getString("owner").equals("")) {
					Owner productOwner = this.ownerManager
							.readOwner(jsonProduct.getString("owner"));
					atomicProduct.setOwner(productOwner);
				}

				if (!jsonProduct.getString("resource").equals("")) {
					Resource productResource = this.serviceRegistry
							.readResource(jsonProduct.getString("resource"));
					atomicProduct.setResource(productResource);
				}

				if (!jsonProduct.getString("quota").equals("")) {
					if (jsonProduct.getString("quota").equals("LIMITED")) {
						atomicProduct.setQuota(new LimitedQuota());
					} else if (jsonProduct.getString("quota").equals(
							"UNLIMITED")) {
						atomicProduct.setQuota(new UnlimitedQuota());
					}
				}

				if (!jsonProduct.isNull("productValidity")) {
					if (jsonProduct.getString("productValidity")
							.equalsIgnoreCase("infinite")) {
						atomicProduct.setValidity(new InfiniteTime());
					} else if (jsonProduct.getString("productValidity")
							.equalsIgnoreCase("days")) {

						if (jsonProduct.get("validityDays").equals("")) {
							throw new CatalogException(
									"Validitys days can not be empty!!");
						}

						atomicProduct.setValidity(new DaysTime(jsonProduct
								.getInt("validityDays")));
					} else if (jsonProduct.getString("productValidity")
							.equalsIgnoreCase("hours")) {
						if (jsonProduct.get("validityHours").equals("")) {
							throw new CatalogException(
									"Validitys hours can not be empty!!");
						}

						if (jsonProduct.getInt("validityHours") < 0
								|| jsonProduct.getInt("validityHours") > 24) {
							throw new CatalogException(
									"Validitys hours should be greater than Zero and less than 24!!");
						}

						atomicProduct.setValidity(new HoursTime(
								Byte.valueOf(jsonProduct
										.getString("validityHours"))));
					} else if (jsonProduct.getString("productValidity")
							.equalsIgnoreCase("date")) {
						if (jsonProduct.get("validityDate").equals("")) {
							throw new CatalogException(
									"Validitys date can not be empty!!");
						}
						String validityDateString = jsonProduct
								.getString("validityDate");
						Date validity_date;
						try {
							validity_date = DateUtil.toDate(validityDateString,
									"yyyy-MM-dd");
							atomicProduct.setValidity(new HardDateTime(
									validity_date));
						} catch (ParseException e) {
							throw new CatalogException(e);
						}
					}

				}

				offer.addProduct(atomicProduct);
			}

		}

		if (json.isNull("externalHandlers")
				|| !(json.get("externalHandlers") instanceof JSONArray)
				|| json.getJSONArray("externalHandlers").length() < 1) {
			throw new CatalogException("External Handlers can not be empty!!");
		}
		JSONArray externalHandlers = json.getJSONArray("externalHandlers");

		int externalHandlerSize = externalHandlers.length();
		for (int i = 0; i < externalHandlerSize; i++) {
			offer.addExternalHandle((String) externalHandlers.get(i));
		}

		if (!json.isNull("whitelistUsers")
				&& (json.get("whitelistUsers") instanceof JSONArray)) {
			JSONArray whitelistUsers = json.getJSONArray("whitelistUsers");

			int whitelistUsersSize = whitelistUsers.length();

			Set<String> whitelistUsersSet = new HashSet<String>();

			for (int i = 0; i < whitelistUsersSize; i++) {
				whitelistUsersSet.add((String) whitelistUsers.get(i));
			}
			offer.setWhiteListedUsers(whitelistUsersSet);
		}

		if (!json.isNull("exitOfferId")
				&& !json.getString("exitOfferId").equals("")) {
			Offer exitOffer = this.offerManager.getOfferById(json
					.getString("exitOfferId"));

			if (exitOffer == null) {
				throw new CatalogException(json.getString("exitOfferId")
						+ " Offer does not exist!!");
			}

			offer.setExit(exitOffer);
		}
	}

	@RequestMapping(value = "/isOfferExists", method = RequestMethod.GET)
	public @ResponseBody Response checkIfOfferExists(
			@RequestParam String offerId) {
		boolean isOfferExists = Boolean.FALSE;
		Response response = new Response();
		// Right now we dont have exception class implemented,to be done later
		if (offerId == null)
			throw new FrontEndException("The OfferId provided was null", "");
		try {
			isOfferExists = this.offerManager.offerExists(offerId);
			if (isOfferExists) {
				response.setStatus(GuiConstants.SUCCESS);

			} else {
				response.setStatus(GuiConstants.FAILURE);
			}
			response.setMessage(String.valueOf(isOfferExists));

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(String.valueOf(isOfferExists));
		}
		return response;

	}

	@RequestMapping(value = "/promote", method = RequestMethod.GET, headers = { "Content-type=application/json" })
	public @ResponseBody Response changeLifeCycleState(
			@RequestParam("offerName") String offerId,
			@RequestParam("offerState") String offerState) {
		if (offerId == null)
			throw new FrontEndException("", "");
		if (offerState == null)
			throw new FrontEndException("", "");
		Response response = new Response();
		try {
			Offer offer = this.offerManager.getOfferById(offerId);
			if (offerState.equalsIgnoreCase("In_Creation")) {
				offer.setOfferState(State.IN_CREATION);
			} else if (offerState.equalsIgnoreCase("Testing")) {
				offer.setOfferState(State.TESTING);
			} else if (offerState.equalsIgnoreCase("Publish")) {
				offer.setOfferState(State.PUBLISHED);
			} else if (offerState.equalsIgnoreCase("Disabled")) {
				offer.setOfferState(State.DISABLED);
			} else if (offerState.equalsIgnoreCase("Retired")) {
				offer.setOfferState(State.RETIRED);
			}
			this.offerManager.changeLifeCycle(offerId, offer.getVersion(),
					offer.getOfferState());
			response.setStatus(GuiConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/createResource", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Response createResource(@RequestBody String str) {
		Response response = new Response();
		try {
			JSONObject json = new JSONObject(str.trim());
			if (json.getString("name") == null
					|| json.getString("name").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Resource name cannot be empty");
				return response;
			}
			Resource resource = new Service((String) json.get("name"));
			resource.setDescription((String) json.get("description"));
			resource.setExternallyConsumed(json
					.getBoolean("externallyConsumed"));
			resource.setConsumable(json.getBoolean("consumable"));
			resource.setDiscoverable(json.getBoolean("discoverable"));
			resource.setAbstract(json.getBoolean("abstract"));
			if (json.getBoolean("consumable")) {
				if (json.isNull("consumptionUnitName")
						|| json.getString("consumptionUnitName").isEmpty()) {
					response.setStatus(GuiConstants.FAILURE);
					response.setMessage("Consumption Unit Name can not be empty.");
					return response;
				}
				resource.setConsumptionUnitName((String) json
						.get("consumptionUnitName"));
				if (!json.isNull("enforcedMinQuota")
						&& !json.getString("enforcedMinQuota").isEmpty()
						&& !json.isNull("enforcedMaxQuota")
						&& !json.getString("enforcedMaxQuota").isEmpty()) {
					if (json.getLong("enforcedMinQuota") > json
							.getLong("enforcedMaxQuota")) {
						response.setStatus(GuiConstants.FAILURE);
						response.setMessage("Minimum Quota cannot be more than maximum Quota");
						return response;
					}

					resource.setEnforcedMinQuota(json
							.getLong("enforcedMinQuota"));
					resource.setEnforcedMaxQuota(json
							.getLong("enforcedMaxQuota"));
				}

			}

			if (!json.getBoolean("abstract") && !json.isNull("cost")
					&& !json.get("cost").equals("")) {
				MonetaryUnit monetoryUnit = new Cost(
						CurrencyCode.PHP.toString(), json.getLong("cost"));
				resource.setCost(monetoryUnit);
			}
			Owner owner = null;
			if (json.getString("ownerType") != null
					&& json.getString("ownerType").equalsIgnoreCase("select")) {
				if (json.getString("ownerType").equalsIgnoreCase("EndUser")) {
					owner = new EndUser(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"Market")) {
					owner = new Market(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase("Opco")) {
					owner = new Opco(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"OpcoGroup")) {
					owner = new OpcoGroup(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"Partner")) {
					owner = new Partner(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"TenantMvno")) {
					owner = new TenantMvno(json.getString("ownerType"));
				}
			}
			resource.setOwner(owner);

			if (json.isNull("profileType")
					|| json.getString("profileType").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Select at least one Profile or Workflow");
				return response;
			}
			if (json.isNull("profileName")
					|| json.getString("profileName").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Enter Profile name.");
				return response;
			}

			if (this.profileRegistry.readProfile(json.getString("profileName")) != null) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("This profile name already exist, please try another one.");
				return response;
			}

			Class<?> profile = Class.forName(json.getString("profileType"));
			Constructor<?> cons = profile.getConstructor(String.class);
			Object object = cons.newInstance(json.getString("profileName"));

			Method[] methods = profile.getMethods();

			for (Method method : methods) {
				String methodName = method.getName();

				if (methodName.contains("set") && !methodName.equals("setName")) {
					methodName = methodName.substring(3, 4).toLowerCase()
							+ methodName.substring(4);

					boolean isFieldExist = true;

					if (json.isNull(methodName)
							|| !(json.get(methodName) instanceof String)
							|| json.getString(methodName).isEmpty()) {
						isFieldExist = false;
					}

					String parameterType = method.getGenericParameterTypes()[0]
							.toString();

					if (isFieldExist) {
						if (parameterType.toLowerCase().contains("int")) {
							method.invoke(object, json.getInt(methodName));
						} else if (parameterType.toLowerCase().contains("long")) {
							method.invoke(object, json.getLong(methodName));
						} else if (parameterType.toLowerCase().contains("date")) {
							String dateStr = json.getString(methodName);
							Date date = DateUtil.toDate(dateStr, "yyyy-MM-dd");
							method.invoke(object, date);
						} else if (parameterType.toLowerCase().contains(
								"string")) {
							method.invoke(object, json.getString(methodName));
						} else if (parameterType.toLowerCase().contains(
								"boolean")) {
							method.invoke(object, json.getBoolean(methodName));
						} else if (parameterType.toLowerCase().contains(
								"currencycode")) {
							method.invoke(object, CurrencyCode.valueOf(json
									.getString(methodName)));
						}
					} else {
						if (parameterType.toLowerCase().contains(
								"paminformation")) {
							if (!json.isNull("pamInformation")
									&& (json.get("pamInformation") instanceof JSONArray)) {
								List<PamInformation> pamInformationArrayList = new ArrayList<>();

								JSONArray pamInformationArray = json
										.getJSONArray("pamInformation");
								for (int i = 0; i < pamInformationArray
										.length(); i++) {
									JSONObject pamInformationJson = (JSONObject) pamInformationArray
											.get(i);

									PamInformation pamInformation = new PamInformation();

									if (!pamInformationJson
											.isNull("pamClassID")
											&& !pamInformationJson.getString(
													"pamClassID").isEmpty()) {
										pamInformation
												.setPamClassID(pamInformationJson
														.getInt("pamClassID"));
									}

									if (!pamInformationJson
											.isNull("pamIndicator")
											&& !pamInformationJson.getString(
													"pamIndicator").isEmpty()) {
										pamInformation
												.setPamIndicator(pamInformationJson
														.getInt("pamIndicator"));
									}

									if (!pamInformationJson
											.isNull("scheduleID")
											&& !pamInformationJson.getString(
													"scheduleID").isEmpty()) {
										pamInformation
												.setScheduleID(pamInformationJson
														.getInt("scheduleID"));
									}
									pamInformation
											.setPamServiceID(pamInformationJson
													.getInt("pamServiceID"));

									pamInformationArrayList.add(pamInformation);
								}

								if (parameterType.toLowerCase().contains(
										"paminformationlist")) {
									PamInformationList pamInformationList = new PamInformationList();

									pamInformationList
											.setPamInfolist(pamInformationArrayList);

									method.invoke(object, pamInformationList);
								} else {
									method.invoke(object,
											pamInformationArrayList);
								}

							}
						} else if (parameterType.toLowerCase().contains(
								"serviceoffering")) {
							if (!json.isNull("serviceOfferings")
									&& (json.get("serviceOfferings") instanceof JSONArray)) {
								List<ServiceOffering> serviceOfferingsArrayList = new ArrayList<>();

								JSONArray serviceOfferingsJsonArray = json
										.getJSONArray("serviceOfferings");
								for (int i = 0; i < serviceOfferingsJsonArray
										.length(); i++) {
									JSONObject serviceOfferingsJson = (JSONObject) serviceOfferingsJsonArray
											.get(i);

									ServiceOffering serviceOffering = new ServiceOffering();

									serviceOffering
											.setServiceOfferingId(serviceOfferingsJson
													.getInt("serviceOfferingId"));

									if (!serviceOfferingsJson
											.isNull("serviceOfferingActiveFlag")
											&& !serviceOfferingsJson
													.getString(
															"serviceOfferingActiveFlag")
													.isEmpty()) {
										serviceOffering
												.setServiceOfferingActiveFlag(serviceOfferingsJson
														.getBoolean("serviceOfferingActiveFlag"));
									}

									serviceOfferingsArrayList
											.add(serviceOffering);
								}

								method.invoke(object, serviceOfferingsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"dedicatedaccountreversal")) {
							if (!json.isNull("daReversals")
									&& (json.get("daReversals") instanceof JSONArray)) {
								List<DedicatedAccountReversal> daReversalsArrayList = new ArrayList<>();

								JSONArray daReversalsJsonArray = json
										.getJSONArray("daReversals");
								for (int i = 0; i < daReversalsJsonArray
										.length(); i++) {
									JSONObject daReversalsJson = (JSONObject) daReversalsJsonArray
											.get(i);

									DedicatedAccountReversal daReversal = new DedicatedAccountReversal();

									if (!daReversalsJson
											.isNull("amountToReverse")
											&& !daReversalsJson.getString(
													"amountToReverse")
													.isEmpty()) {
										daReversal
												.setAmountToReverse(daReversalsJson
														.getInt("amountToReverse"));
									}

									daReversal
											.setDedicatedAccountInformationID(daReversalsJson
													.getInt("dedicatedAccountInformationID"));

									if (!daReversalsJson
											.isNull("hoursToReverse")
											&& !daReversalsJson.getString(
													"hoursToReverse").isEmpty()) {
										daReversal
												.setHoursToReverse(daReversalsJson
														.getInt("hoursToReverse"));
									}

									daReversalsArrayList.add(daReversal);
								}

								method.invoke(object, daReversalsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"timerofferreversal")) {
							if (!json.isNull("toReversals")
									&& (json.get("toReversals") instanceof JSONArray)) {
								List<TimerOfferReversal> toReversalsArrayList = new ArrayList<>();

								JSONArray toReversalsJsonArray = json
										.getJSONArray("toReversals");
								for (int i = 0; i < toReversalsJsonArray
										.length(); i++) {
									JSONObject toReversalsJson = (JSONObject) toReversalsJsonArray
											.get(i);

									TimerOfferReversal toReversal = new TimerOfferReversal();

									if (!toReversalsJson
											.isNull("hoursToReverse")
											&& !toReversalsJson.getString(
													"hoursToReverse").isEmpty()) {
										toReversal
												.setHoursToReverse(toReversalsJson
														.getInt("hoursToReverse"));
									}

									toReversal.setOfferID(toReversalsJson
											.getInt("offerID"));
									toReversal
											.setDedicatedAccountInformationID(toReversalsJson
													.getInt("dedicatedAccountInformationID"));

									toReversalsArrayList.add(toReversal);
								}

								method.invoke(object, toReversalsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"offerselection")) {
							if (!json.isNull("offerSelection")
									&& (json.get("offerSelection") instanceof JSONArray)) {

								JSONArray offerSelectionJsonArray = json
										.getJSONArray("offerSelection");

								OfferSelection[] offerSelectionArray = new OfferSelection[offerSelectionJsonArray
										.length()];

								for (int i = 0; i < offerSelectionJsonArray
										.length(); i++) {
									JSONObject offerSelectionJson = (JSONObject) offerSelectionJsonArray
											.get(i);

									OfferSelection offerSelection = new OfferSelection();

									offerSelection
											.setOfferIDFirst(offerSelectionJson
													.getInt("offerIDFirst"));
									offerSelection
											.setOfferIDLast(offerSelectionJson
													.getInt("offerIDLast"));

									offerSelectionArray[i] = offerSelection;
								}

								method.invoke(object,
										new Object[] { offerSelectionArray });
							}
						}
					}
				}
			}

			this.profileRegistry.createProfile((FulfillmentProfile<?>) object);

			resource.addFulfillmentProfile(json.getString("profileName"));

			this.serviceRegistry.createResource(resource);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/readResource", method = RequestMethod.GET)
	public @ResponseBody String readResource(
			@RequestParam(value = "resourceName", required = true) String resourceName)
			throws FrameworkException {
		Resource resource = null;
		try {
			resource = this.serviceRegistry.readResource(resourceName);

			Map<String, Object> map = new HashMap<>();
			map.put("resource", resource);

			if (resource.getFulfillmentProfiles().size() > 0) {
				FulfillmentProfile<?> profile = this.profileRegistry
						.readProfile(resource.getFulfillmentProfiles().get(0));
				if (profile != null) {
					if (profile.getCriteria() == null) {
						profile.setCriteria(new Rule("R1"));
					}
					map.put("profile", profile);
					map.put("profileClass", profile.getClass().getName());
				}
			}
			String jsonResource = this.objectMapper.writeValueAsString(map);
			return jsonResource;
		} catch (CatalogException | IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value = "/updateResource", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Response updateResource(@RequestBody String str) {
		Response response = new Response();
		try {
			JSONObject json = new JSONObject(str.trim());
			if (json.getString("name") == null
					|| json.getString("name").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Resource name cannot be empty");
				return response;
			}
			Resource resource = new Service((String) json.get("name"));
			resource.setDescription((String) json.get("description"));
			resource.setExternallyConsumed(json
					.getBoolean("externallyConsumed"));
			resource.setConsumable(json.getBoolean("consumable"));
			resource.setDiscoverable(json.getBoolean("discoverable"));
			resource.setAbstract(json.getBoolean("abstract"));
			if (json.getBoolean("consumable")) {
				if (json.isNull("consumptionUnitName")
						|| json.getString("consumptionUnitName").isEmpty()) {
					response.setStatus(GuiConstants.FAILURE);
					response.setMessage("Consumption Unit Name can not be empty.");
					return response;
				}
				resource.setConsumptionUnitName((String) json
						.get("consumptionUnitName"));
				if (!json.isNull("enforcedMinQuota")
						&& !json.getString("enforcedMinQuota").isEmpty()
						&& !json.isNull("enforcedMaxQuota")
						&& !json.getString("enforcedMaxQuota").isEmpty()) {
					if (json.getLong("enforcedMinQuota") > json
							.getLong("enforcedMaxQuota")) {
						response.setStatus(GuiConstants.FAILURE);
						response.setMessage("Minimum Quota cannot be more than maximum Quota");
						return response;
					}

					resource.setEnforcedMinQuota(json
							.getLong("enforcedMinQuota"));
					resource.setEnforcedMaxQuota(json
							.getLong("enforcedMaxQuota"));
				}

			}
			if (!json.getBoolean("abstract") && !json.isNull("cost")
					&& !json.get("cost").equals("")) {
				MonetaryUnit monetoryUnit = new Cost(
						CurrencyCode.PHP.toString(), json.getLong("cost"));
				resource.setCost(monetoryUnit);
			}
			Owner owner = null;
			if (json.getString("ownerType") != null
					&& json.getString("ownerType").equalsIgnoreCase("select")) {
				if (json.getString("ownerType").equalsIgnoreCase("EndUser")) {
					owner = new EndUser(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"Market")) {
					owner = new Market(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase("Opco")) {
					owner = new Opco(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"OpcoGroup")) {
					owner = new OpcoGroup(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"Partner")) {
					owner = new Partner(json.getString("ownerType"));
				} else if (json.getString("ownerType").equalsIgnoreCase(
						"TenantMvno")) {
					owner = new TenantMvno(json.getString("ownerType"));
				}
			}
			resource.setOwner(owner);

			if (json.isNull("profileType")
					|| json.getString("profileType").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Select at least one Profile or Workflow");
				return response;
			}
			if (json.isNull("profileName")
					|| json.getString("profileName").isEmpty()) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Enter Profile name.");
				return response;
			}

			Class<?> profile = Class.forName(json.getString("profileType"));
			Constructor<?> cons = profile.getConstructor(String.class);
			Object object = cons.newInstance(json.getString("profileName"));

			Method[] methods = profile.getMethods();

			for (Method method : methods) {
				String methodName = method.getName();

				if (methodName.contains("set") && !methodName.equals("setName")) {
					methodName = methodName.substring(3, 4).toLowerCase()
							+ methodName.substring(4);

					boolean isFieldExist = true;

					if (json.isNull(methodName)
							|| !(json.get(methodName) instanceof String)
							|| json.getString(methodName).isEmpty()) {
						isFieldExist = false;
					}

					String parameterType = method.getGenericParameterTypes()[0]
							.toString();

					if (isFieldExist) {
						if (parameterType.toLowerCase().contains("int")) {
							method.invoke(object, json.getInt(methodName));
						} else if (parameterType.toLowerCase().contains("long")) {
							method.invoke(object, json.getLong(methodName));
						} else if (parameterType.toLowerCase().contains("date")) {
							String dateStr = json.getString(methodName);
							Date date = DateUtil.toDate(dateStr, "yyyy-MM-dd");
							method.invoke(object, date);
						} else if (parameterType.toLowerCase().contains(
								"string")) {
							method.invoke(object, json.getString(methodName));
						} else if (parameterType.toLowerCase().contains(
								"boolean")) {
							method.invoke(object, json.getBoolean(methodName));
						} else if (parameterType.toLowerCase().contains(
								"currencycode")) {
							method.invoke(object, CurrencyCode.valueOf(json
									.getString(methodName)));
						}
					} else {
						if (parameterType.toLowerCase().contains(
								"paminformation")) {
							if (!json.isNull("pamInformation")
									&& (json.get("pamInformation") instanceof JSONArray)) {
								List<PamInformation> pamInformationArrayList = new ArrayList<>();

								JSONArray pamInformationArray = json
										.getJSONArray("pamInformation");
								for (int i = 0; i < pamInformationArray
										.length(); i++) {
									JSONObject pamInformationJson = (JSONObject) pamInformationArray
											.get(i);

									PamInformation pamInformation = new PamInformation();

									if (!pamInformationJson
											.isNull("pamClassID")
											&& !pamInformationJson.getString(
													"pamClassID").isEmpty()) {
										pamInformation
												.setPamClassID(pamInformationJson
														.getInt("pamClassID"));
									}

									if (!pamInformationJson
											.isNull("pamIndicator")
											&& !pamInformationJson.getString(
													"pamIndicator").isEmpty()) {
										pamInformation
												.setPamIndicator(pamInformationJson
														.getInt("pamIndicator"));
									}

									if (!pamInformationJson
											.isNull("scheduleID")
											&& !pamInformationJson.getString(
													"scheduleID").isEmpty()) {
										pamInformation
												.setScheduleID(pamInformationJson
														.getInt("scheduleID"));
									}
									pamInformation
											.setPamServiceID(pamInformationJson
													.getInt("pamServiceID"));

									pamInformationArrayList.add(pamInformation);
								}

								if (parameterType.toLowerCase().contains(
										"paminformationlist")) {
									PamInformationList pamInformationList = new PamInformationList();

									pamInformationList
											.setPamInfolist(pamInformationArrayList);

									method.invoke(object, pamInformationList);
								} else {
									method.invoke(object,
											pamInformationArrayList);
								}

							}
						} else if (parameterType.toLowerCase().contains(
								"serviceoffering")) {
							if (!json.isNull("serviceOfferings")
									&& (json.get("serviceOfferings") instanceof JSONArray)) {
								List<ServiceOffering> serviceOfferingsArrayList = new ArrayList<>();

								JSONArray serviceOfferingsJsonArray = json
										.getJSONArray("serviceOfferings");
								for (int i = 0; i < serviceOfferingsJsonArray
										.length(); i++) {
									JSONObject serviceOfferingsJson = (JSONObject) serviceOfferingsJsonArray
											.get(i);

									ServiceOffering serviceOffering = new ServiceOffering();

									serviceOffering
											.setServiceOfferingId(serviceOfferingsJson
													.getInt("serviceOfferingId"));

									if (!serviceOfferingsJson
											.isNull("serviceOfferingActiveFlag")
											&& !serviceOfferingsJson
													.getString(
															"serviceOfferingActiveFlag")
													.isEmpty()) {
										serviceOffering
												.setServiceOfferingActiveFlag(serviceOfferingsJson
														.getBoolean("serviceOfferingActiveFlag"));
									}

									serviceOfferingsArrayList
											.add(serviceOffering);
								}

								method.invoke(object, serviceOfferingsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"dedicatedaccountreversal")) {
							if (!json.isNull("daReversals")
									&& (json.get("daReversals") instanceof JSONArray)) {
								List<DedicatedAccountReversal> daReversalsArrayList = new ArrayList<>();

								JSONArray daReversalsJsonArray = json
										.getJSONArray("daReversals");
								for (int i = 0; i < daReversalsJsonArray
										.length(); i++) {
									JSONObject daReversalsJson = (JSONObject) daReversalsJsonArray
											.get(i);

									DedicatedAccountReversal daReversal = new DedicatedAccountReversal();

									if (!daReversalsJson
											.isNull("amountToReverse")
											&& !daReversalsJson.getString(
													"amountToReverse")
													.isEmpty()) {
										daReversal
												.setAmountToReverse(daReversalsJson
														.getInt("amountToReverse"));
									}

									daReversal
											.setDedicatedAccountInformationID(daReversalsJson
													.getInt("dedicatedAccountInformationID"));

									if (!daReversalsJson
											.isNull("hoursToReverse")
											&& !daReversalsJson.getString(
													"hoursToReverse").isEmpty()) {
										daReversal
												.setHoursToReverse(daReversalsJson
														.getInt("hoursToReverse"));
									}

									daReversalsArrayList.add(daReversal);
								}

								method.invoke(object, daReversalsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"timerofferreversal")) {
							if (!json.isNull("toReversals")
									&& (json.get("toReversals") instanceof JSONArray)) {
								List<TimerOfferReversal> toReversalsArrayList = new ArrayList<>();

								JSONArray toReversalsJsonArray = json
										.getJSONArray("toReversals");
								for (int i = 0; i < toReversalsJsonArray
										.length(); i++) {
									JSONObject toReversalsJson = (JSONObject) toReversalsJsonArray
											.get(i);

									TimerOfferReversal toReversal = new TimerOfferReversal();

									if (!toReversalsJson
											.isNull("hoursToReverse")
											&& !toReversalsJson.getString(
													"hoursToReverse").isEmpty()) {
										toReversal
												.setHoursToReverse(toReversalsJson
														.getInt("hoursToReverse"));
									}

									toReversal.setOfferID(toReversalsJson
											.getInt("offerID"));
									toReversal
											.setDedicatedAccountInformationID(toReversalsJson
													.getInt("dedicatedAccountInformationID"));

									toReversalsArrayList.add(toReversal);
								}

								method.invoke(object, toReversalsArrayList);
							}
						} else if (parameterType.toLowerCase().contains(
								"offerselection")) {
							if (!json.isNull("offerSelection")
									&& (json.get("offerSelection") instanceof JSONArray)) {

								JSONArray offerSelectionJsonArray = json
										.getJSONArray("offerSelection");

								OfferSelection[] offerSelectionArray = new OfferSelection[offerSelectionJsonArray
										.length()];

								for (int i = 0; i < offerSelectionJsonArray
										.length(); i++) {
									JSONObject offerSelectionJson = (JSONObject) offerSelectionJsonArray
											.get(i);

									OfferSelection offerSelection = new OfferSelection();

									offerSelection
											.setOfferIDFirst(offerSelectionJson
													.getInt("offerIDFirst"));
									offerSelection
											.setOfferIDLast(offerSelectionJson
													.getInt("offerIDLast"));

									offerSelectionArray[i] = offerSelection;
								}

								method.invoke(object,
										new Object[] { offerSelectionArray });
							}
						}
					}
				}
			}

			if (this.profileRegistry.readProfile(json.getString("profileName")) == null) {
				this.profileRegistry
						.createProfile((FulfillmentProfile<?>) object);
			} else {
				this.profileRegistry
						.updateProfile((FulfillmentProfile<?>) object);
			}

			resource.addFulfillmentProfile(json.getString("profileName"));

			this.serviceRegistry.updateResource(resource);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;

	}

	@RequestMapping(value = "/deleteResource", method = RequestMethod.GET)
	public @ResponseBody Response deleteResource(
			ModelMap model,
			@RequestParam(value = "resourceName", required = true) String resourceName)
			throws FrameworkException {
		Response response = new Response();
		if (resourceName == null || resourceName.isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Resource name cannot be empty");
			return response;
		}
		try {
			Resource resource = this.serviceRegistry.readResource(resourceName);
			if (resource != null)
				this.serviceRegistry.deleteResource(resource);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (CatalogException e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	@RequestMapping(value = "/getAllResources", method = RequestMethod.GET)
	public @ResponseBody String getAllResources(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "pageSize", required = true) int pageSize)
			throws FrameworkException {
		// getting all resources from ccm files
		List<Resource> resources = this.serviceRegistry.getAllResources();
		logger.debug("Displaying size of the resource list " + resources.size());
		logger.debug("Displaying objects in the list" + resources.toString());

		if (search != null && !search.equals("")) {

			Iterator<Resource> iter = resources.iterator();

			Resource entry = null;

			while (iter.hasNext()) {
				entry = iter.next();
				if (!Pattern
						.compile(Pattern.quote(search),
								Pattern.CASE_INSENSITIVE)
						.matcher(entry.getName()).find()) {
					iter.remove();
				}
			}
		}

		int startIndex = (pageNumber - 1) * pageSize;

		int lastIndex = startIndex + pageSize;

		if (lastIndex > resources.size()) {
			lastIndex = resources.size();
		}

		List<Resource> subList = resources.subList(startIndex, lastIndex);

		String resourceList = null;
		Map<String, List<Resource>> map = new HashMap<>();
		map.put("resources", subList);
		ObjectMapper mapper = new ObjectMapper();
		try {
			resourceList = mapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resourceList;
	}

	@RequestMapping(value = "getAllResourcesForOwner/", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody String getAllResourcesForOwner(
			@ModelAttribute("resourceName") String ownerCommond,
			BindingResult result, SessionStatus status)
			throws FrameworkException {
		// getting all resources from ccm files
		String resourcesForOwner = "";
		Owner owner = new EndUser("Ankit");
		List<Resource> resourcesforOwner = (List<Resource>) this.serviceRegistry
				.getAllResourcesFor(owner);
		if (!resourcesforOwner.isEmpty()) {
			Map<String, List<Resource>> map = new HashMap<String, List<Resource>>();
			map.put("resourcesForOwner", resourcesforOwner);
			ObjectMapper mapper = new ObjectMapper();
			try {
				resourcesForOwner = mapper.writeValueAsString(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resourcesForOwner;
	}

	@RequestMapping(value = "getAllResourcesForResourceGroup/", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody String getAllResourcesForResourceGroup(
			@ModelAttribute("resourceGroupName") String resourceGroupName,
			BindingResult result, SessionStatus status)
			throws FrameworkException {
		// getting all resources from ccm files
		String resourceForGroup = "";
		List<Resource> resourcesForGroup = (List<Resource>) this.serviceRegistry
				.getAllResourcesFor(resourceGroupName);
		if (!resourcesForGroup.isEmpty()) {
			Map<String, List<Resource>> map = new HashMap<String, List<Resource>>();
			map.put("resourcesForOwner", resourcesForGroup);
			ObjectMapper mapper = new ObjectMapper();
			try {
				resourceForGroup = mapper.writeValueAsString(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resourceForGroup;
	}

	// TO:DO ---Exceptions handling method to be implemented
	/*
	 * @ExceptionHandler( value={NullPointerException.class,
	 * IllegalAccessException.class} )
	 */

	/*
	 * @ExceptionHandler(FrontEndException.class) public @ResponseBody String
	 * handleCustomException(FrontEndException exception) {
	 * 
	 * ModelAndView model = new ModelAndView("error/generic_error");
	 * model.addObject("errCode", ex.getErrCode()); model.addObject("errMsg",
	 * ex.getErrMsg()); return null; }
	 */

	/*
	 * ______________________________________________________________________________________________________________
	 * //Start of controllers implementation for ResourceGroupManager
	 * ____________________________________________________________________________________________________________
	 */

	// get resource group from resourceGroupStore.ccm file
	@RequestMapping(value = "/getResourceGroup", method = RequestMethod.GET)
	public @ResponseBody Response getResourceGroup(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName) {
		String jsonResource = null;
		Response response = new Response();
		Map<String, Byte> resourceGroup = null;
		try {
			resourceGroup = this.resourceGroupManager
					.getResourceGroup(resourceGroupName);
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonResource = mapper.writeValueAsString(resourceGroup);
				response.setStatus(GuiConstants.SUCCESS);
				response.setResponseString(jsonResource);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(jsonResource);
		}
		return response;
	}

	@RequestMapping(value = "/setResourceGroup", method = RequestMethod.GET, headers = { "Content-type=application/json" })
	public @ResponseBody Response setResourceGroup(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName,
			@RequestParam(value = "resourceName", required = true) String resourceName)
			throws FrameworkException {
		Response response = new Response();
		// convert json string to json object
		String[] strArray = resourceName.split(",");
		Map<String, Byte> resources = new TreeMap<String, Byte>();
		int resourcePriority = 1;
		for (int i = 0; i < strArray.length; i++) {
			resources.put(strArray[i], new Byte((byte) resourcePriority));
			resourcePriority++;
		}
		try {
			this.resourceGroupManager.setResourceGroup(resourceGroupName,
					resources);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Resource Group :" + resourceGroupName
					+ " has been created in Resource Group Manager");

		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());

		}
		return response;
	}

	// define resource group and priority in ResourceGroup Manager
	@RequestMapping(value = "/defineResourceGroupAndPriority", method = RequestMethod.GET)
	public @ResponseBody Response defineResourceGroupAndPriority(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName,
			@RequestParam(value = "resourceName", required = true) String resourceName,
			@RequestParam(value = "resourcePriority", required = true) Byte resourcePriority) {
		Response response = new Response();
		try {
			this.resourceGroupManager.defineResourceGroupAndPriority(
					resourceGroupName, resourceName, resourcePriority);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Resource:" + resourceName
					+ " has been assigned in Resource Group :"
					+ resourceGroupName);
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// unset(remove) resource group and priority in ResourceGroup Manager
	@RequestMapping(value = "/unsetResourceGroupAndPriority", method = RequestMethod.GET)
	public @ResponseBody Response unsetResourceGroupAndPriority(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName,
			@RequestParam(value = "resourceName", required = true) String resourceName) {
		Response response = new Response();
		try {
			this.resourceGroupManager.unsetResourceGroupAndPriority(
					resourceGroupName, resourceName);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Resource:" + resourceName
					+ " has been reset from ResourceGroup :"
					+ resourceGroupName);
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// get resource priority based on resourceGroupName and ResourceName
	// get resource group from resourceGroupStore.ccm file
	@RequestMapping(value = "/getResourcePriority", method = RequestMethod.GET)
	public @ResponseBody Response getResourcePriority(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName,
			@RequestParam(value = "resourceName", required = true) String resourceName) {
		String jsonResource = null;
		Response response = new Response();
		Byte resourcePriority = null;
		try {
			resourcePriority = this.resourceGroupManager.getResourcePriority(
					resourceGroupName, resourceName);
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonResource = mapper.writeValueAsString(resourcePriority);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Fetch the resource priority from resource group");
				response.setResponseString("Resource Priority is:"
						+ jsonResource);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(jsonResource);
		}
		return response;
	}

	// get all resource group from resourceGroupStore.ccm file
	@RequestMapping(value = "/getAllResourceGroups", method = RequestMethod.GET)
	public @ResponseBody Response getAllResourceGroups()
			throws FrameworkException {
		// getting all resource group from resourceGroupStore.ccm file
		Response response = new Response();
		String resourceGroupsString = null;
		try {
			Map<String, Map<String, Byte>> resourceGroups = this.resourceGroupManager
					.getAllResourceGroup();
			ObjectMapper mapper = new ObjectMapper();
			try {
				resourceGroupsString = mapper
						.writeValueAsString(resourceGroups);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Resource Group Count :"
						+ resourceGroups.size()
						+ "Fetched from Resource Group Manager");
				response.setResponseString(resourceGroupsString);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(resourceGroupsString);
		}
		return response;
	}

	// delete resource group Resource Group manager
	@RequestMapping(value = "/deleteResourceGroup", method = RequestMethod.GET)
	public @ResponseBody Response deleteResourceGroup(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName) {
		Response response = new Response();
		try {
			this.resourceGroupManager.deleteResourceGroup(resourceGroupName);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Resource Group:" + resourceGroupName
					+ "has been deleted from ResourceGroup Manager");
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/isResourceGroupExists", method = RequestMethod.GET)
	public @ResponseBody Response isResourceGroupExists(
			ModelMap model,
			@RequestParam(value = "resourceGroupName", required = true) String resourceGroupName) {
		boolean isResourceGroupExists = Boolean.FALSE;
		Response response = new Response();
		// Right now we don't have exception class implemented,to be done later
		if (resourceGroupName == null)
			throw new FrontEndException(
					"The OfferGroup Name provided was null", "");
		try {
			isResourceGroupExists = this.resourceGroupManager
					.isResourceGroupExists(resourceGroupName);
			if (isResourceGroupExists) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Resource Group [" + resourceGroupName
						+ "] is created.");
			} else {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Duplicate Resource Group ["
						+ resourceGroupName + "] cannot be created.");
			}
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	/*
	 * ______________________________________________________________________________________________________________
	 * //End of controllers implementation for ResourceGroupManager
	 * ____________________________________________________________________________________________________________
	 */

	/*
	 * ______________________________________________________________________________________________________________
	 * //Start of controllers implementation for OfferCatalogGroupManager
	 * ____________________________________________________________________________________________________________
	 */
	@RequestMapping(value = "/setOfferGroup", method = RequestMethod.POST)
	public @ResponseBody Response setOfferGroup(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName,
			@RequestParam(value = "offerName", required = true) String offerName)
			throws FrameworkException {
		Response response = new Response();
		// convert json string to json object
		String[] strArray = offerName.split(",");
		Map<String, Byte> offers = new TreeMap<String, Byte>();
		int offerPriority = 1;
		for (int i = 0; i < strArray.length; i++) {
			offers.put(strArray[i], new Byte((byte) offerPriority));
			offerPriority++;
		}
		try {
			this.offerGroupManager.setOfferGroup(offerGroupName, offers);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Offer Group :" + offerGroupName
					+ " has been created in Offer Group Manager");

		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// get all resource group from offerGroupStore.ccm file
	@RequestMapping(value = "/getAllOffersGroups", method = RequestMethod.GET)
	public @ResponseBody Response getAllOffersGroups()
			throws FrameworkException {
		// getting all offer group from offerGroupStore.ccm file
		Response response = new Response();
		String offerGroupsString = null;
		try {
			Map<String, Map<String, Byte>> offerGroups = this.offerGroupManager
					.getAllOfferGroups();
			ObjectMapper mapper = new ObjectMapper();
			try {
				offerGroupsString = mapper.writeValueAsString(offerGroups);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Offer Group Count :" + offerGroups.size()
						+ "Fetched from Offer Group Manager");
				response.setResponseString(offerGroupsString);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(offerGroupsString);
		}
		return response;
	}

	// get resource group from offerGroupStore.ccm file
	@RequestMapping(value = "/getOfferGroup", method = RequestMethod.GET)
	public @ResponseBody Response getOfferGroup(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName) {
		String jsonResource = null;
		Response response = new Response();
		Map<String, Byte> offerGroup = null;
		try {
			offerGroup = this.offerGroupManager.getOfferGroup(offerGroupName);
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonResource = mapper.writeValueAsString(offerGroup);
				response.setStatus(GuiConstants.SUCCESS);
				response.setResponseString(jsonResource);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(jsonResource);
		}
		return response;
	}

	// define offer group and priority in OfferGroup Manager
	@RequestMapping(value = "/defineOfferGroupAndPriority", method = RequestMethod.GET)
	public @ResponseBody Response defineOfferGroupAndPriority(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName,
			@RequestParam(value = "offerName", required = true) String offerName,
			@RequestParam(value = "offerPriority", required = true) Byte offerPriority) {
		Response response = new Response();
		/*
		 * String defaultResourceGroupName = "ResourceGrp1"; String
		 * defaultResourceName = "cxvxcvcx"; Byte defaultResourcePriority = new
		 * Byte((byte) 4);
		 */
		try {
			this.offerGroupManager.defineOfferGroupAndPriority(offerGroupName,
					offerName, offerPriority);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Offer:" + offerName
					+ " has been assigned in Offer Group :" + offerGroupName);
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// unset(remove) offer group and priority in OfferGroup Manager
	@RequestMapping(value = "/unsetOfferGroupAndPriority", method = RequestMethod.GET)
	public @ResponseBody Response unsetOfferGroupAndPriority(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName,
			@RequestParam(value = "offerName", required = true) String offerName) {
		Response response = new Response();
		try {
			this.offerGroupManager.unsetOfferGroupAndPriority(offerGroupName,
					offerName);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Offer:" + offerName
					+ " has been reset from Offer Group :" + offerGroupName);
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// get offer priority based on offerGroupName and offerName
	@RequestMapping(value = "/getOfferPriority", method = RequestMethod.GET)
	public @ResponseBody Response getOfferPriority(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName,
			@RequestParam(value = "offerName", required = true) String offerName) {
		String jsonResource = null;
		Response response = new Response();
		Byte offerPriority = null;
		try {
			offerPriority = this.offerGroupManager.getOfferPriority(
					offerGroupName, offerName);
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonResource = mapper.writeValueAsString(offerPriority);
				response.setStatus(GuiConstants.SUCCESS);
				response.setResponseString(jsonResource);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(jsonResource);
		}
		return response;
	}

	// delete offer group Offer Group manager
	@RequestMapping(value = "/deleteOfferGroup", method = RequestMethod.GET)
	public @ResponseBody Response deleteOfferGroup(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName) {
		Response response = new Response();
		try {
			this.offerGroupManager.deleteOfferGroup(offerGroupName);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Offer Group:" + offerGroupName
					+ "has been deleted from Offer Group Manager");
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/isOfferGroupExists", method = RequestMethod.GET)
	public @ResponseBody Response checkIfOfferGroupExists(
			ModelMap model,
			@RequestParam(value = "offerGroupName", required = true) String offerGroupName) {
		boolean isOfferGroupExists = Boolean.FALSE;
		Response response = new Response();
		// Right now we don't have exception class implemented,to be done later
		if (offerGroupName == null)
			throw new FrontEndException(
					"The OfferGroup Name provided was null", "");
		try {
			isOfferGroupExists = this.offerGroupManager
					.isOfferGroupExists(offerGroupName);
			if (isOfferGroupExists) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Offer Group [" + offerGroupName
						+ "] is created.");
			} else {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Duplicate OfferGroup [" + offerGroupName
						+ "] cannot be created.");
			}
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	/*
	 * ______________________________________________________________________________________________________________
	 * //End of controllers implementation for OfferCatalogGroupManager
	 * ____________________________________________________________________________________________________________
	 */

	/*
	 * ______________________________________________________________________________________________________________
	 * //Start of controllers implementation for Owner Manager
	 * ____________________________________________________________________________________________________________
	 */
	@RequestMapping(value = "/createOwner", method = RequestMethod.POST)
	public @ResponseBody Response createOwner(@RequestBody String inputString) {
		Response response = new Response();
		JSONObject json = new JSONObject(inputString.trim());
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Owner Name Cannot be Empty");
			return response;
		}
		Owner owner = null;

		switch (json.getString("ownerType")) {

		case "Opco": {
			try {
				owner = new Opco(json.getString("name"));

				Market opcoMarket = new Market(json.getString("marketName"));
				TenantMvno opcoMarketTenant = new TenantMvno(
						json.getString("tenantName2"));
				Partner opcoMarketPartner = new Partner(
						json.getString("partnerName6"));
				opcoMarketTenant.addPartner(new Partner(json
						.getString("partnerName5")));
				opcoMarket.addTenant(opcoMarketTenant);
				opcoMarket.addPartner(opcoMarketPartner);
				owner.addMarket(opcoMarket);

				TenantMvno opcoTenantMvno = new TenantMvno(
						json.getString("tenantName3"));
				opcoTenantMvno.addPartner(new Partner(json
						.getString("partnerName7")));
				owner.addTenant(opcoTenantMvno);

				owner.addPartner(new Partner(json.getString("partnerName8")));

				// owner.addOpco(opco);
			} catch (CatalogException e) {

			}
			break;

		}
		case "Opco Group": {
			try {
				owner = new OpcoGroup(json.getString("name"));

				Opco opco = new Opco(json.getString("opcoName"));

				Market opcoMarket = new Market(json.getString("marketName"));
				TenantMvno opcoMarketTenant = new TenantMvno(
						json.getString("tenantName"));
				Partner opcoMarketPartner = new Partner(
						json.getString("partnerName1"));
				opcoMarketTenant.addPartner(new Partner(json
						.getString("partnerName")));
				opcoMarket.addTenant(opcoMarketTenant);
				opcoMarket.addPartner(opcoMarketPartner);
				opco.addMarket(opcoMarket);

				TenantMvno opcoTenantMvno = new TenantMvno(
						json.getString("tenantName1"));
				opcoTenantMvno.addPartner(new Partner(json
						.getString("partnerName2")));
				opco.addTenant(opcoTenantMvno);

				opco.addPartner(new Partner(json.getString("partnerName3")));

				owner.addOpco(opco);
				owner.addPartner(new Partner(json.getString("partnerName4")));

			} catch (CatalogException e) {

			}

			break;
		}

		default: {
			owner = null;
			break;
		}
		}
		try {
			ownerManager.createOwner(owner);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Owner has been created in owner manager");

		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// read owner from ownerStore.ccm file
	@RequestMapping(value = "/readOwner", method = RequestMethod.GET)
	public @ResponseBody Response readOwner(ModelMap model,
			@RequestParam(value = "ownerName", required = true) String ownerName) {
		String jsonResource = null;
		Response response = new Response();
		Owner owner = null;
		try {
			owner = ownerManager.readOwner(ownerName);

			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonResource = mapper.writeValueAsString(owner);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Owner has been read from owner manager");
				response.setResponseString(jsonResource);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			response.setResponseString(jsonResource);
		}
		return response;
	}

	// get all existing owners from ownerStore.ccm file
	@RequestMapping(value = "/getAllOwners", method = RequestMethod.GET)
	public @ResponseBody Response getAllOwners() throws FrameworkException {
		// getting all offer group from offerGroupStore.ccm file
		Response response = new Response();
		String jsonOwnerResponse = null;
		List<Owner> fetchOwnerChilds = new ArrayList<Owner>();
		try {
			Map<String, Owner> owners = ownerManager.getAllOwners();

			Owner ownerParent = null;
			for (Map.Entry<String, Owner> owner : owners.entrySet()) {
				ownerParent = owner.getValue();
				fetchOwnerChilds.add(ownerParent);

			}

			Map<String, List<Owner>> map = new HashMap<String, List<Owner>>();
			map.put("owners", fetchOwnerChilds);
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonOwnerResponse = mapper.writeValueAsString(map);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Fetch the owners from ownerStore.ccm file");
				response.setResponseString(jsonOwnerResponse);
			} catch (Exception e) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(e.getMessage());
			}
		} catch (CatalogException e1) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		}
		return response;
	}

	// delete owner from owner manager
	@RequestMapping(value = "/deleteOwner", method = RequestMethod.GET)
	public @ResponseBody Response deleteOwner(ModelMap model,
			@RequestParam(value = "ownerName", required = true) String ownerName) {
		Response response = new Response();
		try {
			Owner owner = null;
			String ownerType = "OpcoGroup";
			switch (ownerType) {

			case "Opco": {
				owner = new Opco(ownerName);
				break;
			}
			case "OpcoGroup": {
				owner = new OpcoGroup(ownerName);

				break;
			}

			default: {
				owner = null;
				break;
			}
			}
			ownerManager.deleteOwner(owner);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Owner has been deleted in Owner Manager");
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/isOwnerExists", method = RequestMethod.GET)
	public @ResponseBody Response checkIfOwnerExists(ModelMap model,
			@RequestParam(value = "ownerName", required = true) String ownerName) {
		boolean isOwnerExists = Boolean.FALSE;
		Response response = new Response();
		// Right now we don't have exception class implemented,to be done later
		if (ownerName == null)
			throw new FrontEndException("The OfferId provided was null", "");
		try {
			isOwnerExists = ownerManager.isOwnerExists(ownerName);
			if (isOwnerExists) {
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage("Owner [" + ownerName + "] is created.");
			} else {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Duplicate Owner [" + ownerName
						+ "] cannot be created.");
			}
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	// update owner from ownerManager
	@RequestMapping(value = "/updateOwner", method = RequestMethod.POST)
	public @ResponseBody Response updateOwner(@RequestBody String inputString) {
		Response response = new Response();
		JSONObject json = new JSONObject(inputString.trim());
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Owner Name Cannot be Empty");
			return response;
		}

		Owner owner = null;

		switch (json.getString("ownerType")) {

		case "Opco": {
			try {
				owner = new Opco(json.getString("name"));

				Market opcoMarket = new Market(json.getString("marketName"));
				TenantMvno opcoMarketTenant = new TenantMvno(
						json.getString("tenantName2"));
				Partner opcoMarketPartner = new Partner(
						json.getString("partnerName6"));
				opcoMarketTenant.addPartner(new Partner(json
						.getString("partnerName5")));
				opcoMarket.addTenant(opcoMarketTenant);
				opcoMarket.addPartner(opcoMarketPartner);
				owner.addMarket(opcoMarket);

				TenantMvno opcoTenantMvno = new TenantMvno(
						json.getString("tenantName3"));
				opcoTenantMvno.addPartner(new Partner(json
						.getString("partnerName7")));
				owner.addTenant(opcoTenantMvno);

				owner.addPartner(new Partner(json.getString("partnerName8")));

			} catch (CatalogException e) {

			}
			break;

		}
		case "OpcoGroup": {
			try {
				owner = new OpcoGroup(json.getString("name"));

				Opco opco = new Opco(json.getString("opcoName"));

				Market opcoMarket = new Market(json.getString("marketName"));
				TenantMvno opcoMarketTenant = new TenantMvno(
						json.getString("tenantName"));
				Partner opcoMarketPartner = new Partner(
						json.getString("partnerName1"));
				opcoMarketTenant.addPartner(new Partner(json
						.getString("partnerName")));
				opcoMarket.addTenant(opcoMarketTenant);
				opcoMarket.addPartner(opcoMarketPartner);
				opco.addMarket(opcoMarket);

				TenantMvno opcoTenantMvno = new TenantMvno(
						json.getString("tenantName1"));
				opcoTenantMvno.addPartner(new Partner(json
						.getString("partnerName2")));
				opco.addTenant(opcoTenantMvno);

				opco.addPartner(new Partner(json.getString("partnerName3")));

				owner.addOpco(opco);
				owner.addPartner(new Partner(json.getString("partnerName4")));

			} catch (CatalogException e) {

			}

			break;
		}

		default: {
			owner = null;
			break;
		}
		}

		try {
			ownerManager.updateOwner(owner);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Owner has been updated in owner manager");
			// response.setResponseString(isOwnerCreated);

		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}
	/*
	 * ______________________________________________________________________________________________________________
	 * //End of controllers implementation for Owner Manager
	 * ____________________________________________________________________________________________________________
	 */

}