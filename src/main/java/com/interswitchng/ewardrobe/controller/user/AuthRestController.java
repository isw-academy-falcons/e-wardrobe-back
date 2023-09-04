package com.interswitchng.ewardrobe.controller.user;


import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthController authController;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws EWardRobeException {
        return ResponseEntity.ok(authController.login(loginDto));
    }
}
