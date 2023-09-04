package com.interswitchng.ewardrobe.controller.user;


import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthenticationResponse login(LoginDto loginDto) throws EWardRobeException {
        return authService.login(loginDto);
    }
}
