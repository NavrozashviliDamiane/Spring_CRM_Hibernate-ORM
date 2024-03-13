package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.UserService;
import org.damiane.util.PasswordGenerator;
import org.damiane.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<User> getAllUsers(String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return userRepository.findById(id).orElse(null);
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
    public void deleteUser(Long id, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        userRepository.deleteById(id);
        log.info("User Deleted Successfully");
    }

    @Override
    @Transactional
    public User saveUser(User user) {

        return userRepository.save(user);
    }
}
