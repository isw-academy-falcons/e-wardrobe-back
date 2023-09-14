//package com.interswitchng.ewardrobe;
//
//import com.interswitchng.ewardrobe.data.model.Gender;
//import com.interswitchng.ewardrobe.data.model.Plan;
//import com.interswitchng.ewardrobe.data.model.User;
//import com.interswitchng.ewardrobe.dto.SignupDto;
//import com.interswitchng.ewardrobe.exception.InvalidEmailException;
//import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
//import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
//import com.interswitchng.ewardrobe.repository.UserRepository;
//import com.interswitchng.ewardrobe.repository.VerificationTokenRepository;
//import com.interswitchng.ewardrobe.service.user.EmailService;
//import com.interswitchng.ewardrobe.service.user.UserService;
//import com.interswitchng.ewardrobe.service.user.VerificationTokenService;
//import jakarta.mail.MessagingException;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//
//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//    @MockBean
//    private UserRepository userRepo;
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//    @MockBean
//    private EmailService emailService;
//    @Autowired
//    private VerificationTokenService tokenService;
//    @MockBean
//    private VerificationTokenRepository tokenRepository;
//
//    @Test
//    public void testThatAUserCanSignUp() throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException, MessagingException {
//        SignupDto request = new SignupDto();
//        request.setFirstName("John");
//        request.setLastName("John");
//        request.setEmail("je2s14712@gmail.com");
//        request.setGender("MALE");
//        request.setPassword("password");
//        request.setConfirmPassword("password");
//        userService.signUp(request);
//
//        Optional<User> savedUser = userRepo.findByEmail("je2s14712@gmail.com");
//
//        // Add log statements to debug
//        System.out.println("Saved User: " + savedUser.orElse(null));
//
//        Assertions.assertEquals(Plan.FREE, savedUser.get().getPlan());
//    }
//    @AfterEach
//    void tearDown() throws InvalidEmailException {
//        userService.deleteUserByEmail("je2s14712@gmail.com");
//    }
//
//}
//
