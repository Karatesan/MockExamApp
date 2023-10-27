//package com.fdmgroup.MockExam.configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.fdmgroup.MockExam.config.ApplicationConfig;
//import com.fdmgroup.MockExam.model.User;
//import com.fdmgroup.MockExam.repositories.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@TestConfiguration
//@RequiredArgsConstructor
//public class TestConfig {
//	
//	private final UserRepository userRepository;
//	
//	@Autowired
//	UserDetailsService myUserDetailsService;
//	
//	 @Bean(name = "passwordEncoder()")
//	 public PasswordEncoder passwordEncoder() {
//	       return NoOpPasswordEncoder.getInstance()  ;
//	    }
//	 
//	
//		
//		@Bean(name = "authenticationProvider()")
//		public AuthenticationProvider authenticationProvider() {
//			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//			authProvider.setUserDetailsService(myUserDetailsService);
//			authProvider.setPasswordEncoder(passwordEncoder());
//			return authProvider;
//		}
//	 
//}
