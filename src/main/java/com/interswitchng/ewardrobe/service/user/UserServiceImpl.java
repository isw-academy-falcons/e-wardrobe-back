package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.Gender;
import com.interswitchng.ewardrobe.data.model.Plan;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.SignupDto;
import com.interswitchng.ewardrobe.dto.SignupResponse;
import com.interswitchng.ewardrobe.exception.InvalidEmailException;
import com.interswitchng.ewardrobe.exception.PasswordMisMatchException;
import com.interswitchng.ewardrobe.exception.UserAlreadyExistException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponse signUp(SignupDto signupDto) throws UserAlreadyExistException, PasswordMisMatchException, InvalidEmailException {
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
                    newUser.setEmail(signupDto.getEmail());
                    newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                    newUser.setGender(Gender.valueOf(signupDto.getGender().toUpperCase()));
                    newUser.setPlan(Plan.FREE);
                    User saved = userRepository.save(newUser);
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
}
