package org.damiane.service;

import org.damiane.dto.ChangePasswordRequest;
import org.damiane.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {

    List<User> getAllUsers(String username, String password);

    User getUserById(Long id, String username, String password);

    User createUser(String firstName, String lastName);

    User saveUser(User user);

    void deleteUser(Long id, String username, String password);


    void changePassword(ChangePasswordRequest request);
}
