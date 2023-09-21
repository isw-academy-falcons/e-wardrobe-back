package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.config.SecureUser;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.data.model.VerificationToken;
import com.interswitchng.ewardrobe.dto.AuthenticationResponse;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.repository.VerificationTokenRepository;
import com.interswitchng.ewardrobe.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerificationTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public AuthenticationResponse login(LoginDto loginDto) throws EWardRobeException, UserNotFoundException {
        Optional<User>savedUser = userRepository.findByEmail(loginDto.getEmail().toLowerCase());
        if (savedUser.isPresent()){
            if (savedUser.get().isActive()){
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getEmail().toLowerCase(), loginDto.getPassword())
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (BadCredentialsException e) {
                    throw new EWardRobeException(e.getLocalizedMessage());
                }
                User foundUser = userRepository.findByEmail(loginDto.getEmail().toLowerCase()).orElseThrow(() -> new EWardRobeException("user not found"));
                SecureUser user = new SecureUser(foundUser);
                String jwtToken = jwtService.generateToken(user);
                String fullName = foundUser.getFirstname() + " " + foundUser.getLastname();
                return AuthenticationResponse.of(jwtToken, user.getUserId(), fullName);
            }
            throw new EWardRobeException("Your account has not been verified !!!");
        }
        throw new UserNotFoundException("User not found!!!");
    }
}
