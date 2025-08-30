package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.config.UserDetailsImpl;
import com.samsung3.quangbatourdulich.dto.respone.UserResponseDTO;
import com.samsung3.quangbatourdulich.dto.request.UserRequestDTO;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.enums.Role;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
        user.setPassword(request.getPassword());
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

    public UserResponseDTO updateUser(Integer id, UserRequestDTO request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existingUser.setFullName(request.getFullName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existingUser.setPassword(request.getPassword());
        }
        userRepository.save(existingUser);
        return modelMapper.map(existingUser, UserResponseDTO.class);
    }
    public boolean register(UserRequestDTO customerDto) {
        if (userRepository.existsByEmail(customerDto.getEmail())){
            return false;
        }
        User user=modelMapper.map(customerDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        return true;
    }
    public UserResponseDTO findUserByEmail(String email) {
        User user=userRepository.findUserByEmail(email);
        return modelMapper.map(user, UserResponseDTO.class);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
}
