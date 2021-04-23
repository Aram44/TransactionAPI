package com.example.transactionapi.controllers;

import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.enums.Type;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.TransactionRepository;
import com.example.transactionapi.services.ActionService;
import com.example.transactionapi.services.TransactionService;
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
import java.util.ArrayList;
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
    private ActionService actionService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/alltransactions")
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return new ResponseEntity<>(transactionRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/transaction/view/{id}")
    public ResponseEntity<Map<String,String>> findByID(@PathVariable Integer id) {
        return transactionService.ShowItem(id);
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
                    }else if(action==2){
                        status = Status.REFUSED;
                    }else {
                        status = Status.CANCELED;
                    }
                    if (actionService.Transaction(transaction.getId(),status)){
                        actionService.TransactionNotify(id,status);
                        transaction.setStatus(status);
                        transactionRepository.save(transaction);
                    }
                    jsonObject.put("message","Email notification sended!");
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
        System.out.println(transaction.getMonth());
        JSONObject jsonObject = new JSONObject();
        try {
            if (transaction.getType()==Type.LOAN){
                if(actionService.TransactionLoan(transaction.getSender(),transaction.getReceiver(),transaction.getBalance(),transaction.getMonth())) {
                    transaction.setFee(0);
                    transaction.setStatus(Status.DONE);
                    transaction.setSendtime(LocalDateTime.now());
                    transactionRepository.save(transaction);
                    jsonObject.put("message", "Transaction Saved");
                }
            }else if(transaction.getType()==Type.INTERNAL) {
                if(actionService.TransactionInternalSave(transaction.getSender(),transaction.getReceiver(),transaction.getBalance())){
                    transaction.setFee(0);
                    transaction.setStatus(Status.PROCESS);
                    transaction.setSendtime(LocalDateTime.now());
                    transactionRepository.save(transaction);
                    jsonObject.put("message","Transaction Saved");
                }
            }else if(transaction.getType()==Type.DEPOSIT) {
                float fee = actionService.TransactionSave(transaction.getSender(),transaction.getBalance(),transaction.getType());
                if(fee==1){
                    transaction.setFee(0);
                    transaction.setStatus(Status.DONE);
                    transaction.setSendtime(LocalDateTime.now());
                    transactionRepository.save(transaction);
                    jsonObject.put("message","Transaction Saved");
                }else {
                    jsonObject.put("error", "Something Wrong");
                }
            }else{
                float fee = actionService.TransactionSave(transaction.getSender(),transaction.getBalance(),transaction.getType());
                if (fee>999){
                    transaction.setSendtime(LocalDateTime.now());
                    transaction.setFee(fee);
                    transactionRepository.save(transaction);
                    jsonObject.put("message","Transaction Saved");
                }else {
                    jsonObject.put("error","You don't have enough funds in your account");
                }
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
    public ResponseEntity<Page<Transaction>> withFilter(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String finish, @RequestParam(defaultValue = "4") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return transactionService.ShowTransactions(start,finish,status,uid,pageable);
    }
}