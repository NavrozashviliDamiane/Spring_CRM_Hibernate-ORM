package org.damiane.service;

import org.damiane.dto.user.ChangePasswordRequest;
import org.damiane.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface UserService {


    User createUser(String firstName, String lastName);

    @Transactional
    void deleteUserById(Long userId);

    User saveUser(User user);

    void changePassword(ChangePasswordRequest request);
}
