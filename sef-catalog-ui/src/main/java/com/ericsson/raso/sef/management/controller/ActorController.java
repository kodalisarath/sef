package com.ericsson.raso.sef.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Algorithm;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.PasswordCredential;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.auth.service.UserStoreService;
import com.ericsson.raso.sef.catalog.response.GuiConstants;
import com.ericsson.raso.sef.catalog.response.Response;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ActorController {
	private IUserStore userStoreService = null;

	public ActorController() {
		userStoreService = new UserStoreService();
	}

	// create actor in user store service
	@RequestMapping(value = "/createActor", method = RequestMethod.POST)
	public @ResponseBody
	Response createActor(@RequestBody String userActorStr) {

		Response response = new Response();

		JSONObject json = new JSONObject(userActorStr);

		if (json.isNull("name") || json.getString("name").equals("")) {
			response.setMessage("Actor Name can not be empty.");
			return response;
		}

		String name = json.getString("name");

		response.setStatus(GuiConstants.FAILURE);

		if (json.isNull("userName") || json.getString("userName").equals("")) {
			response.setMessage("UserName can not be empty.");
			return response;
		}

		Actor actor = new Actor(name);
		try {

			if (userStoreService.isActorExist(actor)) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Actor Already exist.");
				return response;
			}
		} catch (FrameworkException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			return response;
		}

		PasswordCredential credential = new PasswordCredential();

		if (json.getString("credentialType") != null
				&& json.getString("credentialType").equals("PASSWORD")) {

			if (json.isNull("password")
					|| json.getString("password").equals("")) {
				response.setMessage("Password can not be empty.");
				return response;
			}

			credential.setAlgorithm(Algorithm.DIGEST_MD5);

			try {
				credential.setPassword(json.getString("password"));
			} catch (FrameworkException e1) {
				e1.printStackTrace();
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e1.getMessage());
				return response;
			}
		}

		User usr = new User(json.getString("userName"), credential);

		JSONObject actorMeta = json.getJSONObject("actorMeta");

		if (actorMeta != null) {
			Map<String, Object> actorMap = new HashMap<String, Object>();
			for (Object key : actorMeta.keySet()) {
				actorMap.put(key.toString(), actorMeta.get(key.toString()));
			}
			actor.setMetas(actorMap);
		}

		JSONObject userMeta = json.getJSONObject("userMeta");
		if (userMeta != null) {
			ArrayList<Meta> userMetas = new ArrayList<Meta>();
			for (Object key : userMeta.keySet()) {
				Meta meta = new Meta();
				meta.setKey(key.toString());
				meta.setValue(userMeta.getString(key.toString()));
				userMetas.add(meta);
			}
			usr.setUserMeta(userMetas);
		}

		JSONArray privilegeArray = json.getJSONArray("privileges");
		JSONArray grpArray = json.getJSONArray("groups");
		if (grpArray.length() == 0 && privilegeArray.length() == 0) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Actor cannot be created without a group or a privilege assigned");
			return response;
		}

		if (privilegeArray != null) {
			for (int i = 0; i < privilegeArray.length(); i++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(privilegeArray
								.getString(i)));
				usr.addPrivilege(privilege);
			}
		}

		Map<String, User> identities = new HashMap<String, User>();

		identities.put(name, usr);

		actor.setIdentities(identities);

		Map<String, Group> memebers = new HashMap<String, Group>();

		for (int i = 0; i < grpArray.length(); i++) {
			Group grp = new Group(grpArray.get(i).toString());
			memebers.put(String.valueOf(i), grp);
		}

		actor.setMemberships(memebers);

		boolean isActorCreated = Boolean.FALSE;
		try {
			isActorCreated = userStoreService.createActor(actor);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Is Actor Created:" + isActorCreated);
		} catch (FrameworkException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		}
		return response;
	}

	// read actor from user store service
	@RequestMapping(value = "/readActor", method = RequestMethod.GET)
	public @ResponseBody
	Response readActor(ModelMap model,
			@RequestParam(value = "name", required = true) String actorName) {
		Response response = new Response();

		Actor actorResponse = null;
		try {
			actorResponse = userStoreService.readActor(actorName);
		} catch (FrameworkException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			return response;
		}

		if (actorResponse == null) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Actor not found");
			return response;
		}

		String jsonActorResponse = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonActorResponse = mapper.writeValueAsString(actorResponse);
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(jsonActorResponse);

		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}

		return response;
	}

	// update actor in user store service
	@RequestMapping(value = "/updateActor", method = RequestMethod.POST)
	public @ResponseBody
	Response updateActor(@RequestBody String userActorStr) {

		Response response = new Response();

		JSONObject json = new JSONObject(userActorStr);

		if (json.isNull("name") || json.getString("name").equals("")) {
			response.setMessage("Actor Name can not be empty.");
			return response;
		}

		String name = json.getString("name");

		response.setStatus(GuiConstants.FAILURE);

		if (json.isNull("userName") || json.getString("userName").equals("")) {
			response.setMessage("UserName can not be empty.");
			return response;
		}

		Actor actor = new Actor(name);
		PasswordCredential credential = new PasswordCredential();

		if (json.getString("credentialType") != null
				&& json.getString("credentialType").equals("PASSWORD")) {

			if (json.isNull("password")
					|| json.getString("password").equals("")) {
				response.setMessage("Password can not be empty.");
				return response;
			}

			credential.setAlgorithm(Algorithm.DIGEST_MD5);

			try {
				credential.setPassword(json.getString("password"));
			} catch (FrameworkException e1) {
				e1.printStackTrace();
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage(e1.getMessage());
				return response;
			}
		}

		User usr = new User(json.getString("userName"), credential);

		JSONObject actorMeta = json.getJSONObject("actorMeta");

		if (actorMeta != null) {
			Map<String, Object> actorMap = new HashMap<String, Object>();
			for (Object key : actorMeta.keySet()) {
				actorMap.put(key.toString(), actorMeta.get(key.toString()));
			}
			actor.setMetas(actorMap);
		}

		JSONObject userMeta = json.getJSONObject("userMeta");
		if (userMeta != null) {
			ArrayList<Meta> userMetas = new ArrayList<Meta>();
			for (Object key : userMeta.keySet()) {
				Meta meta = new Meta();
				meta.setKey(key.toString());
				meta.setValue(userMeta.getString(key.toString()));
				userMetas.add(meta);
			}
			usr.setUserMeta(userMetas);
		}

		JSONArray privilegeArray = json.getJSONArray("privileges");
		JSONArray grpArray = json.getJSONArray("groups");
		if (grpArray.length() == 0 && privilegeArray.length() == 0) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage("Actor cannot be created without a group or a privilege assigned");
			return response;
		}

		if (privilegeArray != null) {
			for (int i = 0; i < privilegeArray.length(); i++) {
				Privilege privilege = new Permission(
						AuthorizationPrinciple.valueOf(privilegeArray
								.getString(i)));
				usr.addPrivilege(privilege);
			}
		}

		Map<String, User> identities = new HashMap<String, User>();

		identities.put(name, usr);

		actor.setIdentities(identities);

		Map<String, Group> memebers = new HashMap<String, Group>();

		for (int i = 0; i < grpArray.length(); i++) {
			Group grp = new Group(grpArray.get(i).toString());
			memebers.put(String.valueOf(i), grp);
		}

		actor.setMemberships(memebers);

		boolean isActorCreated = Boolean.FALSE;
		try {
			isActorCreated = userStoreService.updateActor(actor);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Is Actor Created:" + isActorCreated);
		} catch (FrameworkException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
		}
		return response;
	}

	// call delete actor in user store service
	@RequestMapping(value = "/deleteActor", method = RequestMethod.GET)
	public @ResponseBody
	Response deleteActor(ModelMap model,
			@RequestParam(value = "name", required = true) String actorName) {
		Response response = new Response();
		boolean isDeletedActor = Boolean.FALSE;

		Actor actor = new Actor(actorName);

		try {

			if (!userStoreService.isActorExist(actor)) {
				response.setStatus(GuiConstants.FAILURE);
				response.setMessage("Actor does not exist.");
				return response;
			}
		} catch (FrameworkException e1) {
			e1.printStackTrace();
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e1.getMessage());
			return response;
		}

		try {
			isDeletedActor = userStoreService.deleteActor(actor);
			response.setStatus(GuiConstants.SUCCESS);
			response.setMessage("Is Actor Deleted:" + isDeletedActor);
		} catch (FrameworkException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/getAllActors", method = RequestMethod.GET)
	// headers="Accept=application/json")
	public @ResponseBody
	Response getAllActors(
			ModelMap model,
			@RequestParam(value = "searchQuery", required = false) String searchQuery,
			@RequestParam(value = "pageNumber", required = true) String pageNumber,
			@RequestParam(value = "pageSize", required = true) String pageSize) {

		Response response = new Response();

		List<Actor> actors = null;
		try {

			actors = userStoreService.readAllActors(searchQuery, pageNumber,
					pageSize);
		} catch (FrameworkException e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
			return response;
		}

		ObjectMapper mapper = new ObjectMapper();

		Map<String, List<Actor>> map = new HashMap<String, List<Actor>>();
		map.put("actors", actors);

		String val = "";

		try {
			val = mapper.writeValueAsString(map);
			response.setMessage("Read Group from user store service: ");
			response.setStatus(GuiConstants.SUCCESS);
			response.setResponseString(val);
		} catch (Exception e) {
			response.setStatus(GuiConstants.FAILURE);
			response.setMessage(e.getMessage());
		}

		return response;
	}
}
