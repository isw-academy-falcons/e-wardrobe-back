package com.interswitchng.ewardrobe.controller.user;

import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.InvalidEmailException;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
import com.interswitchng.ewardrobe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity <?> signUp(@RequestBody SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException {
        userService.signUp(signupDto);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }
}
