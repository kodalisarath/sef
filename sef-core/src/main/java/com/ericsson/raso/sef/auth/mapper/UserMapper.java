package com.ericsson.raso.sef.auth.mapper;

import java.util.List;

import com.ericsson.raso.sef.auth.User;

public interface UserMapper {

	public User getUserById(Integer userId);

	public List<User> getAllUsers();

	public void insertUser(User user);
	
	public void updateUser(User user);

	public void deleteUser(Integer userId);

}
