package com.interswitchng.ewardrobe.service.user;

import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.SendEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{

    @Value("${ewardrobe.app.FRONTEND_HOST}")
    private String frontEndHost;

    @Value("${ewardrobe.app.VERIFY_USER}")
    private String uri;

    @Value("${ewardrobe.app.RESET_PASSWORD}")
    private String resetPassword;

    @Value("${spring.mail.username}")
    private String emailEndPoint;

    private final JavaMailSender javaMailSender;
    @Override
    @Async
    public void sendPasswordResetEmail(User user, String token) throws MessagingException {
        sendEmail(SendEmailRequest.builder()
                .message(String.format("""
                        Dear %s,
                        
                        There was a request to change your password!
                        If you did not make this request, please ignore this email.
                        Otherwise, please click this link to change your password
                        """ + frontEndHost + resetPassword  + token, user.getFirstname().toUpperCase()))
                .subject("PASSWORD RESET EMAIL - SKYFITZ")
                .emailAddress(user.getEmail()).build());

    }

    private void sendEmail(SendEmailRequest request) throws MessagingException {
        MimeMessage res = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(res, true);

        mimeMessageHelper.setFrom(request.getFrom());
        mimeMessageHelper.setSubject(request.getSubject());
        mimeMessageHelper.setText(request.getMessage());
        mimeMessageHelper.setTo(request.getEmailAddress());

        javaMailSender.send(res);
    }

    @Override
    @Async
    public void sendRegistrationEmail(User user, String token) throws MessagingException {
        String link = frontEndHost + "/verify?token=" + token;
        sendEmail(SendEmailRequest.builder()
                .subject("Welcome to SkyFitz - Your Ultimate eWardrobe Experience!")
                .emailAddress(user.getEmail())
                .message(String.format("""
                    
                    Dear %s,
                    
                    Welcome to SkyFitzz, your one-stop destination for thr perfect wardrobe combination!
                    
                    We're delighted to have you join our stylish community. At SkyFitzz, we're all about making fashion 
                    fun and effortless.
                    Whether you're a trendsetter or just looking for that ideal outfit, we've got you covered.
                    
                    Start curating your dream eWardrobe today by activating and logging into your account.
                    Click %s to activate your account.
                    
                    If you ever need any assistance or have any questions about our features, our dedicated support team is here to assist you.
                    
                    
                    Fashionably yours,
                    
                    The SkyFitzz Team
                    [Website Url]
                    skyfitzz11@gmail.com
                    """, user.getFirstname().toUpperCase(),link))
                .build());
    }

}
