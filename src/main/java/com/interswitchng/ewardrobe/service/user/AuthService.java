package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.ResetPasswordRequest;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import jakarta.mail.MessagingException;

public interface AuthService {
    AuthenticationResponse login(LoginDto loginDto) throws EWardRobeException, UserNotFoundException;



}
