package com.interswitchng.ewardrobe.controller.userChoice;

import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.UserChoice.UserChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userChoice")
public class UserChoiceController {

    @Autowired
    private UserChoice userChoice;
    @PostMapping("/saveUserChoice")
    public String saveUserChoice(
            @RequestParam("userId") String userId,
            @RequestParam("pictureUrl") String pictureUrl
    ) throws UserNotFoundException {

        return userChoice.saveUserChoice(userId,pictureUrl);
    }


}

