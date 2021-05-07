package com.example.transactionapi.services.user;

import com.example.transactionapi.config.JwtTokenProvider;
import com.example.transactionapi.exceptions.TransactionException;
import com.example.transactionapi.model.user.Role;
import com.example.transactionapi.model.user.User;
import com.example.transactionapi.repository.user.UserRepository;
import com.example.transactionapi.repository.user.RoleRepository;
import com.example.transactionapi.services.NotificationService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;
    private final MessageSource messageSource;

    UserService(UserRepository userRepository, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider, NotificationService notificationService, MessageSource messageSource){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
    }

    public ResponseEntity<Map<String,String>> registration(User user){
        Map<String,String> result = new HashMap<>();
        User search = userRepository.findByEmail(user.getEmail());
        if (search == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            Role role = roleRepository.findById(3).get();
            user.setRole(role);
            user = userRepository.save(user);
            String link= "http://localhost:3000/verify?t="+jwtTokenProvider.generateToken(user.getEmail(),user.getRole())+"&id="+user.getId();
            String mes="Hello,\n\nFollow this link to verify your email address.\n\n\n"+link;
            if(notificationService.SendNotification(user.getEmail(),mes)){
                result.put("message","Verify email sended to user");
            }else{
                result.put("message","Error!");
            }
        }else{
            result.put("message","User with this email has");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<User> changePassword(User user){
        User search = userRepository.findById(user.getId()).orElseThrow(() -> {throw new TransactionException(messageSource.getMessage("user.not.found", new Object[]{user.getId()}, Locale.ENGLISH));});
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        search.setPassword(encodedPassword);
        Role role = roleRepository.findById(2).get();
        search.setRole(role);
        return new ResponseEntity<>(userRepository.save(search), HttpStatus.OK);
    }
}
