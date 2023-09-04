package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.Gender;
import com.interswitchng.ewardrobe.data.model.Plan;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.exception.InvalidEmailException;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException {
        if (isValidEmail(signupDto.getEmail().toLowerCase())){
            Optional<User> savedUser = userRepository.findByEmail(signupDto.getEmail().toLowerCase());
            if (savedUser.isPresent()){
                throw new UserAlreadyExistException("User with this email address already exist !!!");
            }else{
                if (signupDto.getPassword().equals(signupDto.getConfirmPassword())){
                    User newUser = new User();
                    newUser.setFirstname(signupDto.getFirstName());
                    newUser.setLastname(signupDto.getLastName());
                    newUser.setEmail(signupDto.getEmail().toLowerCase());
                    newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                    newUser.setDateCreated(LocalDateTime.now());
                    newUser.setGender(Gender.valueOf(signupDto.getGender().toUpperCase()));
                    newUser.setPlan(Plan.FREE);
                    userRepository.save(newUser);
                }
                else {
                    throw new PasswordMisMatchException("Password does not match");
                }
            }
        }else {
            throw new InvalidEmailException(signupDto.getEmail() +" is not a valid Email address");
        }
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

    private boolean isValidEmail(String email){
//        email must only contain letters, numbers, underscores, hyphens, and periods
//        email must contain an @ symbol
//        email must contain . after @ symbol
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }
}
