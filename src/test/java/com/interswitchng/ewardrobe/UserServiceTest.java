package com.interswitchng.ewardrobe;

import com.interswitchng.ewardrobe.data.model.Gender;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.InvalidEmailException;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @Test
    public void testThatAUserCanSignUp() throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException {
        SignupDto request = new SignupDto();
        request.setFirstName("John");
        request.setLastName("John");
        request.setEmail("jes13@gmail.com");
        request.setGender("MALE");
        request.setPassword("password");
        request.setConfirmPassword("password");
        userService.signUp(request);

        Optional<User> savedUser = userRepo.findByEmail("jes1@gmail.com");

        // Add log statements to debug
        System.out.println("Saved User: " + savedUser.orElse(null));

        Assertions.assertEquals("FREE", savedUser.orElse(null).getPlan());
    }

}

