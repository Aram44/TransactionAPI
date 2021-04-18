package com.example.transactionapi.controllers;

import com.example.transactionapi.config.JwtTokenProvider;
import com.example.transactionapi.models.Role;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.RoleRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.services.NotificationService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController{
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/allusers")
    public ResponseEntity<Page<User>> findAll(Pageable pageable) {
        return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);
    }

    public ResponseEntity<User> findByID(Integer id) {
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> save(@RequestBody User user) {
        User search = userRepository.findByEmail(user.getEmail());
        JSONObject jsonObject = new JSONObject();
        try {
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
                    jsonObject.put("message","Verify email sended to user");
                }else{
                    jsonObject.put("message","Error!");
                }
            }else{
                jsonObject.put("message","User with this email has");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @PostMapping("/change")
    public ResponseEntity<User> update(@RequestBody User user) {
        User search = userRepository.findById(user.getId()).get();
        if (search != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            search.setPassword(encodedPassword);
            Role role = roleRepository.findById(2).get();
            search.setRole(role);
            return new ResponseEntity<>(userRepository.save(search), HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteById(Integer id) {
        JSONObject jsonObject = new JSONObject();
        try {
            userRepository.deleteById(id);
            jsonObject.put("message","Deleted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
