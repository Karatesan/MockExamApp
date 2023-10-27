package com.fdmgroup.MockExam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static com.fdmgroup.MockExam.model.Role.ADMIN;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_READ;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_UPDATE;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_DELETE;
import static com.fdmgroup.MockExam.model.Permission.ADMIN_CREATE;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for security-related configurations.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;
	
    /**
     * Bean definition for MvcRequestMatcher.Builder.
     * @param introspector HandlerMappingIntrospector instance.
     * @return MvcRequestMatcher.Builder instance.
     */
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
	
    /**
     * Defines the security filter chain configuration for the application.
     * @param http The HttpSecurity configuration.
     * @param mvc MvcRequestMatcher.Builder instance.
     * @return SecurityFilterChain instance.
     * @throws Exception if an error occurs while configuring security.
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
		http
			.csrf()
			.disable()
			.authorizeHttpRequests()
			//Requests that can be accessed without authentication
			.requestMatchers(mvc.pattern("/api/authentication/**"))
			.permitAll()
			.requestMatchers(mvc.pattern("/api/admin/**")).hasAnyRole(ADMIN.name())
	        .requestMatchers(mvc.pattern(GET, "/api/admin/**")).hasAnyAuthority(ADMIN_READ.name())
	        .requestMatchers(mvc.pattern(POST, "/api/admin/**")).hasAnyAuthority(ADMIN_CREATE.name())
	        .requestMatchers(mvc.pattern(PUT, "/api/admin/**")).hasAnyAuthority(ADMIN_UPDATE.name())
	        .requestMatchers(mvc.pattern(DELETE, "/api/admin/**")).hasAnyAuthority(ADMIN_DELETE.name())
			.anyRequest()
				.authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
		
		
	}

}
