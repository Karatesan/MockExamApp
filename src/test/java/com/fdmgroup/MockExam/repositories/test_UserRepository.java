package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fdmgroup.MockExam.model.User;

@SpringJUnitConfig
@SpringBootTest
class test_UserRepository {
	
	@MockBean
	private UserRepository userRepository;
	
    private static final String EXISTING_EMAIL = "abc@gmail.com";
    private static final String NON_EXISTING_EMAIL = "nonexistent@gmail.com";

    @BeforeEach
    void setUp() {
        User existingUser = new User();
        existingUser.setEmail(EXISTING_EMAIL);
        when(userRepository.findByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(NON_EXISTING_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.enableUser(EXISTING_EMAIL)).thenReturn(1);
        when(userRepository.enableUser(NON_EXISTING_EMAIL)).thenReturn(0);
    }

    @Test
    public void test_findByEmail_whenEmailIsFound() {
    	Optional<User> foundUser = userRepository.findByEmail(EXISTING_EMAIL);
        assertTrue(foundUser.isPresent());
        assertEquals(EXISTING_EMAIL, foundUser.get().getEmail());
    }
    
    @Test
    public void test_findByEmail_whenEmailNotFound() {
    	Optional<User> foundUser = userRepository.findByEmail(NON_EXISTING_EMAIL);
        assertTrue(foundUser.isEmpty());
    }
    
    @Test
    public void test_enableUser() {
        int result = userRepository.enableUser(EXISTING_EMAIL);
        assertEquals(1, result);
        verify(userRepository, times(1)).enableUser(EXISTING_EMAIL);
    }
    
    @Test
    public void test_enableUser_userNotEnabled() {
        int result = userRepository.enableUser(NON_EXISTING_EMAIL);
        assertEquals(0, result);
        verify(userRepository, times(1)).enableUser(NON_EXISTING_EMAIL);
    }

}
