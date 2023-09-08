package com.interswitchng.ewardrobe;

import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.UserChoice.UserChoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class UserChoiceTest {
    @Autowired
    private UserChoice userChoice;

    @Test
    public void saveUserChoice() throws UserNotFoundException {
        String userId = "64fad60b7fc4923ffcf0a73e";
        String pictureUrl="https://unsplash.com.worldwide...";

        String savedUserChoice = userChoice.saveUserChoice(userId, pictureUrl);

        Assertions.assertEquals("Cloth has been saved successfully",savedUserChoice);

    }
}
