package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.Type;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.services.NotificationService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*")
public class MainController{

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    NotificationService notificationService;

    @GetMapping("/alltransactions")
    public ResponseEntity<Page<Transaction>> findAll(Pageable pageable) {
        return new ResponseEntity<>(transactionRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/transaction/view/{id}")
    public ResponseEntity<Transaction> findByID(@PathVariable Integer id) {
        return new ResponseEntity<>(transactionRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/transaction/{uid}")
    public ResponseEntity<Page<Transaction>> findByUID(@PathVariable Integer uid, Pageable pageable) {
        List<Account> accountList = accountRepository.findAllByUId(uid);
        List<Transaction> listTransaction = new ArrayList<>();
        for (Account accountItem: accountList) {
            List<Transaction> list = transactionRepository.findAllBySenderOrReceiver(accountItem.getId(),accountItem.getId());
            listTransaction.addAll(list);
        }
        Page<Transaction> page = new PageImpl<>(listTransaction);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/transaction")
    public ResponseEntity<Transaction> save(@RequestBody Transaction transaction) {
        System.out.println(transaction.getStatus());
        transaction.setSendtime(LocalDateTime.now());
        Type type = Type.WITHDRAWAL;
        transaction.setType(type);
//        notificationService.SendNotification(email,"Transaction Added");
        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.CREATED);
    }


    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        JSONObject jsonObject = new JSONObject();
        try {
            transactionRepository.deleteById(id);
            jsonObject.put("message","Deleted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}