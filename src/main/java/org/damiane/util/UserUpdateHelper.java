package org.damiane.util;

import org.damiane.dto.TrainerUpdateDTO;
import org.damiane.entity.User;
import org.damiane.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateHelper {

    @Autowired
    private UserService userService;

    public User updateUser(User user, TrainerUpdateDTO trainerUpdateDTO) {
        user.setFirstName(trainerUpdateDTO.getFirstName());
        user.setLastName(trainerUpdateDTO.getLastName());
        user.setActive(trainerUpdateDTO.isActive());
        return userService.saveUser(user);
    }
}
