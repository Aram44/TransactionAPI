package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        List<Account> listAccount = repository.findAllByUid(user.getId());
        model.addAttribute("listAccount", listAccount);
        return "account";
    }
    @PostMapping("/account/add")
    public String registerAccount(Model model, Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        Account account = new Account();
        account.setUid(user.getId());
        account.setBalance(0);
        repository.save(account);
        return "redirect:/account";
    }

    @GetMapping("/account/remove/{id}")
    public String deleteAccount(@PathVariable("id") Integer id, Model model, Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        Account account = repository.findById(id).get();
        if (account.getUid().equals(user.getId())){
            account.setUid(0);
            repository.save(account);
        }
        return "redirect:/account";
    }
}
