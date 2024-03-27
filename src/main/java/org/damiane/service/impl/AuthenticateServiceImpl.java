package org.damiane.service.impl;

import org.damiane.entity.User;
import org.damiane.exception.UnauthorizedAccessException;
import org.damiane.repository.UserRepository;
import org.damiane.service.AuthenticateService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;

    public AuthenticateServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean matchUserCredentials(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null || !user.getPassword().equals(password)) {
                throw new UnauthorizedAccessException("User credentials do not match");
            }
            return true;
        } catch (UnauthorizedAccessException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
