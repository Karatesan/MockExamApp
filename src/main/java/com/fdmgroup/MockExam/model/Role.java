package com.fdmgroup.MockExam.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fdmgroup.MockExam.model.Permission.ADMIN_READ;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_UPDATE;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_DELETE;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_CREATE;

/**
 * Represents user roles in the system.
 */
@RequiredArgsConstructor
public enum Role {
	TRAINEE(Collections.emptySet()),
	ADMIN(
	          Set.of(
	                  ADMIN_READ,
	                  ADMIN_UPDATE,
	                  ADMIN_DELETE,
	                  ADMIN_CREATE))
	          ;
	
	@Getter
	private final Set<Permission> permissions;
	
	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getPermissions()
	            .stream()
	            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
	            .collect(Collectors.toList());
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
	    return authorities;
	}
}

