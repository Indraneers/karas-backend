package com.twistercambodia.karasbackend.auth.services;

import com.twistercambodia.karasbackend.auth.entities.User;
import com.twistercambodia.karasbackend.auth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
