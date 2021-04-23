package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.enums.Type;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private ScheduleRepository scheduleRepository;
    private LoanRepository loanRepository;
    private NotificationService notificationService;


    @Autowired
    public ActionService(TransactionRepository transactionRepository, UserRepository userRepository, AccountRepository accountRepository, ScheduleRepository scheduleRepository, LoanRepository loanRepository, NotificationService notificationService){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.scheduleRepository = scheduleRepository;
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
    }

    public void TransactionNotify(Integer id, Status status){
        Transaction transaction = transactionRepository.findById(id).get();
        if (transaction.getType()== Type.WITHDRAWAL){
            Account senderAccount = accountRepository.findById(transaction.getSender()).get();
            User sender = userRepository.findById(senderAccount.getUid()).get();
            System.out.println(sender.getName());
            CheckAndSend(sender.getEmail(), "",status,id);
        }else if(transaction.getType()==Type.DEPOSIT){
            Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
            User receiver = userRepository.findById(receiverAccount.getUid()).get();
            CheckAndSend(receiver.getEmail(), "",status,id);
        }else{
            Account senderAccount = accountRepository.findById(transaction.getSender()).get();
            User sender = userRepository.findById(senderAccount.getUid()).get();
            Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
            User receiver = userRepository.findById(receiverAccount.getUid()).get();
            CheckAndSend(sender.getEmail(), receiver.getEmail(), status,id);
        }
    }
    public boolean CheckAndSend(String senderEmail, String receiverEmail,Status status,Integer id){
        if (status == Status.DONE){
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Transaction "+id+" Applyed!");
            }
            notificationService.SendNotification(senderEmail,"Transaction "+id+" Applyed!");
            return true;
        }else if (status == Status.REFUSED){
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Transaction "+id+" Refused!");
            }
            notificationService.SendNotification(senderEmail,"Transaction "+id+" Refused!");
            return true;
        }else{
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Transaction "+id+" Canceled!");
            }
            notificationService.SendNotification(senderEmail,"Transaction "+id+" Canceled!");
            return true;
        }
    }

    public boolean TransactionLoan(Integer aid,Integer lid, double balance, int month){
        try {
            Account sender = accountRepository.findById(aid).get();
            if (sender.getBalance()>=balance){
                System.out.println(sender.getBalance()+" "+lid+" "+balance+" "+month);
                Schedule schedule = scheduleRepository.findByLidAndMonth(lid, month);
                System.out.println(schedule);
                sender.setBalance(sender.getBalance()-balance);
                schedule.setBalance(schedule.getBalance()+balance);
                if (schedule.getMonthly()<=balance){
                    schedule.setStatus(Status.DONE);
                }
                accountRepository.save(sender);
                scheduleRepository.save(schedule);
                return true;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }
    public float TransactionSave(Integer sid, double bal, Type type){
        try {
            Account sender = accountRepository.findById(sid).get();
            float fee = (float) (bal*0.1>1000 ? bal*0.1 : 1000.0);
            int feeball = (int)Math.ceil(fee+bal);
            if (sender!=null){
                if (type == Type.DEPOSIT){
                    sender.setBalance(sender.getBalance()+bal);
                    accountRepository.save(sender);
                    return 1;
                }else if(type == Type.WITHDRAWAL){
                    System.out.println(sender.getBalance()+" "+feeball);
                    if (sender.getBalance()>=feeball){
                        sender.setBalance(sender.getBalance()-feeball);
                        sender.setReserv(sender.getReserv()+feeball);
                        accountRepository.save(sender);
                        return fee;
                    }
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        return 0;
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
                        receiver.setBalance(receiver.getBalance()+(transaction.getBalance()+(int)transaction.getFee()));
                        accountRepository.save(sender);
                        accountRepository.save(receiver);
                        return true;
                    }else if (transaction.getType()==Type.DEPOSIT){
                        sender.setBalance(sender.getBalance()+(transaction.getBalance()+(int)transaction.getFee()));
                        accountRepository.save(sender);
                        return true;
                    }else {
                        sender.setReserv(sender.getReserv()-(transaction.getBalance()+(int)transaction.getFee()));
                        accountRepository.save(sender);
                        return true;
                    }
                }else{
                    sender.setBalance(sender.getBalance()+(transaction.getBalance()+(int)transaction.getFee()));
                    sender.setReserv(sender.getReserv()-(transaction.getBalance()+(int)transaction.getFee()));
                    accountRepository.save(sender);
                    return true;
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }


    public boolean TransactionInternalSave(Integer sid, Integer rid, double balance) {
        try{
            Account sender = accountRepository.findById(sid).get();
            Account receiver = accountRepository.findById(rid).get();
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
}
