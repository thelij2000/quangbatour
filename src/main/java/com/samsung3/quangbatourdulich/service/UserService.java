package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.dto.respone.UserResponseDTO;
import com.samsung3.quangbatourdulich.dto.request.UserRequestDTO;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.enums.Role;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = modelMapper.map(request, User.class);
        user.setPasswordHash(request.getPassword());
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
