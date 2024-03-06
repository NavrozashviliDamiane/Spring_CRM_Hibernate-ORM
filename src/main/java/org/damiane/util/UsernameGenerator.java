package org.damiane.util;

import org.damiane.repository.UserRepository;

public class UsernameGenerator {

    private UserRepository userRepository;

    public UsernameGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (usernameAlreadyExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }

    private boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
