package com.example.transactionapi.services;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.Status;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NotificationService notificationService;

    public void TransactionNotify(Integer id, Status status){
        Transaction transaction = transactionRepository.findById(id).get();
        Account senderAccount = accountRepository.findById(transaction.getSender()).get();
        Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
        User sender = userRepository.findById(senderAccount.getUid()).get();
        User receiver = userRepository.findById(receiverAccount.getUid()).get();
        if (status == Status.DONE){
            if (receiver!=null){
                notificationService.SendNotification(receiver.getEmail(),"Transaction Applyed!");
            }
            notificationService.SendNotification(sender.getEmail(),"Transaction Applyed!");
        }else if (status == Status.REFUSED){
            if (receiver!=null){
                notificationService.SendNotification(receiver.getEmail(),"Transaction Refused!");
            }
            notificationService.SendNotification(sender.getEmail(),"Transaction Refused!");
        }else{
            if (receiver!=null){
                notificationService.SendNotification(receiver.getEmail(),"Transaction Canceled!");
            }
            notificationService.SendNotification(sender.getEmail(),"Transaction Canceled!");
        }
    }
}
