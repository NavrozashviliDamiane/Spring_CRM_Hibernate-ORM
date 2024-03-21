package org.damiane.service;

import org.damiane.dto.user.ChangePasswordRequest;
import org.damiane.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface UserService {

    List<User> getAllUsers(String username, String password);

    User getUserById(Long id, String username, String password);

    User createUser(String firstName, String lastName);

    @Transactional
    void deleteUserById(Long userId);

    User saveUser(User user);

    void deleteUser(Long id, String username, String password);


    void changePassword(ChangePasswordRequest request);
}
