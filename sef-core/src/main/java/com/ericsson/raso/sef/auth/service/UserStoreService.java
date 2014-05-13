package com.ericsson.raso.sef.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.db.mapper.ActorMapper;
import com.ericsson.raso.sef.core.db.mapper.GroupMapper;
import com.ericsson.raso.sef.core.db.mapper.UserMapper;

@Service
public class UserStoreService implements IUserStore {
	// TODO: implement the persistence with DB.

	private SqlSession sqlSession;

	private ActorMapper actorMapper;
	private GroupMapper groupMapper;
	private UserMapper userMapper;

	public void setGroupMapper(GroupMapper groupMapper) {
		this.groupMapper = groupMapper;
	}

	public void setActorMapper(ActorMapper actorMapper) {
		this.actorMapper = actorMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.raso.sef.auth.IUserStore#createActor(com.ericsson.raso.sef
	 * .auth.Actor)
	 */

	@Override
	@Transactional
	public boolean createActor(Actor actor) throws FrameworkException {
		// TODO: implement the following logic when DB layer is ready...
		/*
		 * 1. Check if a user already exists... Remember, a subscriber is also
		 * an actor... 2. If the actor is subscriber, atleast one identity must
		 * be present to identify the subscriber entity. This implies that
		 * subscriber entity must exist before this operation. 3. If the actor
		 * is a robotic account, then check/ validate for relevant identity and
		 * credential 4. ensure to use SecureSerializationHelper to encrypt the
		 * Password before persisting in DB - irrespective of the type of Actor.
		 */

		// Validations
		// ===========
		// 1. Actor must have one identity (user)
		// 2. Minimum one privilege must exists for a user or group

		// Actions
		// =======
		// 1. Create an actor - insert (ActorMapper.createActor)
		// 2. If actor already exists, update actor details - Update actor meta
		// if any (ActorMapper.createMetas) else
		// 3. If actor POJO contains group mapping, create the Group map -
		// insert on Group map(ActorMapper.createMapping)
		// 4. Create the user - insert (UserMapper.createUser)
		// 5. Create user-actor mapping - insert (ActorMapper.createMapping) -
		// 6. create user meta - insert (UserMapper.createUserMetas)

		actorMapper = sqlSession.getMapper(ActorMapper.class);
		userMapper = sqlSession.getMapper(UserMapper.class);

		if ((actor.getIdentities() != null)
				&& (!actor.getIdentities().isEmpty())) {

			try {
				actorMapper.createActor(actor);

			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("Actor [" + actor.getName()
						+ "] already exists..!! " + e);
			}

			if ((actor.getMetas() != null)) {
				try {
					actorMapper.updateMetas(actor);
				} catch (PersistenceException e) {
					e.printStackTrace();
					throw new FrameworkException("PersistenceException: " + e);
				}
			}

			if ((actor.getMemberships() != null)) {
				try {
					actorMapper.createMapping(actor);

					Map<String, User> userMap = new HashMap<String, User>();

					userMap.put(actor.getName(), actor.getIdentities()
							.get(actor.fetchIdentity(actor.getName())));

					userMapper.createMapping(userMap);
					userMapper.createUserMetas(userMap.get(actor));
				} catch (PersistenceException e) {
					e.printStackTrace();
					throw new FrameworkException("PersistenceException: " + e);
				}
			}

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.raso.sef.auth.IUserStore#updateActor(com.ericsson.raso.sef
	 * .auth.Actor)
	 */
	@Override
	public boolean updateActor(Actor actor) throws FrameworkException {
		// TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Check if the user already exists... Take care that an actor can be
		 * missing though its subscriber exists... In such scenario, create he
		 * actor by internally calling createActor... 2. if the actor is type
		 * subscriber and the subscriber entity does not exist, then do not
		 * honor this request... 3. For other types of actor, do NOT honor the
		 * request if actor does not exist. 4. if the actor is a robotic
		 * account, then check/validate for relevant identity and credential. 5.
		 * ensure to use SecureSerializationHelper to encrypt the Password
		 * before persisting in DB - irrespective of the type of Actor.
		 */

		if ((actor.getIdentities() != null)
				&& (!actor.getIdentities().isEmpty())) {

			if ((actor.getMetas() != null)) {
				try {
					actorMapper.updateMetas(actor);
				} catch (PersistenceException e) {
					e.printStackTrace();
					throw new FrameworkException("PersistenceException: " + e);
				}
			}

			if ((actor.getMemberships() != null)) {
				try {
					actorMapper.createMapping(actor);

					Map<String, User> userMap = new HashMap<String, User>();

					userMap.put(actor.getName(), actor.getIdentities()
							.get(actor.fetchIdentity(actor.getName())));

					userMapper.createMapping(userMap);
					userMapper.createUserMetas(userMap.get(actor));
				} catch (PersistenceException e) {
					e.printStackTrace();
					throw new FrameworkException("PersistenceException: " + e);
				}
			}

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.raso.sef.auth.IUserStore#deleteActor(com.ericsson.raso.sef
	 * .auth.Actor)
	 */
	@Override
	public boolean deleteActor(Actor actor) throws FrameworkException {
		// TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Check if the user already exists... Take care that an actor can be
		 * missing though its subscriber exists... In such scenario, create he
		 * actor by internally calling createActor... 2. if the actor is type
		 * subscriber and the subscriber entity does not exist, then do not
		 * honor this request... 3. For other types of actor, do NOT honor the
		 * request if actor does not exist. 4. if the actor is a robotic
		 * account, then check/validate for relevant identiy and credential. 5.
		 * ensure to use SecureSerializationHelper to encrypt the Password
		 * before persisting in DB - irrespective of the type of Actor.
		 */

		if ((actor.getIdentities() != null)
				&& (!actor.getIdentities().isEmpty())) {

			if ((actor.getMetas() != null)
					&& (actor.getMemberships() != null)) {

				try {
					actor.removeMembership(actor.getMemberships().get(actor));
					actorMapper.deleteActor(actor);
				} catch (PersistenceException e) {
					e.printStackTrace();
					throw new FrameworkException("PersistenceException: " + e);
				}
			}

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.raso.sef.auth.IUserStore#readActor(java.lang.String)
	 */
	@Override
	public Actor readActor(String actorName) throws FrameworkException {
		// TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Query the DB Schema with the name of Actor and NOT the name of
		 * Identity/User. 2. Ensure all the Identities are packed in POJO
		 * returned. 3. Ensure all the Groups are packed in POJO returned. 4.
		 * Ensure all the Privileges are packed in the POJO... Think of how you
		 * will do this since the privileges master is in serialized form and if
		 * you store all privileges in DB, you need to validate using the
		 * master!!
		 */

		Actor actor = null;
		if (actorName != null) {

			try {
				actor = actorMapper.readActor(actorName);
			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("PersistenceException: " + e);
			}

		}
		return actor;
	}

	@Override
	public String fetchReferenceMeta(String actorName, String metaName)
			throws FrameworkException {
		// TODO: implement the following logic when DB layer is ready....
		/*
		 * 1. Query the DB Schema with the name of Actor and NOT the name of
		 * Identity/User. 2. Ensure all the Identities are packed in POJO
		 * returned. 3. Ensure all the Groups are packed in POJO returned. 4.
		 * Ensure all the Privileges are packed in the POJO... Think of how you
		 * will do this since the privileges master is in serialized form and if
		 * you store all privileges in DB, you need to validate using the
		 * master!!
		 */

		String actorReferenceMeta = null;
		if ((actorName != null) && (metaName != null)) {

			try {
				actorReferenceMeta = actorMapper.fetchReferenceMeta(actorName,
						metaName);
			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("PersistenceException: " + e);
			}

		}

		return actorReferenceMeta;
	}

	@Override
	public boolean createGrop(Group group) throws FrameworkException {
		// TODO Auto-generated method stub

		groupMapper = sqlSession.getMapper(GroupMapper.class);

		if ((group.getPrivileges() != null) && (!group.getName().isEmpty())) {

			try {
				groupMapper.insertGroup(group);

			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("Group [" + group.getName()
						+ "] already exists..!! " + e);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean updateGroup(Group group) throws FrameworkException {
		// TODO Auto-generated method stub

		groupMapper = sqlSession.getMapper(GroupMapper.class);

		if ((group.getPrivileges() != null) && (!group.getName().isEmpty())) {

			try {
				groupMapper.updateGroup(group);

			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("PersistenceException: " + e);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean deleteGroup(Group group) throws FrameworkException {
		// TODO Auto-generated method stub

		groupMapper = sqlSession.getMapper(GroupMapper.class);

		if ((group.getPrivileges() != null) && (!group.getName().isEmpty())) {

			try {
				groupMapper.deleteGroup(group);

			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("PersistenceException: " + e);
			}

			return true;
		}

		return false;
	}

	@Override
	public String readGroup(String groupName) throws FrameworkException {
		// TODO Auto-generated method stub

		groupMapper = sqlSession.getMapper(GroupMapper.class);
		String description = null;
		if (groupName != null){

			try {
				groupMapper.readGroup(groupName);

			} catch (PersistenceException e) {
				e.printStackTrace();
				throw new FrameworkException("PersistenceException: " + e);
			}

			return description;
		}

		return null;
	}

}
