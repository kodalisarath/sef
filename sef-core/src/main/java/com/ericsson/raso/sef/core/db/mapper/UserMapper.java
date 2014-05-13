package com.ericsson.raso.sef.core.db.mapper;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.User;

public interface UserMapper {

	public User getUserById(String userId);

	public List<User> getAllUsers();
	
	public void createUser(User user);
	
	public void createUserMetas(User user);
	
	public void addIdentity(User userIdentity) throws AuthAdminException;
	
	public void removeIdentity(User userIdentity) throws AuthAdminException;
 	
	public void insertUser(User user);
	
	public void updateUser(User user);

	public void deleteUser(Integer userId);

	public void createMapping(Map<String, User> identities);

}
