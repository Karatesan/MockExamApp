package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

@SpringJUnitConfig
@SpringBootTest
class test_VerificationTokenRepository {
	
    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    private static final String MOCK_TOKEN = "mockToken";
    private static final Integer USER_ID = 1;

    private User user;
    private VerificationToken token;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        token = new VerificationToken();
        token.setId(1L);
        token.setToken(MOCK_TOKEN);
        token.setUser(user);
    }

    @Test
    void test_findByToken() {
        when(verificationTokenRepository.findByToken(MOCK_TOKEN)).thenReturn(Optional.of(token));

        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken(MOCK_TOKEN);

        assertTrue(foundToken.isPresent());
        assertEquals(MOCK_TOKEN, foundToken.get().getToken());
    }

    @Test
    void test_updateConfirmedAt() {
        LocalDateTime confirmedAt = LocalDateTime.now();
        when(verificationTokenRepository.updateConfirmedAt(MOCK_TOKEN, confirmedAt)).thenReturn(1);

        int result = verificationTokenRepository.updateConfirmedAt(MOCK_TOKEN, confirmedAt);

        assertEquals(1, result);
        verify(verificationTokenRepository, times(1)).updateConfirmedAt(MOCK_TOKEN, confirmedAt);
    }

    @Test
    void test_findByUser() {
        List<VerificationToken> tokens = new ArrayList<>();
        tokens.add(token);
        when(verificationTokenRepository.findByUser(user)).thenReturn(tokens);

        List<VerificationToken> foundTokens = verificationTokenRepository.findByUser(user);

        assertFalse(foundTokens.isEmpty());
        assertEquals(1, foundTokens.size());
        assertIterableEquals(tokens, foundTokens);
    }

}
