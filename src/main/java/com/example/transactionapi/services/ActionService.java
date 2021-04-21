package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.utils.Status;
import com.example.transactionapi.models.utils.Type;
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
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private NotificationService notificationService;

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
        }else if (status == Status.REFUSED){
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Transaction "+id+" Refused!");
            }
            notificationService.SendNotification(senderEmail,"Transaction "+id+" Refused!");
        }else{
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Transaction "+id+" Canceled!");
            }
            notificationService.SendNotification(senderEmail,"Transaction "+id+" Canceled!");
        }
        return false;
    }
    public boolean TransactionLoan(Integer aid,Integer lid, double balance, Integer month){
        try {
            Account sender = accountRepository.findById(aid).get();
            if (sender.getBalance()>=balance){
                Schedule schedule = scheduleRepository.findByMonth(month);
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
    public float TransactionSave(Integer sid, Integer rid, double bal, Type type){
        try {
            Account sender = accountRepository.findById(sid).get();
            float fee = (float) (bal*0.1>1000 ? bal*0.1 : 1000.0);
            int feeball = (int)Math.ceil(fee+bal);
            if (sender!=null){
                if (type == Type.DEPOSIT){
                    if (bal+1000>fee) {
                        return fee;
                    }
                }else if(type == Type.WITHDRAWAL){
                    System.out.println(sender.getBalance()+" "+feeball);
                    if (sender.getBalance()>=feeball){
                        sender.setBalance(sender.getBalance()-feeball);
                        sender.setReserv(sender.getReserv()+feeball);
                        accountRepository.save(sender);
                        return fee;
                    }
                }else {
                    Account receiver = accountRepository.findById(rid).get();
                    if (receiver!=null){
                        if (sender.getBalance()>=feeball){
                            sender.setBalance(sender.getBalance()-feeball);
                            sender.setReserv(sender.getReserv()+feeball);
                            accountRepository.save(sender);
                            return fee;
                        }
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
}
