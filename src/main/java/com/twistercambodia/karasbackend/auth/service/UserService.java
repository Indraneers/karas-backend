package com.twistercambodia.karasbackend.auth.service;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.repository.UserRepository;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public User findByIdOrThrowError(String id) throws Exception {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User create(UserDto userDto) {
        User user = this.convertToUser(userDto);
        return this.userRepository.save(user);
    }

    public User update(String id, UserDto userDto) throws Exception {
        User user = this.findByIdOrThrowError(id);

        user.setUsername(userDto.getUsername());
        user.setRole(userDto.getRole());

        return this.userRepository.save(user);
    }

    public User delete(String id) throws Exception {
        User user = this.findByIdOrThrowError(id);
        this.userRepository.delete(user);
        return user;
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

    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
