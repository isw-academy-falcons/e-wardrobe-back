package com.interswitchng.ewardrobe.service.UserChoice;

import com.interswitchng.ewardrobe.exception.UserNotFoundException;

public interface UserChoice {
    public String saveUserChoice(String userId, String pictureUrl) throws UserNotFoundException;
}
