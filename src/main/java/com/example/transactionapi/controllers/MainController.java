package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.Status;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.services.ActionService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*")
public class MainController{

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionService actionService;

    @GetMapping("/alltransactions")
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return new ResponseEntity<>(transactionRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/transaction/view/{id}")
    public ResponseEntity<Map<String,String>> findByID(@PathVariable Integer id) {
        Map<String,String> result = new HashMap<>();
        try {
            Transaction transaction = transactionRepository.findById(id).get();
            Account senderAccount = accountRepository.findById(transaction.getSender()).get();
            Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
            User sender = userRepository.findById(senderAccount.getUid()).get();
            User receiver = userRepository.findById(receiverAccount.getUid()).get();
            result.put("sender",String.valueOf(transaction.getSender()));
            result.put("receiver",String.valueOf(transaction.getReceiver()));
            result.put("senderName",sender.getName());
            result.put("receiverName",receiver.getName());
            result.put("senderEmail",sender.getEmail());
            result.put("receiverEmail",receiver.getEmail());
            result.put("balance",String.valueOf(transaction.getBalance()));
            result.put("status",String.valueOf(transaction.getStatus()));
            result.put("sendtime",String.valueOf(transaction.getSendtime()));
            result.put("type",String.valueOf(transaction.getType()));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/transaction/{action}/{id}")
    public ResponseEntity<String> action(@PathVariable Integer action,@PathVariable Integer id) {
        JSONObject jsonObject = new JSONObject();
        Transaction transaction = transactionRepository.findById(id).get();
            try {
                if(transaction!=null){
                    Status status;
                    if (action==1){
                        status = Status.DONE;
                        actionService.Transaction(transaction.getId(),Status.DONE);
                    }else if(action==2){
                        status = Status.REFUSED;
                    }else {
                        status = Status.CANCELED;
                    }
                    actionService.TransactionNotify(id,status);
                    transaction.setStatus(status);
                    transactionRepository.save(transaction);
                    jsonObject.put("message","Added");
                }else {
                    jsonObject.put("message","Transaction not found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
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
    public ResponseEntity<String> save(@RequestBody Transaction transaction) {
        JSONObject jsonObject = new JSONObject();
        System.out.println();
        try {
            if (actionService.TransactionSave(transaction.getSender(),transaction.getReceiver(),transaction.getBalance(),transaction.getType())){
                transaction.setSendtime(LocalDateTime.now());
                transactionRepository.save(transaction);
                jsonObject.put("message","Transaction Saved");
            }else {
                jsonObject.put("message","Transaction Error!");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.CREATED);
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
    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<Page<Transaction>> withFilter(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String finish, @RequestParam(defaultValue = "none") String uid, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (start!="none" || finish!="none"){
            LocalDateTime dateStart = LocalDateTime.parse(start+" 00:00:00", formatter);
            LocalDateTime dateFinish = LocalDateTime.parse(finish+" 00:00:00", formatter);
            if(uid.equals("none")){
                return new ResponseEntity<>(transactionRepository.findAllBySendtimeBetween(dateStart,dateFinish,pageable), HttpStatus.OK);
            }else {
                List<Account> accountList = accountRepository.findAllByUId(Integer.valueOf(uid));
                List<Transaction> listTransaction = new ArrayList<>();
                for (Account accountItem : accountList) {
                    List<Transaction> list = transactionRepository.findAllBySenderOrReceiverAndSendtimeBetween(accountItem.getId(), accountItem.getId(), dateStart, dateFinish);
                    listTransaction.addAll(list);
                }
                Page<Transaction> page = new PageImpl<>(listTransaction);
                return new ResponseEntity<>(page, HttpStatus.OK);
            }
        }
        LocalDateTime dateStart = LocalDateTime.parse("0000-00-00 00:00:00", formatter);
        LocalDateTime dateFinish = LocalDateTime.parse("0000-00-00 00:00:00", formatter);
        return new ResponseEntity<>(transactionRepository.findAllBySendtimeBetween(dateStart,dateFinish,pageable), HttpStatus.OK);
    }
}