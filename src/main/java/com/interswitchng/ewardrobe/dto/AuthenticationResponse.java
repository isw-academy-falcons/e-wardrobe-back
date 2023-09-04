package com.interswitchng.ewardrobe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String userId;

    public static AuthenticationResponse of(String jwtToken, String userId){
        return new AuthenticationResponse(jwtToken,userId);
    }
}
