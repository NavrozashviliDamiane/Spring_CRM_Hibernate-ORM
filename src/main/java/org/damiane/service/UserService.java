package org.damiane.service;

import org.damiane.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);

    User createUser(String firstName, String lastName);

    void deleteUser(Long id);

    User saveUser(User user);
}
