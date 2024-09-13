package com.twistercambodia.karasbackend.auth.controller;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    public List<UserDto> getUsers() {
        return this.userService.convertToUserDto(
                this.userService.findAll()
        );
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return this.userService.convertToUserDto(
                this.userService.create(userDto)
        );
    }
}
