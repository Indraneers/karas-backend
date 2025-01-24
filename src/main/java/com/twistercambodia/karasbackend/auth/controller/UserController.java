package com.twistercambodia.karasbackend.auth.controller;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        User user = this.userService.create(userDto);
        this.logger.info("Creating User={}", user);
        return this.userService.convertToUserDto(user);
    }

    @PutMapping("{id}")
    public UserDto updateUser(
            @PathVariable String id,
            @RequestBody UserDto userDto
    ) throws Exception {
        User user = this.userService.update(id, userDto);
        this.logger.info("Updating User={}", user);
        return this.userService.convertToUserDto(user);
    }

    @DeleteMapping("{id}")
    public UserDto deleteUser(@PathVariable String id) throws Exception {
        User user = this.userService.delete(id);
        this.logger.info("Deleting User={}", user);
        return this.userService.convertToUserDto(user);
    }
}
