package com.interswitchng.ewardrobe.controller.user;


import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.ResetPasswordRequest;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.user.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) throws EWardRobeException, UserNotFoundException {
        return ResponseEntity.ok(authService.login(loginDto));
    }

}
