package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.InvalidEmailException;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;

public interface UserService {
    void signUp(SignupDto signupDto) throws PasswordMisMatchException, UserAlreadyExistException, InvalidEmailException;
    User loadUser(String email) throws InvalidEmailException;

    void deleteUserByEmail(String mail) throws InvalidEmailException;
}
