package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

@SpringJUnitConfig
@SpringBootTest
class test_PasswordResetTokenRepository {
	
    @MockBean
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String MOCK_TOKEN = "mockToken";
    private static final LocalDateTime MOCK_RESET_AT = LocalDateTime.now();
    
    private PasswordResetToken token;
    
    @BeforeEach
    void setUp() {
        token = new PasswordResetToken();
        token.setToken(MOCK_TOKEN);
    }

    @Test
    void test_findByToken() {
        when(passwordResetTokenRepository.findByToken(MOCK_TOKEN)).thenReturn(Optional.of(token));

        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByToken(MOCK_TOKEN);

        assertTrue(foundToken.isPresent());
        assertEquals(MOCK_TOKEN, foundToken.get().getToken());
    }

    @Test
    void test_updateResetAt() {
        when(passwordResetTokenRepository.updateResetAt(MOCK_TOKEN, MOCK_RESET_AT)).thenReturn(1);

        int result = passwordResetTokenRepository.updateResetAt(MOCK_TOKEN, MOCK_RESET_AT);

        assertEquals(1, result);
        verify(passwordResetTokenRepository, times(1)).updateResetAt(MOCK_TOKEN, MOCK_RESET_AT);
    }

}
