package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.model.transaction.*;
import com.example.transactionapi.model.user.Account;
import com.example.transactionapi.model.utils.Currency;
import com.example.transactionapi.repository.transaction.TransactionCriteriaRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.transaction.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TransactionService {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MessageSource messageSource;
    private final TransactionCriteriaRepository transactionCriteriaRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                              MessageSource messageSource, TransactionCriteriaRepository transactionCriteriaRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.messageSource = messageSource;
        this.transactionCriteriaRepository = transactionCriteriaRepository;
    }

    public Page<Transaction> getTransactions(TransactionPage transactionPage,
                                             TransactionSearchCriteria transactionSearchCriteria){
        return transactionCriteriaRepository.findAllWithFilters(transactionPage, transactionSearchCriteria);
    }


    public ResponseEntity<Map<String,String>> saveTransaction(Transaction transaction){
        System.out.println("OK");
        Map<String,String> result = new HashMap<>();
        Account senderAccount = accountRepository.findById(transaction.getSender().getId()).orElseThrow(() -> {throw new RuntimeException(messageSource.getMessage("account.not.found", new Object[]{transaction.getSender().getId()}, Locale.ENGLISH));});
        transaction.setSender(senderAccount);
        if(transaction.getType()==Type.INTERNAL) {
            if(transactionInternal(transaction.getSender(),transaction.getReceiver(),transaction.getBalance())){
                transaction.setFee(0);
                transaction.setStatus(Status.PROCESS);
                transaction.setSendtime(new Timestamp(new Date().getTime()));
                transactionRepository.save(transaction);
                result.put("message","Transaction Saved");
            }else{
                result.put("error", "You don't have enough funds in your account");
            }
        }else if(transaction.getType()==Type.DEPOSIT) {
            float fee = TransactionSave(transaction.getSender(),transaction.getBalance(),transaction.getType());
            if(fee==1){
                transaction.setFee(0);
                transaction.setStatus(Status.DONE);
                transaction.setSendtime(new Timestamp(new Date().getTime()));
                transactionRepository.save(transaction);
                result.put("message","Transaction Saved");
            }else {
                result.put("error", "Something Wrong");
            }
        }else{
            float fee = TransactionSave(transaction.getSender(),transaction.getBalance(),transaction.getType());
            logger.error("Fee: "+fee);
            if (fee>0){
                transaction.setSendtime(new Timestamp(new Date().getTime()));
                transaction.setFee(fee);
                transactionRepository.save(transaction);
                result.put("message","Transaction Saved");
            }else {
                result.put("error","You don't have enough funds in your account");
            }
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    private float TransactionSave(Account sender, double balance, Type type) {
        try {
            Currency currency = sender.getCurrency();
            float fee = 0;
            if (currency==Currency.AMD){
                fee = (float) (balance*0.1>1000 ? balance*0.1 : 1000.0);
            }else if(currency==Currency.EUR){
                fee = (float) (balance*0.1>1.6 ? balance*0.1 : 1.6);
            }else {
                fee = (float) (balance*0.1>2 ? balance*0.1 : 2);
            }
            int feeball = (int)Math.ceil(fee+balance);
            logger.info("Fee: "+fee+" feebal:"+feeball);
            if (sender!=null){
                if (type == Type.DEPOSIT){
                    sender.setBalance(sender.getBalance()+balance);
                    accountRepository.save(sender);
                    return 1;
                }else if(type == Type.WITHDRAWAL){
                    logger.error("balance:"+sender.getBalance());
                    if (sender.getBalance()>=feeball){
                        sender.setBalance(sender.getBalance()-feeball);
                        sender.setReserv(sender.getReserv()+feeball);
                        accountRepository.save(sender);
                        return fee;
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    private boolean transactionInternal(Account sender, Account receiver, double balance) {
        try{
            if (receiver!=null){
                if (sender.getBalance()>=balance){
                    sender.setBalance(sender.getBalance()-balance);
                    sender.setReserv(sender.getReserv()+balance);
                    accountRepository.save(sender);
                    return true;
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }


//    public ResponseEntity<Page<Transaction>> ShowTransactions(String start, String end, int status, String uid, Pageable pageable){
//        System.out.println(start+" "+end+" "+status+" "+uid);
////        Session session = (Session) entityManager;
////        session.createCriteria(Transaction.class).list();
//        if (!start.equals("none") || !end.equals("none")){
//            LocalDateTime dateStart = LocalDateTime.parse(start+":00");
//            LocalDateTime dateFinish = LocalDateTime.parse(end+":00");
//            if(uid.equals("none")){
//                if (status==4){
//                    return new ResponseEntity<>(transactionRepository.findAllBySendtimeBetweenOrderByIdDesc(dateStart,dateFinish,pageable), HttpStatus.OK);
//                }else {
//                    Status state;
//                    if (status==0){
//                        state = Status.PROCESS;
//                    }else if(status==1){
//                        state = Status.DONE;
//                    }else if (status==2){
//                        state = Status.REFUSED;
//                    }else{
//                        state = Status.CANCELED;
//                    }
//                    return new ResponseEntity<>(transactionRepository.findAllByStatusAndSendtimeBetweenOrderByIdDesc(state, dateStart, dateFinish, pageable), HttpStatus.OK);
//                }
//            }else {
//                if (status==5){
//                    return new ResponseEntity<>(transactionRepository.findAllBySidAndSendtimeBetweenOrderByIdDesc(Integer.valueOf(uid), dateStart, dateFinish ,pageable), HttpStatus.OK);
//                }else {
//                    Status state;
//                    if (status==0){
//                        state = Status.PROCESS;
//                    }else if(status==1){
//                        state = Status.DONE;
//                    }else if (status==2){
//                        state = Status.REFUSED;
//                    }else{
//                        state = Status.CANCELED;
//                    }
//                    return new ResponseEntity<>(transactionRepository.findAllByStatusAndSidAndSendtimeBetweenOrderByIdDesc(state,Integer.valueOf(uid), dateStart, dateFinish,pageable), HttpStatus.OK);
//
//                }
//            }
//        }else if(status!=5){
//            Status state;
//            if (status==0){
//                state = Status.PROCESS;
//            }else if(status==1){
//                state = Status.DONE;
//            }else if (status==2){
//                state = Status.REFUSED;
//            }else{
//                state = Status.CANCELED;
//            }
//            if (uid.equals("none")){
//                return new ResponseEntity<>(transactionRepository.findAllByStatusOrderByIdDesc(state,pageable), HttpStatus.OK);
//            }else {
//                System.out.println("ok");
//                return new ResponseEntity<>(transactionRepository.findAllByStatusAndSidOrderByIdDesc(state,Integer.valueOf(uid),pageable), HttpStatus.OK);
//            }
//
//        }
//        return new ResponseEntity<>(transactionRepository.findAll(pageable), HttpStatus.OK);
//    }
//
//    public ResponseEntity<Map<String,String>> ShowItem(@PathVariable Integer id) {
//        Map<String, String> result = new HashMap<>();
//        try {
//            Transaction transaction = transactionRepository.findById(id).get();
//            if (transaction.getType() == Type.WITHDRAWAL || transaction.getType() == Type.DEPOSIT) {
//                Account senderAccount = accountRepository.findById(transaction.getSender()).get();
//                User sender = userRepository.findById(senderAccount.getUid()).get();
//                result.put("type", String.valueOf(transaction.getType()));
//                result.put("sender", String.valueOf(transaction.getSender()));
//                result.put("senderName", sender.getName());
//                result.put("senderEmail", sender.getEmail());
//                result.put("balanceSender", String.valueOf(transaction.getBalance())+" "+senderAccount.getCurrency());
//                result.put("fee", String.valueOf(transaction.getFee()));
//                result.put("status", String.valueOf(transaction.getStatus()));
//                result.put("sendtime", String.valueOf(transaction.getSendtime()));
//                result.put("type", String.valueOf(transaction.getType()));
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                Account senderAccount = accountRepository.findById(transaction.getSender()).get();
//                Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
//                User sender = userRepository.findById(senderAccount.getUid()).get();
//                User receiver = userRepository.findById(receiverAccount.getUid()).get();
//                double receiverBalance = currencyConverter.convertByValue(senderAccount.getCurrency(),receiverAccount.getCurrency(),transaction.getBalance(),transaction.getSendtime());
//                result.put("type", String.valueOf(transaction.getType()));
//                result.put("sender", String.valueOf(transaction.getSender()));
//                result.put("receiver", String.valueOf(transaction.getReceiver()));
//                result.put("senderName", sender.getName());
//                result.put("receiverName", receiver.getName());
//                result.put("senderEmail", sender.getEmail());
//                result.put("receiverEmail", receiver.getEmail());
//                result.put("balanceSender", String.format("%1.2f", transaction.getBalance())+" "+senderAccount.getCurrency());
//                result.put("balanceReceiver", String.format("%1.2f", receiverBalance)+" "+receiverAccount.getCurrency());
//                result.put("rate1", String.format("%1.2f", currencyConverter.findRate(senderAccount.getCurrency(),transaction.getSendtime())));
//                result.put("rate2", String.format("%1.2f", currencyConverter.findRate(receiverAccount.getCurrency(),transaction.getSendtime())));
//                result.put("fee", String.valueOf(transaction.getFee()));
//                result.put("status", String.valueOf(transaction.getStatus()));
//                result.put("sendtime", String.valueOf(transaction.getSendtime()));
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
}