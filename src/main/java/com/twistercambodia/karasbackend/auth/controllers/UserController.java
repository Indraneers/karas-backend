package com.twistercambodia.karasbackend.auth.controllers;

import com.twistercambodia.karasbackend.auth.entities.User;
import com.twistercambodia.karasbackend.auth.repositories.UserRepository;
import com.twistercambodia.karasbackend.auth.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }
}
