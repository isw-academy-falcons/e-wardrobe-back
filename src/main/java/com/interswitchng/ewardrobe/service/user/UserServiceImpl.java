package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.Gender;
import com.interswitchng.ewardrobe.data.model.Plan;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.data.model.VerificationToken;
import com.interswitchng.ewardrobe.dto.ResetPasswordRequest;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.dto.SignupResponse;
import com.interswitchng.ewardrobe.exception.*;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService tokenService;
    private final VerificationTokenRepository tokenRepository;


    @Override
    public SignupResponse signUp(SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException, MessagingException {
        if (!isValidEmail(signupDto.getEmail().toLowerCase()) && !isValidPassword(signupDto.getPassword())){
            throw new InvalidEmailException(signupDto.getEmail() +" is not a valid Email address");
        }
            Optional<User> savedUser = userRepository.findUserByEmailIgnoreCase(signupDto.getEmail());
            if (savedUser.isPresent()){
                throw new UserAlreadyExistException("User with this email address already exist !!!");
            }else{
                if (signupDto.getPassword().equals(signupDto.getConfirmPassword())){
                    User newUser = new User();
                    newUser.setFirstname(signupDto.getFirstName());
                    newUser.setLastname(signupDto.getLastName());
                    newUser.setEmail(signupDto.getEmail().toLowerCase());
                    newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                    newUser.setGender(Gender.valueOf(signupDto.getGender().toUpperCase()));
                    newUser.setPlan(Plan.FREE);
                    newUser.setActive(false);
                    User saved = userRepository.save(newUser);
                    VerificationToken token = tokenService.createEmailVerificationToken(newUser.getEmail().toLowerCase());
                    emailService.sendRegistrationEmail(newUser,token.getToken());
                    log.info(token.getToken());
                    return SignupResponse.builder()
                            .email(saved.getEmail())
                            .firstName(saved.getFirstname())
                            .lastName(saved.getLastname())
                            .message("User account created successfully. ")
                            .build();
                }
                else {
                    throw new PasswordMisMatchException("Password does not match");
                }
            }

    }

    @Override
    public String verifyUser(String token) throws EWardRobeException, UserNotFoundException {
        Optional<VerificationToken> savedToken = tokenRepository.findByToken(token);
        if (savedToken.isPresent()){
            VerificationToken tk = verifyToken(savedToken.get().getToken());
            if (tk.getToken().equals(savedToken.get().getToken())){
                Optional<User> savedUser = userRepository.findByEmail(savedToken.get().getEmail());
                if (savedUser.isPresent()){
                    savedToken.get().setTimeUsed(LocalDateTime.now());
                    savedUser.get().setActive(true);
                    tokenRepository.save(savedToken.get());
                    userRepository.save(savedUser.get());
                    return "Your account has been activated successfully";
                }
                throw new UserNotFoundException("Invalid User");
            }
            throw new EWardRobeException("Account verification failed!!!");
        }
        throw new EWardRobeException("Invalid Token");
    }

    @Override
    public String forgotPassword(String email) throws UserNotFoundException, MessagingException {
        Optional<User> user = userRepository.findByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        VerificationToken token = tokenService.createPasswordVerificationToken(email.toLowerCase());
        emailService.sendPasswordResetEmail(user.get(), token.getToken());

        return "Please check you mail for a reset-password link";
    }

    @Override
    public String resetPassword(String token, ResetPasswordRequest passwordRequest) throws EWardRobeException, UserNotFoundException {
        Optional<VerificationToken> savedToken = tokenRepository.findByToken(token);
        if (savedToken.isPresent()){
            VerificationToken tk = verifyToken(savedToken.get().getToken()) ;
            if (tk.getToken().equals(savedToken.get().getToken())){
                Optional<User> user = userRepository.findByEmail(savedToken.get().getEmail());
                if (user.isPresent()){
                    if (passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())){
                        user.get().setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
                        savedToken.get().setTimeUsed(LocalDateTime.now());
                        tokenRepository.save(savedToken.get());
                        userRepository.save(user.get());
                        return "You have successfully change you password";
                    }
                    throw new EWardRobeException("Password does not match !!!");
                }
                throw new UserNotFoundException("User does not exist !!!");
            }
            throw new EWardRobeException("Invalid Token");
        }
        throw new UserNotFoundException("Invalid User");
    }

    @Override
    public User loadUser(String email) throws InvalidEmailException {
        return userRepository.findUserByEmailIgnoreCase(email).orElseThrow(()-> new InvalidEmailException("Bad Credentials"));
    }

    @Override
    public void deleteUserByEmail(String mail) throws InvalidEmailException {
        User user_found = userRepository.findByEmail(mail).orElseThrow(() -> new InvalidEmailException("not found"));
        userRepository.delete(user_found);
    }

    @Override
    public User findById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("User not found"));
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
      return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User cannot be found"));
    }

    private boolean isValidEmail(String email){
//        email must only contain letters, numbers, underscores, hyphens, and periods
//        email must contain an @ symbol
//        email must contain . after @ symbol
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }
    private boolean isValidPassword(String password){
//        password must have at least 8 characters, 1 upper case, 1 number, 1 special character
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,}$\n");
    }
    private VerificationToken verifyToken(String token) throws EWardRobeException {

        Optional<VerificationToken> savedToken = tokenRepository.findByToken(token);
        if (savedToken.isPresent()) {
            if (savedToken.get().getTimeUsed() != null) {
                throw new EWardRobeException("This token has been used !!!");
            }
            if (savedToken.get().getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new EWardRobeException("This token has expired !!!");
            }
            return savedToken.get();
        }
        throw new EWardRobeException("Invalid Token");
    }
}
