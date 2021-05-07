package com.example.transactionapi.controllers;

import com.example.transactionapi.exceptions.UserException;
import com.example.transactionapi.model.user.User;
import com.example.transactionapi.model.user.Account;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.RoleRepository;
import com.example.transactionapi.repository.user.UserRepository;
import com.example.transactionapi.services.NotificationService;
import com.example.transactionapi.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController{
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final AccountRepository accountRepository;
    private final MessageSource messageSource;
    private final UserService userService;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, NotificationService notificationService, AccountRepository accountRepository, MessageSource messageSource, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.notificationService = notificationService;
        this.accountRepository = accountRepository;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<User>> findAll(Pageable pageable) {
        return new ResponseEntity<>(userRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
    }

    @GetMapping("/userinfo/{id}")
    public ResponseEntity<User> findUserByAccountId(@PathVariable long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> {throw new UserException(messageSource.getMessage("user.not.found", new Object[]{id}, Locale.ENGLISH)); });
        return new ResponseEntity<>(account.getUser(),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> save(@RequestBody User user) {
        return userService.registration(user);
    }

    @PostMapping("/change")
    public ResponseEntity<User> chengePassword(@RequestBody User user) {
        return userService.changePassword(user);
    }

}
