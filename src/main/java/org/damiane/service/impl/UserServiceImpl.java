package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.user.ChangePasswordRequest;
import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.UserService;
import org.damiane.util.user.PasswordGenerator;
import org.damiane.util.user.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticateService authenticateService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticateService authenticateService) {
        this.userRepository = userRepository;
        this.authenticateService = authenticateService;
    }




    @Override
    @Transactional
    public User createUser(String firstName, String lastName) {
        UsernameGenerator usernameGenerator = new UsernameGenerator(userRepository);
        String username = usernameGenerator.generateUsername(firstName, lastName);

        String password = PasswordGenerator.generatePassword(10);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);

        User createdUser =  userRepository.save(user);

        log.info("User Created Successfully");

        return createdUser;
    }


    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public User saveUser(User user) {

        return userRepository.save(user);
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for trainee creation. Transaction ID: {}", transactionId);

        try {
            authenticateService.matchUserCredentials(request.getUsername(), request.getOldPassword());

            User user = userRepository.findByUsername(request.getUsername());

            user.setPassword(request.getNewPassword());
            log.info("Transaction finished for setting password. Transaction ID: {}", transactionId);

            userRepository.save(user);
            log.info("Transaction finished for saving user. Transaction ID: {}", transactionId);

            log.info("Password changed successfully for user: {}", request.getUsername());
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.getUsername(), e);
            throw e;
        }
    }

}
