package com.example.transactionapi.controllers;

import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.repository.user.AccountRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
        System.out.println(account.getCurrency());
        JSONObject jsonObject = new JSONObject();
        try {
            account.setBalance(0);
            account.setReserv(0);
            accountRepository.save(account);
            jsonObject.put("message","Account created Successfuly!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
    @GetMapping("/remove/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer id){
        JSONObject jsonObject = new JSONObject();
        try {
            Account account = accountRepository.findById(id).get();
            account.setUid(0);
            accountRepository.save(account);
            jsonObject.put("message","Deleted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
