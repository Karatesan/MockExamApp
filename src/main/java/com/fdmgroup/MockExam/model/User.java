package com.fdmgroup.MockExam.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a User entity in the database and implements UserDetails for
 * Spring Security integration.
 */
@Entity
@Data
@Builder
@Table(name = "EXAM_USER")
@AllArgsConstructor
@Validated
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usergen")
	@SequenceGenerator(name = "usergen", sequenceName = "user_id_seq", allocationSize = 1)
	private Integer id;
	@Column(unique = true, nullable = false)
	@Email(message = "Not a valid email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotBlank(message = "Email field cannot be left blank")
	private String email;
	@NotBlank(message = "First name field cannot be left blank!")
	@Size(max = 50, message = "First names cannot be longer than 50 characters!")
	private String firstName;
	@NotBlank(message = "Last name field cannot be left blank!")
	@Size(max = 100, message = "Last names cannot be longer than 100 characters!")
	private String lastName;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	private Boolean verified;

	@JdbcTypeCode(SqlTypes.JSON)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_IMAGE")
	private UserImage userImage;
	@Builder.Default
	private Boolean locked = false;
	@ManyToMany
	private List<Tag> tags;

	/**
	 * Constructs a User object with the specified email, first name, last name,
	 * password and role.
	 * 
	 * @param email     The email address of the User.
	 * @param firstName The first name of the User.
	 * @param lastName  The last name of the User.
	 * @param password  The password of the User.
	 * @param role      The role of the User.
	 */
	public User(String email, String firstName, String lastName, String password, Role role) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		tags=new ArrayList<>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return verified;
	}

	/**
	 * Constructs a User object with the specified email, first name, and last name.
	 * 
	 * @param email     The email address of the User.
	 * @param firstName The first name of the User.
	 * @param lastName  The last name of the User.
	 */
	public User(String email, String firstName, String lastName) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		tags=new ArrayList<>();
	}

	public User(Integer id,
			@Email(message = "Not a valid email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") @NotBlank(message = "Email field cannot be left blank") String email,
			@NotBlank(message = "First name field cannot be left blank!") @Size(max = 50, message = "First names cannot be longer than 50 characters!") String firstName,
			@NotBlank(message = "Last name field cannot be left blank!") @Size(max = 100, message = "Last names cannot be longer than 100 characters!") String lastName,
			String password, Role role, Boolean verified) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		this.verified = verified;
		tags=new ArrayList<>();
	}

	public List<Tag> addToTags(Tag tag) {
		tags.add(tag);
		return tags;
	}

	public User() {
		super();
		tags=new ArrayList<>();
	}

}
