package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.Gender;
import com.interswitchng.ewardrobe.data.model.Plan;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.LoginDto;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public void signUp(SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException {
            Optional<User> savedUser = userRepository.findByEmail(signupDto.getEmail().toLowerCase());
            if (savedUser.isPresent()){
                throw new UserAlreadyExistException("User with this email address already exist !!!");
            }else{
//        encode the passwords

                if (signupDto.getPassword().equals(signupDto.getConfirmPassword())){
                    User newUser = new User();
                    newUser.setFirstname(signupDto.getFirstName());
                    newUser.setLastname(signupDto.getLastName());
                    newUser.setEmail(signupDto.getEmail().toLowerCase());
//        encode the password
                    newUser.setPassword(signupDto.getPassword());
                    newUser.setDateCreated(LocalDateTime.now());
                    newUser.setGender(Gender.valueOf(signupDto.getGender().toUpperCase()));
                    newUser.setPlan(Plan.FREE);
                    userRepository.save(newUser);
                }
                else {
                    throw new PasswordMisMatchException("Password does not match");
                }
            }
    }

    @Override
    public void login(LoginDto loginDto) {

    }
}
