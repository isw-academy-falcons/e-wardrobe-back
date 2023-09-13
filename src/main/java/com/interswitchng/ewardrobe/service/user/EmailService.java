package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendPasswordResetEmail(User user, String token) throws MessagingException;
    void sendRegistrationEmail(User user, String token) throws MessagingException;
}
