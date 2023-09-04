package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.config.SecureUser;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse login(LoginDto loginDto) throws EWardRobeException {
        try{

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail().toLowerCase(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e){
            throw new EWardRobeException(e.getLocalizedMessage());
        }
        User foundUser = userRepository.findByEmail(loginDto.getEmail().toLowerCase()).orElseThrow(()-> new EWardRobeException("user not found"));
        SecureUser user = new SecureUser(foundUser);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.of(jwtToken, user.getUserId());

    }
}
