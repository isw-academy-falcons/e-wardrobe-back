package com.interswitchng.ewardrobe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignupDto {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private String confirmPassword;
}
