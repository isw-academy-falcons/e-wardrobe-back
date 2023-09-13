package com.interswitchng.ewardrobe.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class VerificationToken {
    @Id
    private String tokenId;
    private String email;
    private String token;
    private LocalDateTime timeCreated;
    private LocalDateTime timeUsed;
    private LocalDateTime expirationDate;

}
