package com.example.transactionapi.controllers;

import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
            User uid = repository.findByEmail(user.getEmail());
            if (uid==null){
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.setRole("NONE");
                repository.save(user);
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
        repository.save(user);
        return "redirect:/";
    }
    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = repository.findByEmail(userPrincipal.getUsername());
        List<User> listUsers;
        if (user.getRole().equals("USER")){
            listUsers = repository.findByRoleAndEmailNotContaining(user.getRole(),userPrincipal.getUsername());
        }else {
            listUsers = repository.findByEmailNotContaining(userPrincipal.getUsername());
        }

        model.addAttribute("role",user.getRole());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("message",this.message);
        model.addAttribute("errorMessage",this.errorMessage);
        this.message = "";
        this.errorMessage = "";
        return "users";
    }
}
