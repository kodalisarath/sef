package com.ericsson.raso.sef.management.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
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

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.auth.service.UserStoreService;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.core.FrameworkException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GroupController {
	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(GroupController.class);
	 */
	static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(GroupController.class.getName());
	private IUserStore userStoreService = null;
	private IPrivilegeManager privilegeManager = null;

	public GroupController() {
		userStoreService = new UserStoreService();
		privilegeManager = new PrivilegeManager(
				System.getenv("SEF_CATALOG_HOME") + "/permission.ccm");
	}

	// create group in user store service
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody
	Response createGroup(@RequestBody String inputString) {
		Response response = new Response();
		JSONObject json = new JSONObject(inputString.trim());
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			logger.debug("Debug:Group Name cannot be empty");
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Group name cannot be empty");
			return response;
		}
		try {
			Group group = new Group(json.getString("name"));
			group.setDescription(json.getString("description"));
			List<Privilege> privilegeList = preparePrivileges(json);
			/*
			 * JSONArray jsonArray=json.getJSONArray("privileges"); for(int
			 * index=0;index<jsonArray.length();index++){ Privilege
			 * privilege=new
			 * Permission(AuthorizationPrinciple.valueOf(jsonArray.
			 * getString(index))); privilegeList.add(privilege); }
			 */
			if (privilegeList.isEmpty()) {
				logger.debug("Debug:Atleast a Privilege to be defined to  this group");
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Atleast a Privilege to be defined to  this group");
				return response;
			}
			group.setPrivileges(privilegeList);
			userStoreService.createGroup(group);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (FrameworkException | JSONException e) {
			logger.error("Error creating the group " + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	// read group from user store service
	@RequestMapping(value = "/readGroup", method = RequestMethod.GET)
	public @ResponseBody
	Response readGroup(ModelMap model,
			@RequestParam(value = "groupName", required = true) String groupName) {
		Response response = new Response();
		try {
			Group group = userStoreService.readGroup(groupName);
			ObjectMapper mapper = new ObjectMapper();
			String val = mapper.writeValueAsString(group);
			logger.debug("Completed reading a group from database");
			response.setMessage("Read Group from user store service: ");
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(val);
		} catch (FrameworkException | IOException e) {
			logger.error("Error reading a group from database" + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			response.setResponseString("");
		}
		return response;
	}

	// call delete group in user store service
	@RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
	public @ResponseBody
	Response deleteGroup(ModelMap model,
			@RequestParam(value = "groupName", required = true) String groupName) {
		Response response = new Response();
		if (groupName == null || groupName.isEmpty()) {
			logger.debug("Debug:Group Name Cannot be Empty");
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Group Name Cannot be Empty");
			return response;
		}
		try {
			Group group = new Group(groupName);
			userStoreService.deleteGroup(group);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (FrameworkException e) {
			logger.error("Error deleting a group from database" + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/getAllGroups", method = RequestMethod.GET)
	// headers="Accept=application/json")
	public @ResponseBody
	Response getAllGroups(
			ModelMap model,
			@RequestParam(value = "searchQuery", required = false) String searchQuery,
			@RequestParam(value = "pageNumber", required = true) String pageNumber,
			@RequestParam(value = "pageSize", required = true) String pageSize) {
		Response response = new Response();
		if (searchQuery == null) {
			searchQuery = "";
		}
		String val = null;
		try {
			List<Group> groups = userStoreService.readAllGroups(searchQuery,
					pageNumber, pageSize);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, List<Group>> map = new HashMap<String, List<Group>>();
			map.put("groups", groups);
			val = mapper.writeValueAsString(map);
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(val);
		} catch (FrameworkException | IOException e) {
			logger.error("Error reading  all groups from database" + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/getAllPrivileges", method = RequestMethod.GET)
	public @ResponseBody
	Response getAllPrivileges() {
		String val = "";
		Response response = new Response();
		try {
			Map<AuthorizationPrinciple, Privilege> privileges = privilegeManager
					.getGuiPrivileges();
			Map<String, Map<AuthorizationPrinciple, Privilege>> map = new HashMap<String, Map<AuthorizationPrinciple, Privilege>>();
			map.put("privileges", privileges);
			ObjectMapper mapper = new ObjectMapper();
			val = mapper.writeValueAsString(map);
			response.setMessage("Create Group from user store service: ");
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(val);

		} catch (AuthAdminException | IOException e) {
			logger.error("Error reading all privileges " + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			logger.error("Error reading all privileges " + e);
		}
		return response;
	}

	// check if a group exists
	@RequestMapping(value = "/isGroupExists", method = RequestMethod.GET)
	public @ResponseBody
	Response isGroupExists(ModelMap model,
			@RequestParam(value = "groupName", required = true) String groupName)
			throws FrameworkException {
		Response response = new Response();
		if (!groupName.isEmpty() && groupName != null) {
			boolean groupExists = false;
			try {
				groupExists = userStoreService.isGroupExists(groupName);
				response.setStatus(GuiConstants.SUCCESS);
				response.setMessage(String.valueOf(groupExists));

			} catch (FrameworkException e) {
				logger.error("Error checking if group exists in the database"
						+ e);
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e.getMessage());
			}
		}
		return response;
	}

	// read group from user store service
	@RequestMapping(value = "/updateGroupPrivilege", method = RequestMethod.POST)
	public @ResponseBody
	Response deleteGroupPrivilege(@RequestBody String groupString) {
		Response response = new Response();
		JSONObject json = new JSONObject(groupString);
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			logger.debug("Debug:Group Name cannot be empty");
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Group Name cannot be empty");
			return response;
		}
		if (json.getJSONArray("privileges") == null
				|| json.getJSONArray("privileges").length() < 1) {
			logger.debug("Debug:You must select any privilege to perform this operation");
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("You must select any privilege to perform this operation");
			return response;
		}
		Group group = new Group(json.getString("name"));
		JSONArray jsonArray = json.getJSONArray("privileges");
		List<Privilege> privilegeList = new ArrayList<Privilege>();
		for (int index = 0; index < jsonArray.length(); index++) {
			Privilege privilege = new Permission(
					AuthorizationPrinciple.valueOf(jsonArray.getString(index)));
			privilegeList.add(privilege);
		}
		group.setPrivileges(privilegeList);
		try {
			if (json.getString("action").equalsIgnoreCase("insert")) {
				logger.debug("Debug:It is a insertion");
				userStoreService.addPrivileges(group);
			} else if (json.getString("action").equalsIgnoreCase("delete")) {
				logger.debug("Debug:It is a deletion");
				userStoreService.removePrivileges(group);
			}
			response.setStatus(GuiConstants.SUCCESS);
		} catch (FrameworkException e) {
			logger.error("Error updating the group in the database" + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			response.setResponseString("");
		}
		return response;
	}

	// read group from user store service
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	public @ResponseBody
	Response updateGroup(@RequestBody String inputString) {
		Response response = new Response();

		JSONObject json = new JSONObject(inputString.trim());
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			logger.debug("Debug:Group Name cannot be empty");
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Group name cannot be empty");
			return response;
		}
		try {
			Group group = new Group(json.getString("name"));
			group.setDescription(json.getString("description"));
			// calling a private method to prepare privileges list
			List<Privilege> privilegeList = preparePrivileges(json);

			if (privilegeList.isEmpty()) {
				logger.debug("Debug:Atleast a Privilege to be defined to  this group");
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Atleast a Privilege to be defined to  this group");
				return response;
			}
			group.setPrivileges(privilegeList);
			userStoreService.updateGroup(group);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (FrameworkException | JSONException e) {
			logger.error("Error updating the group " + e);
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	private List<Privilege> preparePrivileges(JSONObject json)
			throws JSONException {

		List<Privilege> privilegeList = new ArrayList<Privilege>();
		if (json.getJSONArray("offers") != null) {
			JSONArray jsonArray = json.getJSONArray("offers");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("resources") != null) {
			JSONArray jsonArray = json.getJSONArray("resources");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("owner") != null) {
			JSONArray jsonArray = json.getJSONArray("owner");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}

		if (json.getJSONArray("rules") != null) {
			JSONArray jsonArray = json.getJSONArray("rules");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("resourceGroup") != null) {
			JSONArray jsonArray = json.getJSONArray("resourceGroup");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("offerGroup") != null) {
			JSONArray jsonArray = json.getJSONArray("offerGroup");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("users") != null) {

			JSONArray jsonArray = json.getJSONArray("users");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}

		if (json.getJSONArray("groups") != null) {
			JSONArray jsonArray = json.getJSONArray("groups");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		if (json.getJSONArray("notification") != null) {
			JSONArray jsonArray = json.getJSONArray("notification");
			for (int index = 0; index < jsonArray.length(); index++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(jsonArray.getString(
								index).toUpperCase()));
				privilegeList.add(privilege);
			}
		}
		return privilegeList;
	}

}
