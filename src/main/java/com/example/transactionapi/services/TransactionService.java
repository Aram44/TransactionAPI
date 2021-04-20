package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.utils.Status;
import com.example.transactionapi.models.utils.Type;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Page<Transaction>> ShowTransactions(String start, String end, int status, String uid, Pageable pageable){
        if (!start.equals("none") || !end.equals("none")){
            LocalDateTime dateStart = LocalDateTime.parse(start+":00");
            LocalDateTime dateFinish = LocalDateTime.parse(end+":00");
            if(uid.equals("none")){
                if (status==4){
                    return new ResponseEntity<>(transactionRepository.findAllBySendtimeBetween(dateStart,dateFinish,pageable), HttpStatus.OK);
                }else {
                    Status state;
                    if (status==0){
                        state = Status.PROCESS;
                    }else if(status==1){
                        state = Status.DONE;
                    }else if (status==2){
                        state = Status.REFUSED;
                    }else{
                        state = Status.CANCELED;
                    }
                    return new ResponseEntity<>(transactionRepository.findAllByStatusAndSendtimeBetween(state, dateStart, dateFinish, pageable), HttpStatus.OK);
                }
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
        }else if(status!=4){
            Status state;
            if (status==0){
                state = Status.PROCESS;
            }else if(status==1){
                state = Status.DONE;
            }else if (status==2){
                state = Status.REFUSED;
            }else{
                state = Status.CANCELED;
            }
            return new ResponseEntity<>(transactionRepository.findAllByStatus(state,pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>(transactionRepository.findAll(pageable), HttpStatus.OK);
    }

    public ResponseEntity<Map<String,String>> ShowItem(@PathVariable Integer id) {
        Map<String, String> result = new HashMap<>();
        try {
            Transaction transaction = transactionRepository.findById(id).get();
            if (transaction.getType() == Type.WITHDRAWAL || transaction.getType() == Type.DEPOSIT) {
                Account senderAccount = accountRepository.findById(transaction.getSender()).get();
                User sender = userRepository.findById(senderAccount.getUid()).get();
                result.put("sender", String.valueOf(transaction.getSender()));
                result.put("senderName", sender.getName());
                result.put("senderEmail", sender.getEmail());
                result.put("balance", String.valueOf(transaction.getBalance()));
                result.put("fee", String.valueOf(transaction.getFee()));
                result.put("status", String.valueOf(transaction.getStatus()));
                result.put("sendtime", String.valueOf(transaction.getSendtime()));
                result.put("type", String.valueOf(transaction.getType()));
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                Account senderAccount = accountRepository.findById(transaction.getSender()).get();
                Account receiverAccount = accountRepository.findById(transaction.getSender()).get();
                User sender = userRepository.findById(senderAccount.getUid()).get();
                User receiver = userRepository.findById(receiverAccount.getUid()).get();
                result.put("sender", String.valueOf(transaction.getSender()));
                result.put("receiver", String.valueOf(transaction.getReceiver()));
                result.put("senderName", sender.getName());
                result.put("receiverName", receiver.getName());
                result.put("senderEmail", sender.getEmail());
                result.put("receiverEmail", receiver.getEmail());
                result.put("balance", String.valueOf(transaction.getBalance()));
                result.put("fee", String.valueOf(transaction.getFee()));
                result.put("status", String.valueOf(transaction.getStatus()));
                result.put("sendtime", String.valueOf(transaction.getSendtime()));
                result.put("type", String.valueOf(transaction.getType()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
