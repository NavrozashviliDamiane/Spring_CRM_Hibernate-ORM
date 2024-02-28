package org.damiane.service;

import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.damiane.util.PasswordGenerator;
import org.damiane.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User createUser(String firstName, String lastName) {
        // Generate username
        UsernameGenerator usernameGenerator = new UsernameGenerator(userRepository);
        String username = usernameGenerator.generateUsername(firstName, lastName);

        // Generate password
        String password = PasswordGenerator.generatePassword(10);

        // Create user entity
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true); // Assuming newly created users are active by default

        // Save user to the database
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
