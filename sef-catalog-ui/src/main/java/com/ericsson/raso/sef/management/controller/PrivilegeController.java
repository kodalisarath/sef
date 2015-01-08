package com.ericsson.raso.sef.management.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.core.FrameworkException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PrivilegeController {
	private IPrivilegeManager privilegeManager = null;

	public PrivilegeController() {
		privilegeManager = new PrivilegeManager();
	}

	// create Privilege
	@RequestMapping(value = "/createPrivilege", method = RequestMethod.GET)
	public @ResponseBody
	Response createPrivilege(
			@RequestParam(value = "privilegeName", required = true) String privilegeName)
			throws FrameworkException {
		Response response = new Response();
		boolean isPrivilegeCreated = Boolean.FALSE;
		Privilege privilege = new Permission(
				AuthorizationPrinciple.valueOf(privilegeName));
		try {
			isPrivilegeCreated = privilegeManager.createPermission(privilege);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Is Group Created:" + isPrivilegeCreated);
		} catch (AuthAdminException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// delete a privilege
	@RequestMapping(value = "/deletePrivilege", method = RequestMethod.GET)
	public @ResponseBody
	Response deletePrivilege(
			@RequestParam(value = "privilegeName", required = true) String privilegeName) {
		Response response = new Response();
		if (privilegeName == null || privilegeName.isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Privilege Name Cannot be Empty");
			return response;
		}
		Privilege privilege;
		try {
			privilege = privilegeManager.readPermission(privilegeName);
			privilegeManager.deletePermission(privilege);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Privilige Deleted Successfully");
		} catch (AuthAdminException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

	// read a privilege
	@RequestMapping(value = "/readPrivilege", method = RequestMethod.GET)
	public @ResponseBody
	Response readPrivilege(
			@RequestParam(value = "privilegeName", required = true) String privilegeName) {
		Response response = new Response();
		if (privilegeName == null || privilegeName.isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Privilege Name Cannot be Empty");
			return response;
		}
		Privilege privilege;
		try {
			privilege = privilegeManager.readPermission(privilegeName);
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(privilege);
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(result);
		} catch (AuthAdminException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	// read a privilege
	@RequestMapping(value = "/updatePrivilege", method = RequestMethod.POST)
	public @ResponseBody
	Response updatePrivilege(@RequestBody String inputString) {
		Response response = new Response();
		JSONObject json = new JSONObject(inputString.trim());
		if (json.getString("name") == null || json.getString("name").isEmpty()) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Privilege Name Cannot be Empty");
			return response;
		}
		try {
			Permission permission = new Permission(
					AuthorizationPrinciple.valueOf(json.getString("name")));
			JSONArray jsonArray = json.getJSONArray("impliedPrivileges");
			for (int index = 0; index < jsonArray.length(); index++) {
				permission.addImplied(new Permission(AuthorizationPrinciple
						.valueOf(jsonArray.getString(index))));
			}
			privilegeManager.updatePermission(permission);
			response.setStatus(GuiConstants.SUCCESS);
		} catch (AuthAdminException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

	// read all privileges
	@RequestMapping(value = "/readallPrivileges", method = RequestMethod.GET)
	public @ResponseBody
	Response readAllPrivileges() {
		Response response = new Response();
		Map<AuthorizationPrinciple, Privilege> privilegesMap = null;
		try {
			privilegesMap = privilegeManager.getGuiPrivileges();
			Map<String, Map<AuthorizationPrinciple, Privilege>> map = new HashMap<String, Map<AuthorizationPrinciple, Privilege>>();
			map.put("privileges", privilegesMap);
			ObjectMapper mapper = new ObjectMapper();
			String val = mapper.writeValueAsString(map);
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(val);
		} catch (AuthAdminException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

}