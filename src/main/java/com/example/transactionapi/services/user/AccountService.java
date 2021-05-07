package com.example.transactionapi.services.user;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.exceptions.TransactionException;
import com.example.transactionapi.model.user.Account;
import com.example.transactionapi.model.user.User;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.UserRepository;
import com.example.transactionapi.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class AccountService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AccountRepository accountRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final MessageSource messageSource;

    public AccountService(AccountRepository accountRepository, ScheduleRepository scheduleRepository, UserRepository userRepository, NotificationService notificationService, MessageSource messageSource){
        this.accountRepository = accountRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
    }

    public ResponseEntity<Map<String,String>> createAccount(Account account){
        System.out.println(account.getUser().getId());
        User user = userRepository.findById(account.getUser().getId())
                .orElseThrow(() -> {
                    throw new TransactionException(
                            messageSource.getMessage("user.not.found", new Object[]{
                                    account.getUser().getId()}, Locale.ENGLISH));});
        account.setUser(user);
        account.setBalance(0);
        account.setReserv(0);
        accountRepository.save(account);
        Map<String, String> result = new HashMap<>();
        result.put("message","Account created Successfuly!");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    public ResponseEntity<Page<Account>> findAccounts(Integer uid, Currency currency, Pageable pageable){
//        if (uid !=null){
//            if(currency!= null){
//                return new ResponseEntity<>(accountRepository.findAllByUidAndCurrencyOrderByIdDesc(uid, currency, pageable), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(accountRepository.findAllByUidOrderByIdDesc(uid, pageable), HttpStatus.OK);
//        }else if (currency != null){
//            return new ResponseEntity<>(accountRepository.findAllByCurrencyOrderByIdDesc(currency, pageable), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(accountRepository.findAll(pageable), HttpStatus.OK);
//    }
}
