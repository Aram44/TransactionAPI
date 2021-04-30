package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.enums.Currency;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private AccountRepository accountRepository;
    private ScheduleRepository scheduleRepository;
    private NotificationService notificationService;

    public AccountService(AccountRepository accountRepository, ScheduleRepository scheduleRepository, NotificationService notificationService){
        this.accountRepository = accountRepository;
        this.scheduleRepository = scheduleRepository;
        this.notificationService = notificationService;
    }

    public ResponseEntity<Page<Account>> findAccounts(Integer uid, Currency currency, Pageable pageable){
        if (uid !=null){
            if(currency!= null){
                return new ResponseEntity<>(accountRepository.findAllByUidAndCurrencyOrderByIdDesc(uid, currency, pageable), HttpStatus.OK);
            }
            return new ResponseEntity<>(accountRepository.findAllByUidOrderByIdDesc(uid, pageable), HttpStatus.OK);
        }else if (currency != null){
            return new ResponseEntity<>(accountRepository.findAllByCurrencyOrderByIdDesc(currency, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>(accountRepository.findAll(pageable), HttpStatus.OK);
    }
}
