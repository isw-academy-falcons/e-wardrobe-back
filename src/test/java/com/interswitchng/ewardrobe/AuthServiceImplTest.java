package com.interswitchng.ewardrobe;


import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.security.JwtService;
import com.interswitchng.ewardrobe.service.user.AuthService;
import com.interswitchng.ewardrobe.service.user.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthServiceImpl.class})
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
public class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    LoginDto loginRequest = LoginDto.builder()
            .email("testingEmail")
            .password("Password123@")
            .build();


    @Test
    void testThatAuthenticationReturnsUserAndToken() throws EWardRobeException {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User("14")));
        when(jwtService.generateToken(any())).thenReturn("ABC123");

        AuthenticationResponse authenticationResponse = authService.login(loginRequest);

        assertEquals("ABC123", authenticationResponse.getAccessToken());
        assertEquals(authenticationResponse.getUserId(), "14");

        verify(userRepository).findByEmail(any());
        verify(jwtService).generateToken(any());
        verify(authenticationManager).authenticate(any());
    }


}
