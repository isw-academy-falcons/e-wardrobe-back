package com.interswitchng.ewardrobe.data.model;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    @Email
    private String email;
    private String password;
    private Gender gender;
    private Plan plan;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private List<Cloth> cloths = new ArrayList<>();
}
