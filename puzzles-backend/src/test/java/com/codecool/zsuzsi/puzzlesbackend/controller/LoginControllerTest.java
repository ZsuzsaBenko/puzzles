package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.InvalidLoginException;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {
    private static final String EMAIL = "email@email.hu";
    private static final String PASSWORD = "password";

    @MockBean
    private Authentication authentication;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private LoginController loginController;

    @Test
    public void testSuccessFulLogin() {
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD)))
                .thenReturn(authentication);
        String token = "token";
        when(jwtTokenServices.createToken(EMAIL, Collections.emptyList())).thenReturn(token);

        UserCredentials data = UserCredentials.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        ResponseEntity<Map<String, String>> result = loginController.login(data);

        assertNotNull(result.getBody());
        assertEquals("{email=email@email.hu, token=token}", result.getBody().toString());

        verify(authentication).getAuthorities();
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
        verify(jwtTokenServices).createToken(EMAIL, Collections.emptyList());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        when(authentication.getAuthorities()).thenThrow(BadCredentialsException.class);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD)))
                .thenReturn(authentication);

        UserCredentials data = UserCredentials.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        assertThrows(InvalidLoginException.class, () -> loginController.login(data));

        verify(authentication).getAuthorities();
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
    }

}
