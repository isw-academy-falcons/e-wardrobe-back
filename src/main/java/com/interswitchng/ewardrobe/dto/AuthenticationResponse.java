package com.interswitchng.ewardrobe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String userId;
    private String fullName;
    private String plan;
    private String gender;
    private String email;

    public static AuthenticationResponse of(String jwtToken, String userId, String fullName, String plan, String gender, String email){
        return new AuthenticationResponse(jwtToken, userId, fullName, plan, gender, email);
    }
}
