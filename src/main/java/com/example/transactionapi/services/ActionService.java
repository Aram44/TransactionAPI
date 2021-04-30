package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.enums.Currency;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.enums.Type;
import com.example.transactionapi.models.utils.Rate;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.services.utils.CurrencyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionService {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private ScheduleRepository scheduleRepository;
    private LoanRepository loanRepository;
    private NotificationService notificationService;
    private CurrencyConverter currencyConverter;


    @Autowired
    public ActionService(TransactionRepository transactionRepository, UserRepository userRepository, AccountRepository accountRepository, ScheduleRepository scheduleRepository, LoanRepository loanRepository, NotificationService notificationService,CurrencyConverter currencyConverter){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.scheduleRepository = scheduleRepository;
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
        this.currencyConverter = currencyConverter;
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
        Transaction transaction = transactionRepository.findById(id).get();
        if (status == Status.DONE){
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Applyed!");
            }
            notificationService.SendNotification(senderEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Applyed!");
            return true;
        }else if (status == Status.REFUSED){
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Refused!");
            }
            notificationService.SendNotification(senderEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Refused!");
            return true;
        }else{
            if (receiverEmail.equals("")){
                notificationService.SendNotification(receiverEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Canceled!");
            }
            notificationService.SendNotification(senderEmail,"Your Transaction by id "+id+" with "+transaction.getBalance()+" balance Canceled!");
            return true;
        }
    }

    public boolean TransactionLoan(Integer aid,Integer lid, double balance, int month){
        try {
            Account sender = accountRepository.findById(aid).get();

            Rate rate = currencyConverter.getCurrency();
            Currency currency = loanRepository.findById(lid).get().getCurrency();
            System.out.println("Curency:"+currency+" balance"+balance);
            double senderBalance = 0;
            double receiverBalance = 0;
            if (sender.getCurrency()==currency){
                receiverBalance = balance;
            }else{
                if (sender.getCurrency() != Currency.USD){
                    if (sender.getCurrency()==Currency.AMD){
                        senderBalance = balance/rate.getAmd();
                    }else {
                        senderBalance = balance/rate.getEur();
                    }
                }else {
                    senderBalance = balance;
                }
                if (currency != Currency.USD){
                    if (currency==Currency.AMD){
                        receiverBalance = senderBalance*rate.getAmd();
                    }else {
                        receiverBalance = senderBalance*rate.getEur();
                        System.out.println("ok");
                    }
                }else{
                    receiverBalance = senderBalance;
                }
            }
            if (sender.getBalance()>=receiverBalance){
                Schedule schedule = scheduleRepository.findByLidAndMonth(lid, month);
                sender.setBalance(sender.getBalance()-balance);
                schedule.setBalance(schedule.getBalance()+receiverBalance);
                if (schedule.getMonthly()<=receiverBalance){
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
        System.out.println(sid+" "+bal+" "+type);
        try {
            Account sender = accountRepository.findById(sid).get();
            Currency currency = sender.getCurrency();
            float fee = 0;
            if (currency==Currency.AMD){
                fee = (float) (bal*0.1>1000 ? bal*0.1 : 1000.0);
            }else if(currency==Currency.EUR){
                fee = (float) (bal*0.1>1.6 ? bal*0.1 : 1.6);
            }else {
                fee = (float) (bal*0.1>2 ? bal*0.1 : 2);
            }
            int feeball = (int)Math.ceil(fee+bal);
            System.out.println(fee);
            if (sender!=null){
                if (type == Type.DEPOSIT){
                    sender.setBalance(sender.getBalance()+bal);
                    accountRepository.save(sender);
                    return 1;
                }else if(type == Type.WITHDRAWAL){
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
                        double senderBalance = 0;
                        double receiverBalance = 0;
                        if (sender.getCurrency()==receiver.getCurrency()){
                            receiverBalance = receiver.getBalance()+transaction.getBalance();
                        }else{
                            Rate rate = currencyConverter.getCurrency();
                            if (sender.getCurrency() != Currency.USD){
                                if (sender.getCurrency()==Currency.AMD){
                                    senderBalance = transaction.getBalance()/rate.getAmd();
                                }else {
                                    senderBalance = transaction.getBalance()/rate.getEur();
                                }
                            }else {
                                senderBalance = transaction.getBalance();
                            }
                            if (receiver.getCurrency() != Currency.USD){
                                if (receiver.getCurrency()==Currency.AMD){
                                    receiverBalance = senderBalance*rate.getAmd();
                                }else {
                                    receiverBalance = senderBalance*rate.getEur();
                                }
                            }else{
                                receiverBalance = senderBalance;
                            }
                        }
                        System.out.println("Sender"+senderBalance+" receiver"+receiverBalance);
                        receiver.setBalance(receiver.getBalance()+receiverBalance);
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
    public void NotifyAllAboutPayment(){
        List<Schedule> listSchedule = scheduleRepository.findAllByPaymantdateBetweenAndStatusNotOrderById(LocalDateTime.now(),LocalDateTime.now().plusDays(3), Status.DONE);
        for(Schedule schedule: listSchedule){
            Loan loan = loanRepository.findById(schedule.getLid()).get();
            User user = userRepository.findById(loan.getUid()).get();
            double balance = schedule.getMonthly()-schedule.getBalance();
            String message = "Your Loan by id: "+loan.getId()+" has payment for "+schedule.getPaymantdate()+" "+String.format("%1.2f", balance)+" "+loan.getCurrency()+" Please pay before that time";
            notificationService.SendNotification(user.getEmail(),message);
        }
    }
}
