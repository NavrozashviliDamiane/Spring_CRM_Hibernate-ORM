package org.damiane.service;

import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean matchUserCredentials(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

}
