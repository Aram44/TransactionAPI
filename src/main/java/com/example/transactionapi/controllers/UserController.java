package com.example.transactionapi.controllers;

import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository repository;
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "add-user";
    }
    @PostMapping("/registeruser")
    public String processRegister(@Valid User user, BindingResult bindingResult) {
        System.out.println(bindingResult.hasErrors());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        repository.save(user);
        return "redirect:/users";
    }
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = repository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }
}
