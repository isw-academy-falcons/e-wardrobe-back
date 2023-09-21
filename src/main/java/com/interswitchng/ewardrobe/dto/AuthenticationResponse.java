package com.interswitchng.ewardrobe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String userId;
    private String fullName;

    public static AuthenticationResponse of(String jwtToken, String userId, String fullName){
        return new AuthenticationResponse(jwtToken, userId, fullName);
    }
}
