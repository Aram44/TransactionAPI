package com.example.transactionapi.controllers.implementation;

import com.example.transactionapi.config.JwtTokenProvider;
import com.example.transactionapi.models.AuthRequest;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthorizationResource {

    @Autowired
    private JwtTokenProvider jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String welcome() {
        return "Welcome to javatechie !!";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            return "inavalid username/password";
        }
        return jwtUtil.generateToken(authRequest.getEmail(), userRepository.findByEmail(authRequest.getEmail()).getRole());
    }
}
