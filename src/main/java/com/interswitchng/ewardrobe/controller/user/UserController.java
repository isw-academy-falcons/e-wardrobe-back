package com.interswitchng.ewardrobe.controller.user;

import com.interswitchng.ewardrobe.dto.ResetPasswordRequest;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.dto.SignupResponse;
import com.interswitchng.ewardrobe.exception.*;
import com.interswitchng.ewardrobe.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity <SignupResponse> signUp(@RequestBody @Valid SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException, EWardRobeException {
        try {
            SignupResponse response = userService.signUp(signupDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (RuntimeException ex){
            throw new EWardRobeException(ex.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/verify-user/{token}")
    public ResponseEntity<?>verifyAccount(@PathVariable("token") @Valid String token) throws EWardRobeException, UserNotFoundException {
        userService.verifyUser(token);
        return ResponseEntity.ok("Your account has been successfully verified");
    }
    @PatchMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") @Valid String token, @RequestBody @Valid ResetPasswordRequest passwordRequest) throws UserNotFoundException, EWardRobeException {
        userService.resetPassword(token,passwordRequest);
        return ResponseEntity.ok("You have successfully changed you password");
    }
    @PostMapping("/forget-password/{email}")
    public ResponseEntity<?>forgetPassword(@PathVariable("email") @Valid String email) throws UserNotFoundException, MessagingException {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Please check you mail for a reset password link");
    }



}
