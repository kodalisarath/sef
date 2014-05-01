package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.raso.sef.core.db.model.Permission;
import com.ericsson.raso.sef.core.db.model.Role;
import com.ericsson.raso.sef.core.db.model.RoleAssignDto;
import com.ericsson.raso.sef.core.db.model.RolePermissionDto;
import com.ericsson.raso.sef.core.db.model.RolePermissionMappingDto;
import com.ericsson.raso.sef.core.db.model.RoleProviderDto;

public interface RoleManagerMapper {
	
	
	int validateProvider(@Param("provider_id") String provider_id,@Param("provider_pwd")  String provider_pwd);
	
	RolePermissionMappingDto getPermissionsbyRoleId(String roleName);
	
	RoleProviderDto getAllProviderRoles(String providerId);
	
	String getPermissionId(String permission_name);
	
	List<RolePermissionDto> getAllRoles_Permissions();
	
	RoleAssignDto getAssignRoles(String providerId);
	
	void insertRoleValue(Role role);
	
	void createRoleIdMapping(@Param("provider_id") String provider_id,@Param("roleName") String roleName);
	
	void createRolePermissionMapping(@Param("roleName") String roleName,@Param("permission_id") String permission_id);
	
	void insertPermissionValue(Permission permission);
	
	Collection<Role> getAllRoleTypes();
	
	Collection<Permission> getAllPermissions();
	
	void removeRole(String roleName);
	
	void updateRole(Role role);
	
	void updatePermission(Permission permission);
	
	void removePermission(String permissionId);
	
	void updateAssignRoles(RoleAssignDto assignDto);
	
	void removeAssignRoles(String roleName);
	
	void removeRolePermissionMappings(String roleName);

	Collection<String> getUserPermission(String providerId);
}
