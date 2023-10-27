package com.fdmgroup.MockExam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fdmgroup.MockExam.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for application-related configurations.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
	
	private final UserRepository userRepository;
	
    /**
     * Defines a bean for UserDetailsService to retrieve user details from the repository.
     * @return UserDetailsService instance.
     */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
    /**
     * Defines a bean for AuthenticationProvider to authenticate users.
     * @return AuthenticationProvider instance.
     */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
    /**
     * Defines a bean for AuthenticationManager to manage authentication.
     * @param config AuthenticationConfiguration instance.
     * @return AuthenticationManager instance.
     * @throws Exception if an error occurs while configuring the AuthenticationManager.
     */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

    /**
     * Defines a bean for PasswordEncoder to encode passwords.
     * @return PasswordEncoder instance (BCryptPasswordEncoder).
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    /**
     * Global CORS (Cross-Origin Resource Sharing) configuration for the application.
     * @return WebMvcConfigurer instance.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allowed request headers
                        .allowCredentials(true);
            }
        };
    }
}
