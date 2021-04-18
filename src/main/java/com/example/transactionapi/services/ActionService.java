package com.example.transactionapi.services;

import com.example.transactionapi.models.*;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
    public boolean TransactionSave(Integer sid, Integer rid, Integer bal, Type type){
        try {
            Account sender = accountRepository.findById(sid).get();
            if (sender!=null){
                if (type == Type.DEPOSIT){
                    return true;
                }else if(type == Type.WITHDRAWAL){
                    if (sender.getBalance()>=bal){
                        sender.setBalance(sender.getBalance()-bal);
                        sender.setReserv(sender.getReserv()+bal);
                        accountRepository.save(sender);
                        return true;
                    }
                }else {
                    Account receiver = accountRepository.findById(rid).get();
                    if (receiver!=null){
                        if (sender.getBalance()>=bal){
                            sender.setBalance(sender.getBalance()-bal);
                            sender.setReserv(sender.getReserv()+bal);
                            accountRepository.save(sender);
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }
    public boolean Transaction(Integer tid, Status status){
        try {
            Transaction transaction = transactionRepository.findById(tid).get();
            Account sender = accountRepository.findById(transaction.getSender()).get();
            if (transaction!=null){
                if (status==Status.DONE){
                    if (transaction.getType()==Type.INTERNAL){
                        Account receiver = accountRepository.findById(transaction.getReceiver()).get();
                        sender.setReserv(sender.getReserv()-transaction.getBalance());
                        receiver.setBalance(receiver.getBalance()+transaction.getBalance());
                        accountRepository.save(sender);
                        accountRepository.save(receiver);
                        return true;
                    }else if (transaction.getType()==Type.DEPOSIT){
                        sender.setBalance(sender.getBalance()+transaction.getBalance());
                        accountRepository.save(sender);
                        return true;
                    }else {
                        sender.setReserv(sender.getReserv()-transaction.getBalance());
                        accountRepository.save(sender);
                        return true;
                    }
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }
}
