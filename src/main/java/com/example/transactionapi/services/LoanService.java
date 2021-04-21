package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.utils.Account;
import com.example.transactionapi.models.utils.Status;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public ResponseEntity<Page<Loan>> ShowLaoads(String start, String end, int status, String uid, Pageable pageable){
        if (!start.equals("none") || !end.equals("none")){
            LocalDateTime dateStart = LocalDateTime.parse(start+":00");
            LocalDateTime dateEnd = LocalDateTime.parse(end+":00");
            if(uid.equals("none")){
                if (status==4){
                    return new ResponseEntity<>(loanRepository.findAllByRequesttimeBetween(dateStart,dateEnd,pageable), HttpStatus.OK);
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
                    return new ResponseEntity<>(loanRepository.findAllByStatusAndRequesttimeBetween(state, dateStart, dateEnd, pageable), HttpStatus.OK);
                }
            }else {
                List<Account> accountList = accountRepository.findAllByUId(Integer.valueOf(uid));
                List<Loan> listLoan = new ArrayList<>();
                for (Account accountItem : accountList) {
                    List<Loan> list = loanRepository.findAllByAidAndRequesttimeBetween(accountItem.getId(), dateStart, dateEnd);
                    listLoan.addAll(list);
                }
                Page<Loan> page = new PageImpl<>(listLoan);
                return new ResponseEntity<>(page, HttpStatus.OK);
            }
        }else if(status!=4){
            Status state;
            if (status == 0) {
                state = Status.PROCESS;
            } else if (status == 1) {
                state = Status.DONE;
            } else if (status == 2) {
                state = Status.REFUSED;
            } else {
                state = Status.CANCELED;
            }
            return new ResponseEntity<>(loanRepository.findAllByStatus(state, pageable), HttpStatus.OK);
        }else if(!uid.equals("none")){
            List<Account> accountList = accountRepository.findAllByUId(Integer.valueOf(uid));
            List<Loan> listLoan = new ArrayList<>();
            for (Account accountItem : accountList) {
                List<Loan> list = loanRepository.findAllByAid(accountItem.getId());
                listLoan.addAll(list);
            }
            Page<Loan> page = new PageImpl<>(listLoan);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }
        return new ResponseEntity<>(loanRepository.findAll(pageable), HttpStatus.OK);
    }

    public boolean loanAction(Integer id, Status status) {
        try {
            Loan loan = loanRepository.findById(id).get();
            if (loan!=null){
                if (status==Status.DONE){
                    List<Schedule> scheduleList = new ArrayList<>();
                    for (int i = 1; i <= loan.getMonths(); i++) {
                        scheduleList.add(new Schedule(null,loan.getId(),0f,i,LocalDateTime.now().plusMonths(i),loan.getMonthly(),Status.PROCESS));
                    }
                    scheduleRepository.saveAll(scheduleList);
                    loan.setChangetime(LocalDateTime.now());
                    loan.setStatus(Status.DONE);
                    loanRepository.save(loan);
                }else{

                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }
}
