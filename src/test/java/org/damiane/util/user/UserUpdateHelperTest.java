package org.damiane.util.user;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.dto.trainer.TrainerUpdateDTO;
import org.damiane.entity.User;
import org.damiane.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUpdateHelperTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserUpdateHelper userUpdateHelper;

    @Test
    public void updateUser_UpdatesUserWithTrainerUpdateDTO_WhenValidUserAndTrainerUpdateDTOProvided() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        trainerUpdateDTO.setFirstName("Jane");
        trainerUpdateDTO.setLastName("Smith");
        trainerUpdateDTO.setActive(false);

        when(userService.saveUser(user)).thenReturn(user);

        User updatedUser = userUpdateHelper.updateUser(user, trainerUpdateDTO);

        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        assertFalse(updatedUser.isActive());
        verify(userService, times(1)).saveUser(user);
    }
}
