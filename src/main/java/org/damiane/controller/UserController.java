package org.damiane.controller;

import org.damiane.dto.UserCredentialsDTO;
import org.damiane.entity.User;
import org.damiane.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@ModelAttribute UserCredentialsDTO credentials) {
        List<User> users = userService.getAllUsers(credentials.getUsername(), credentials.getPassword());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}