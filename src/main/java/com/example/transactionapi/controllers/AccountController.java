package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*")
public class AccountController{
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/user/{uid}")
    public ResponseEntity<Page<Account>> findByUid(@PathVariable Integer uid, Pageable pageable) {
        return new ResponseEntity<>(accountRepository.findByUid(uid,pageable), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Account account) {
        accountRepository.save(account);
        return new ResponseEntity<>("Account Saved", HttpStatus.OK);
    }
    @GetMapping("/account/remove/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Integer id){
        Account account = accountRepository.findById(id).get();
        account.setUid(0);
        accountRepository.save(account);
        return new ResponseEntity<>("Account Deleted", HttpStatus.OK);
    }
}
