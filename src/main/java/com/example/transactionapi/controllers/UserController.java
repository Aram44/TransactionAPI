package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Role;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.RoleRepository;
import com.example.transactionapi.repository.UserRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/allusers")
    public ResponseEntity<Page<User>> findAll(Pageable pageable) {
        return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);
    }

    public ResponseEntity<User> findByID(Integer id) {
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> save(@RequestBody User user) {
        User search = userRepository.findByEmail(user.getEmail());
        if (search == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            Role role = roleRepository.findById(3).get();
            user.setRole(role);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
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
