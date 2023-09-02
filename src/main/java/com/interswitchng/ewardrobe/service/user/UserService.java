package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;

public interface UserService {
    void signUp(SignupDto signupDto) throws PasswordMisMatchException, UserAlreadyExistException;
    void login(LoginDto loginDto);
}
