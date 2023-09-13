package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createEmailVerificationToken(String email);
    VerificationToken createPasswordVerificationToken(String email);
//    VerificationToken findTokenByToken(String token);
}
