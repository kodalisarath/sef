package com.ericsson.raso.sef.auth;

import com.ericsson.raso.sef.core.FrameworkException;

public class UserStore {
	//TODO: implement the persistence with DB.



	public boolean createActor(Actor actor) throws FrameworkException {
		//TODO: implement the following logic when DB layer is ready...
		/*
		 * 1. Check if a user already exists... Remember, a subscriber is also an actor...
		 * 2. If the actor is subscriber, atleast one identity must be present to identify the subscriber entity. This implies that subscriber entity must exist before this operation.
		 * 3. If the actor is a robotic account, then check/ validate for relevant identity and credential
		 * 4. ensure to use SecureSerializationHelper to encrypt the Password before persisting in DB - irrespective of the type of Actor. 
		 */
		
		return false;
	}
	
	public boolean updateActor(Actor actor) throws FrameworkException {
		//TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Check if the user already exists... Take care that an actor can be missing though its subscriber exists... In such scenario, create he actor by internally calling createActor...
		 * 2. if the actor is type subscriber and the subscriber entity does not exist, then do not honor this request...
		 * 3. For other types of actor, do NOT honor the request if actor does not exist.
		 * 4. if the actor is a robotic account, then check/validate for relevant identiy and credential.
		 * 5. ensure to use SecureSerializationHelper to encrypt the Password before persisting in DB - irrespective of the type of Actor.
		 */
		
		return false;
	}
	
	public boolean deleteActor(Actor actor) throws FrameworkException {
		//TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Check if the user already exists... Take care that an actor can be missing though its subscriber exists... In such scenario, create he actor by internally calling createActor...
		 * 2. if the actor is type subscriber and the subscriber entity does not exist, then do not honor this request...
		 * 3. For other types of actor, do NOT honor the request if actor does not exist.
		 * 4. if the actor is a robotic account, then check/validate for relevant identiy and credential.
		 * 5. ensure to use SecureSerializationHelper to encrypt the Password before persisting in DB - irrespective of the type of Actor.
		 */
		
		return false;
	}
	
	public Actor readActor(String actorName) throws FrameworkException {
		//TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Query the DB Schema with the name of Actor and NOT the name of Identity/User.
		 * 2. Ensure all the Identities are packed in POJO returned.
		 * 3. Ensure all the Groups are packed in POJO returned.
		 * 4. Ensure all the Privileges are packed in the POJO... Think of how you will do this since the privileges master is in serialized form and if you store all privileges in DB, you need to validate using the master!!
		 */
		
		return null;
	}
	

}
