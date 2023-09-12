package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.ResetPasswordRequest;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.dto.SignupResponse;
import com.interswitchng.ewardrobe.exception.*;
import jakarta.mail.MessagingException;

public interface UserService {
    SignupResponse signUp(SignupDto signupDto) throws PasswordMisMatchException, UserAlreadyExistException, InvalidEmailException, MessagingException;
    String verifyUser(String token) throws EWardRobeException, UserNotFoundException;
    String forgotPassword(String email) throws UserNotFoundException, MessagingException;

    String resetPassword(String token, ResetPasswordRequest passwordRequest) throws EWardRobeException, UserNotFoundException;

    User loadUser(String email) throws InvalidEmailException;

    void deleteUserByEmail(String mail) throws InvalidEmailException;
}
