package com.interswitchng.ewardrobe.service.UserChoice;

import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.data.model.CollectionType;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.cloth.ClothService;
import com.interswitchng.ewardrobe.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserChoiceImpl implements UserChoice {

 @Autowired
 private UserService userService;

@Autowired
private ClothService clothService;

    public String saveUserChoice(String userId, String pictureUrl) throws UserNotFoundException {
        User foundUser = userService.findById(userId);

        Cloth clothByUserId = clothService.findClothByUserIdAndImageUrl(foundUser.getUserId(), pictureUrl);

        if (clothByUserId != null) {

            return "Cloth has been saved successfully";
        }


        Cloth newCloth = new Cloth();
        newCloth.setUserId(foundUser.getUserId());
        newCloth.setImageUrl(pictureUrl);
        newCloth.setCollectionType(CollectionType.UNSPLASH);
        clothService.saveCloth(newCloth);

        return "Cloth has been saved successfully";
    }

}
