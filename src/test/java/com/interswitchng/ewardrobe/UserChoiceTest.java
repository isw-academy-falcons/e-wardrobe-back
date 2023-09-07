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
        String userId = "{USER_ID}";
        String pictureUrl="https://unsplash...";

        String savedUserChoice = userChoice.saveUserChoice(userId, pictureUrl);

        Assertions.assertEquals("Cloth has been saved successfully",savedUserChoice);

    }
}
