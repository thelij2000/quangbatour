package com.samsung3.quangbatourdulich.controller;

import com.samsung3.quangbatourdulich.config.Jwt.JwtUtils;
import com.samsung3.quangbatourdulich.dto.request.JwtRequest;
import com.samsung3.quangbatourdulich.dto.request.UserRequestDTO;
import com.samsung3.quangbatourdulich.dto.respone.UserResponseDTO;
import com.samsung3.quangbatourdulich.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    UserService userService;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        try{
            Authentication authentication= authenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                UserResponseDTO userDto=userService.findUserByEmail(userDetails.getUsername());
                String token = jwtUtils.generateToken(userDetails);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("accessToken", token);
                responseBody.put("user", userDto);
                HttpHeaders headers = new HttpHeaders();
                return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    private Authentication authenticate(String email, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        } catch (Exception e) {
            throw new Exception("Authentication failed", e);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody UserRequestDTO userDto){
        if (userService.register(userDto)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản đã tồn tại");
    }
}