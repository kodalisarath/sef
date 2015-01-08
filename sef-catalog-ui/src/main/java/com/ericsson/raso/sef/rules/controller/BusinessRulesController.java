package com.ericsson.raso.sef.rules.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.RuleManager;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.core.DateUtil;
import com.ericsson.raso.sef.ruleengine.Action;
import com.ericsson.raso.sef.ruleengine.Condition;
import com.ericsson.raso.sef.ruleengine.ContainsCondition;
import com.ericsson.raso.sef.ruleengine.DoesNotContainCondition;
import com.ericsson.raso.sef.ruleengine.DoesNotEndWithCondition;
import com.ericsson.raso.sef.ruleengine.DoesNotStartWithCondition;
import com.ericsson.raso.sef.ruleengine.EndsWithCondition;
import com.ericsson.raso.sef.ruleengine.EnumeratedCondition;
import com.ericsson.raso.sef.ruleengine.EqualsCondition;
import com.ericsson.raso.sef.ruleengine.ExclusionCondition;
import com.ericsson.raso.sef.ruleengine.GreaterThanCondition;
import com.ericsson.raso.sef.ruleengine.GreaterThanOrEqualsCondition;
import com.ericsson.raso.sef.ruleengine.LesserThanCondition;
import com.ericsson.raso.sef.ruleengine.LesserThanOrEqualsCondition;
import com.ericsson.raso.sef.ruleengine.LogicType;
import com.ericsson.raso.sef.ruleengine.MatchesCondition;
import com.ericsson.raso.sef.ruleengine.NotEqualsCondition;
import com.ericsson.raso.sef.ruleengine.NotInRangeCondition;
import com.ericsson.raso.sef.ruleengine.NotMatchesCondition;
import com.ericsson.raso.sef.ruleengine.RangeCondition;
import com.ericsson.raso.sef.ruleengine.Rule;
import com.ericsson.raso.sef.ruleengine.RuleFailedException;
import com.ericsson.raso.sef.ruleengine.RuleUnit;
import com.ericsson.raso.sef.ruleengine.StartsWithCondition;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class BusinessRulesController {
	private RuleManager rulesManager = null;

	public BusinessRulesController() {
		this.rulesManager = new RuleManager();
	}

	@RequestMapping(value = "/createRule", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody
	Response createRule(@RequestBody String inputString) {
		Response response = new Response();
		try {
			JSONObject json = new JSONObject(inputString.trim());
			Rule ruleObj = prepareRuleObj(json);
			if (ruleObj != null) {
				this.rulesManager.createRule(ruleObj);
				response.setStatus(GuiConstants.SUCCESS);
			}
		} catch (CatalogException | JSONException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;

	}

	@RequestMapping(value = "/updateRule", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody
	Response updateRule(@RequestBody String inputString) {
		Response response = new Response();
		try {
			JSONObject json = new JSONObject(inputString.trim());
			Rule ruleObj = prepareRuleObj(json);
			if (ruleObj != null) {
				this.rulesManager.updateRule(ruleObj);
				response.setStatus(GuiConstants.SUCCESS);
			}
		} catch (CatalogException | JSONException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/deleteRule", method = RequestMethod.GET)
	public @ResponseBody
	Response deleteRule(ModelMap model,
			@RequestParam(value = "ruleName", required = true) String ruleName) {
		Response response = new Response();
		if (ruleName == null || ruleName.isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Rule name cannot be empty");
			return response;
		}
		try {
			this.rulesManager.deleteRule(ruleName);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (CatalogException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/readRule", method = RequestMethod.GET)
	public @ResponseBody
	Response readRule(ModelMap model,
			@RequestParam(value = "ruleName", required = true) String ruleName) {
		Response response = new Response();
		if (ruleName == null || ruleName.isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Rule name cannot be empty");
			return response;
		}
		Rule rule = null;
		try {
			rule = this.rulesManager.readRule(ruleName);
			response.setStatus(GuiConstants.SUCCESS);
			ObjectMapper mapper = new ObjectMapper();
			String result;
			result = mapper.writeValueAsString(rule);
			response.setResponseString(result);

		} catch (CatalogException | IOException e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/readAllRules", method = RequestMethod.GET)
	public @ResponseBody
	Response readAllRules() {
		Response response = new Response();
		try {
			Map<String, Rule> ruleMap = this.rulesManager.readAllRules();
			response.setStatus(GuiConstants.SUCCESS);
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(ruleMap);
			response.setResponseString(result);

		} catch (CatalogException | IOException e) {
			e.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// Private method to prepare the Rule object as this is similar for
	// create/update
	private Rule prepareRuleObj(JSONObject json) throws CatalogException {

		if (json.getString("name") == null || json.getString("name").isEmpty())
			throw new CatalogException("Rule name cannot be empty");
		Rule rule = new Rule();
		try {
			rule.setName(json.getString("name"));
			rule.setLogicGate(LogicType.valueOf(json.getString("gate")));
			JSONArray str = json.getJSONArray("ruleUnits");
			JSONArray rulesArray = json.getJSONArray("rules");
			List<Action> listRuleSet = new ArrayList<>();

			if (str.length() == 0 && rulesArray.length() == 0)
				throw new CatalogException(
						"Rule should contains either of the one :Rule or Rule units");

			for (int i = 0; i < rulesArray.length(); i++) {
				Rule ruleAction = this.rulesManager.readRule(rulesArray
						.getString(i));
				if (ruleAction != null)
					listRuleSet.add(ruleAction);
			}
			rule.setRuleset(listRuleSet);
			for (int index = 0; index < str.length(); index++) {

				JSONObject jsonObj = str.getJSONObject(index);
				RuleUnit ruleUnit = new RuleUnit(jsonObj.getString("unitName"));
				String conditionInput = jsonObj.getString("condition");
				String ruleVariable = jsonObj.getString("ruleVariable");

				if (conditionInput == null || conditionInput.isEmpty()
						|| ruleVariable == null || ruleVariable.isEmpty())
					throw new CatalogException(
							"Rule cannot be saved/Updated without the rule unit attributes");

				ruleUnit.setBaseObjectName(jsonObj.getString("baseObject"));
				ruleUnit.setSchemaName(jsonObj.getString("schemaName"));
				ruleUnit.setRuleVariable(jsonObj.getString("ruleVariable"));
				Condition condition = null;
				if (jsonObj.getString("condition").equalsIgnoreCase(
						"ENUMERATED")) {

					List<String> enumeratedValues = new ArrayList<String>();

					if (jsonObj.get("enumeratedValue") instanceof JSONArray) {
						JSONArray arry = jsonObj
								.getJSONArray("enumeratedValue");

						for (int i = 0; i < arry.length(); i++) {
							enumeratedValues.add(arry.getString(i));
						}
					} else {
						enumeratedValues.add(jsonObj
								.getString("enumeratedValue"));
					}

					condition = new EnumeratedCondition(enumeratedValues);
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"EXCLUSION_LIST")) {

					List<String> enumeratedValues = new ArrayList<String>();

					if (jsonObj.get("exclusionList") instanceof JSONArray) {
						JSONArray arry = jsonObj.getJSONArray("exclusionList");

						for (int i = 0; i < arry.length(); i++) {
							enumeratedValues.add(arry.getString(i));
						}
					} else {
						enumeratedValues
								.add(jsonObj.getString("exclusionList"));
					}
					condition = new ExclusionCondition(enumeratedValues);
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"RANGE")) {
					condition = new RangeCondition(
							jsonObj.getString("lowerBound"),
							jsonObj.getString("upperBound"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"NOT_IN_RANGE")) {
					condition = new NotInRangeCondition(
							jsonObj.getString("lowerBound"),
							jsonObj.getString("upperBound"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"STARTS_WITH")) {
					condition = new StartsWithCondition(
							jsonObj.getString("startWithPattern"));
					condition.evaluate(jsonObj.getString("startWithPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"DOESNT_START_WITH")) {
					condition = new DoesNotStartWithCondition(
							jsonObj.getString("notStartWithPattern"));
					condition
							.evaluate(jsonObj.getString("notStartWithPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"ENDS_WITH")) {
					condition = new EndsWithCondition(
							jsonObj.getString("endWithPattern"));
					condition.evaluate(jsonObj.getString("endWithPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"DOESNT_END_WITH")) {
					condition = new DoesNotEndWithCondition(
							jsonObj.getString("notEndWith"));
					condition.evaluate(jsonObj.getString("notEndWith"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"CONTAINS")) {
					condition = new ContainsCondition(
							jsonObj.getString("containWithPattern"));
					condition.evaluate(jsonObj.getString("containWithPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"DOESNT_CONTAIN")) {
					condition = new DoesNotContainCondition(
							jsonObj.getString("notContainWithPattern"));
					condition.evaluate(jsonObj
							.getString("notContainWithPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"MATCHES")) {
					condition = new MatchesCondition(
							jsonObj.getString("matchesPattern"));
					condition.evaluate(jsonObj.getString("matchesPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"NOT_MATCHES")) {
					condition = new NotMatchesCondition(
							jsonObj.getString("notMatchesPattern"));
					condition.evaluate(jsonObj.getString("notMatchesPattern"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"EQUALS")) {
					condition = new EqualsCondition(
							jsonObj.getString("ruleCondition"));
					condition.evaluate(jsonObj.getString("ruleCondition"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"NOT_EQUALS")) {
					condition = new NotEqualsCondition(
							jsonObj.getString("ruleCondition"));
					condition.evaluate(jsonObj.getString("ruleCondition"));
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"GREATER_THAN")) {
					if (jsonObj.getString("conditionValue").equalsIgnoreCase(
							"number")) {
						Integer ruleValue = Integer.valueOf(jsonObj
								.getString("conditionValueNumber"));
						condition = new GreaterThanCondition(ruleValue);
						condition.evaluate(ruleValue);
					} else if (jsonObj.getString("conditionValue")
							.equalsIgnoreCase("date")) {
						Date date = null;
						date = DateUtil.toDate(
								jsonObj.getString("conditionValueDate"),
								"yyyy-MM-dd");
						condition = new GreaterThanCondition(date);
						condition.evaluate(date);
					}

				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"GREATER_THAN_OR_EQUALS")) {
					if (jsonObj.getString("conditionValue").equalsIgnoreCase(
							"number")) {
						Integer ruleValue = Integer.valueOf(jsonObj
								.getString("conditionValueNumber"));
						condition = new GreaterThanOrEqualsCondition(ruleValue);
						condition.evaluate(ruleValue);
					} else if (jsonObj.getString("conditionValue")
							.equalsIgnoreCase("date")) {
						Date date = null;
						date = DateUtil.toDate(
								jsonObj.getString("conditionValueDate"),
								"yyyy-MM-dd");
						condition = new GreaterThanOrEqualsCondition(date);
						condition.evaluate(date);
					}
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"LESSER_THAN")) {
					if (jsonObj.getString("conditionValue").equalsIgnoreCase(
							"number")) {
						Integer ruleValue = Integer.valueOf(jsonObj
								.getString("conditionValueNumber"));
						condition = new LesserThanCondition(ruleValue);
						condition.evaluate(ruleValue);
					} else if (jsonObj.getString("conditionValue")
							.equalsIgnoreCase("date")) {
						Date date = null;
						date = DateUtil.toDate(
								jsonObj.getString("conditionValueDate"),
								"yyyy-MM-dd");
						condition = new LesserThanCondition(date);
						condition.evaluate(date);
					}
				} else if (jsonObj.getString("condition").equalsIgnoreCase(
						"LESSER_THAN_OR_EQUALS")) {
					if (jsonObj.getString("conditionValue").equalsIgnoreCase(
							"number")) {
						Integer ruleValue = Integer.valueOf(jsonObj
								.getString("conditionValueNumber"));
						condition = new LesserThanOrEqualsCondition(ruleValue);
						condition.evaluate(ruleValue);
					} else if (jsonObj.getString("conditionValue")
							.equalsIgnoreCase("date")) {
						Date date = null;
						date = DateUtil.toDate(
								jsonObj.getString("conditionValueDate"),
								"yyyy-MM-dd");
						condition = new LesserThanOrEqualsCondition(date);
						condition.evaluate(date);
					}
				}
				ruleUnit.setEvaluate(condition);
				listRuleSet.add(ruleUnit);
			}
			return rule;
		} catch (RuleFailedException | CatalogException | JSONException
				| ParseException e) {
			e.printStackTrace();
			throw new CatalogException(e.getMessage());
		}
	}
}
