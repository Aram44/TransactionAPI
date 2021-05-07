package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.model.loan.Loan;
import com.example.transactionapi.model.loan.LoanPage;
import com.example.transactionapi.model.loan.LoanSearchCriteria;
import com.example.transactionapi.repository.loan.LoanCriteriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class LoanService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final LoanCriteriaRepository loanCriteriaRepository;


    public LoanService(LoanCriteriaRepository loanCriteriaRepository){
        this.loanCriteriaRepository = loanCriteriaRepository;
    }

    public Page<Loan> getLoans(LoanPage loanPage,
                                             LoanSearchCriteria loanSearchCriteria){
        return loanCriteriaRepository.findAllWithFilters(loanPage,loanSearchCriteria);
    }

//    private boolean transactionLoan(Account sender, Loan loan, double balance, int month) {
//        try {
//            Loan finalLoan = loan;
//            loan = loanRepository.findById(loan.getId()).orElseThrow(() -> {throw new RuntimeException(messageSource.getMessage("loan.not.found", new Object[]{finalLoan.getId()}, Locale.ENGLISH));});
//            Rate rate = currencyConverter.getCurrency();
//            Currency currency = loan.getCurrency();
//            System.out.println("Curency:"+currency+" balance"+balance);
//            double senderBalance = 0;
//            double receiverBalance = 0;
//            if (sender.getCurrency()==currency){
//                receiverBalance = balance;
//            }else{
//                if (sender.getCurrency() != Currency.USD){
//                    if (sender.getCurrency()==Currency.AMD){
//                        senderBalance = balance/rate.getAmd();
//                    }else {
//                        senderBalance = balance/rate.getEur();
//                    }
//                }else {
//                    senderBalance = balance;
//                }
//                if (currency != Currency.USD){
//                    if (currency==Currency.AMD){
//                        receiverBalance = senderBalance*rate.getAmd();
//                    }else {
//                        receiverBalance = senderBalance*rate.getEur();
//                        System.out.println("ok");
//                    }
//                }else{
//                    receiverBalance = senderBalance;
//                }
//            }
//            if (sender.getBalance()>=receiverBalance){
//                Schedule schedule = scheduleRepository.findByLidAndMonth(loan.getId(), month);
//                sender.setBalance(sender.getBalance()-balance);
//                schedule.setBalance(schedule.getBalance()+receiverBalance);
//                if (schedule.getMonthly()<=receiverBalance){
//                    schedule.setStatus(Status.DONE);
//                }
//                accountRepository.save(sender);
//                scheduleRepository.save(schedule);
//                return true;
//            }
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        return false;
//    }

//    public ResponseEntity<Page<Loan>> ShowLaoads(String start, String end, int status, String uid, Pageable pageable){
//        if (!start.equals("none") || !end.equals("none")){
//            LocalDateTime dateStart = LocalDateTime.parse(start+":00");
//            LocalDateTime dateEnd = LocalDateTime.parse(end+":00");
//            if(uid.equals("none")){
//                if (status==5){
//                    return new ResponseEntity<>(loanRepository.findAllByRequesttimeBetweenOrderByIdDesc(dateStart,dateEnd,pageable), HttpStatus.OK);
//                }else {
//                    Status state;
//                    if (status==0){
//                        state = Status.PROCESS;
//                    }else if(status==1){
//                        state = Status.DONE;
//                    }else if (status==2){
//                        state = Status.REFUSED;
//                    }else if(status==4){
//                        state = Status.EDITED;
//                    }else{
//                        state = Status.CANCELED;
//                    }
//                    return new ResponseEntity<>(loanRepository.findAllByStatusAndRequesttimeBetweenOrderByIdDesc(state, dateStart, dateEnd, pageable), HttpStatus.OK);
//                }
//            }else {
//                Page<Loan> page = loanRepository.findAllByUidAndRequesttimeBetweenOrderByIdDesc(Integer.valueOf(uid), dateStart, dateEnd, pageable);
//                return new ResponseEntity<>(page, HttpStatus.OK);
//            }
//        }else if(status!=5){
//            Status state;
//            if (status == 0) {
//                state = Status.PROCESS;
//            } else if (status == 1) {
//                state = Status.DONE;
//            } else if (status == 2) {
//                state = Status.REFUSED;
//            }else if(status==4){
//                state = Status.EDITED;
//            }else{
//                state = Status.CANCELED;
//            }
//            return new ResponseEntity<>(loanRepository.findAllByStatusOrderByIdDesc(state, pageable), HttpStatus.OK);
//        }else if(!uid.equals("none")){
//            Page<Loan> page = loanRepository.findAllByUidOrderByIdDesc(Integer.valueOf(uid), pageable);
//            return new ResponseEntity<>(page, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(loanRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
//    }
//
//    public void loanSaveorChange(Loan loan){
//        String message;
//        loan.setRequesttime(LocalDateTime.now());
//        loan.setChangetime(LocalDateTime.now());
//        loan = loanRepository.save(loan);
//        //TODO add custom exceptions
//        String userEmail = userRepository.findById(loan.getUid()).orElseThrow().getEmail();
//        message = "Your loan "+loan.getName()+" by id: "+loan.getId()+" amount: "+loan.getAmount()+" "+loan.getCurrency()+" interest: "+loan.getInterest()+" "+loan.getCurrency()+" edited by admin";
//        notificationService.SendNotification(userEmail, message);
//    }
//
//    public boolean loanAction(Integer id, Status status) {
//        try {
//            Loan loan = loanRepository.findById(id).orElseThrow();
//            User user = userRepository.findById(loan.getUid()).orElseThrow();
//            if (status==Status.DONE){
//                List<Schedule> scheduleList = new ArrayList<>();
//                for (int i = 1; i <= loan.getMonths(); i++) {
//                    scheduleList.add(new Schedule(loan.getId(),0f,i,0f,LocalDateTime.now().plusMonths(i),loan.getMonthly(),Status.PROCESS));
//                }
//                scheduleRepository.saveAll(scheduleList);
//                loan.setChangetime(LocalDateTime.now());
//                loan.setStatus(Status.DONE);
//                loanRepository.save(loan);
//                String message = "Your Loan id: "+loan.getId()+" Ready!";
//                notificationService.SendNotification(user.getEmail(),message);
//            }else{
//                loan.setStatus(status);
//                String message = "Your Loan id: "+loan.getId()+" "+status;
//                notificationService.SendNotification(user.getEmail(),message);
//                loanRepository.save(loan);
//            }
//            return true;
//        }catch (Exception e){
//            e.getMessage();
//        }
//        return false;
//    }
}
