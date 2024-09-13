package com.twistercambodia.karasbackend.auth.service;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {

        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserDto convertToUserDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }

    public List<UserDto> convertToUserDto(List<User> users) {
        return users
                .stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
}
