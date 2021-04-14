package com.example.transactionapi.controllers;

import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private String message = "";
    private String errorMessage = "";

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "add-user";
    }
    @PostMapping("/register")
    public String processRegister(@Valid User user, BindingResult bindingResult) {
        System.out.println(bindingResult.hasErrors());
        try {
            User uid = userRepository.findByEmail(user.getEmail());
            if (uid==null){
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.setRole("NONE");
                userRepository.save(user);
                this.message = "User created";
            }else{
                this.errorMessage = "Such user already exists!";
            }
        }catch (Exception e){
            this.message = e.getMessage();
        }
        return "redirect:/users";
    }
    @PostMapping("/changepass")
    public String changepass(User user, Model model){
        System.out.print(user.getId()+" "+user.getName()+" "+user.getEmail());
        user.setRole("USER");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/";
    }
    
    @GetMapping("/allusers")
    public List<User> listUsers() {
        List<User> listUsers = userRepository.findAll();
        return listUsers;
    }
}
