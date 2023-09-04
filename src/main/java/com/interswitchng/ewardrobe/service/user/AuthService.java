package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;

public interface AuthService {
    AuthenticationResponse login(LoginDto loginDto) throws EWardRobeException;

}
