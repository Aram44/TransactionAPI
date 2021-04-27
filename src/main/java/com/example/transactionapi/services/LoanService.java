package com.example.transactionapi.services;

import com.example.transactionapi.controllers.UserController;
import com.example.transactionapi.models.*;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private LoanRepository loanRepository;
    private NotificationService notificationService;
    private ScheduleRepository scheduleRepository;
    private UserRepository userRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, NotificationService notificationService, ScheduleRepository scheduleRepository, UserRepository userRepository){
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Page<Loan>> ShowLaoads(String start, String end, int status, String uid, Pageable pageable){
        if (!start.equals("none") || !end.equals("none")){
            LocalDateTime dateStart = LocalDateTime.parse(start+":00");
            LocalDateTime dateEnd = LocalDateTime.parse(end+":00");
            if(uid.equals("none")){
                if (status==5){
                    return new ResponseEntity<>(loanRepository.findAllByRequesttimeBetweenOrderByIdDesc(dateStart,dateEnd,pageable), HttpStatus.OK);
                }else {
                    Status state;
                    if (status==0){
                        state = Status.PROCESS;
                    }else if(status==1){
                        state = Status.DONE;
                    }else if (status==2){
                        state = Status.REFUSED;
                    }else if(status==4){
                        state = Status.EDITED;
                    }else{
                        state = Status.CANCELED;
                    }
                    return new ResponseEntity<>(loanRepository.findAllByStatusAndRequesttimeBetweenOrderByIdDesc(state, dateStart, dateEnd, pageable), HttpStatus.OK);
                }
            }else {
                Page<Loan> page = loanRepository.findAllByUidAndRequesttimeBetweenOrderByIdDesc(Integer.valueOf(uid), dateStart, dateEnd, pageable);
                return new ResponseEntity<>(page, HttpStatus.OK);
            }
        }else if(status!=5){
            Status state;
            if (status == 0) {
                state = Status.PROCESS;
            } else if (status == 1) {
                state = Status.DONE;
            } else if (status == 2) {
                state = Status.REFUSED;
            }else if(status==4){
                state = Status.EDITED;
            }else{
                state = Status.CANCELED;
            }
            return new ResponseEntity<>(loanRepository.findAllByStatusOrderByIdDesc(state, pageable), HttpStatus.OK);
        }else if(!uid.equals("none")){
            Page<Loan> page = loanRepository.findAllByUidOrderByIdDesc(Integer.valueOf(uid), pageable);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }
        return new ResponseEntity<>(loanRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
    }

    public boolean loanSaveorChange(Loan loan){
        String message;
        loan.setRequesttime(LocalDateTime.now());
        loan.setChangetime(LocalDateTime.now());
        loanRepository.save(loan);
        String userEmail = userRepository.findById(loan.getUid()).get().getEmail();
        if (loan.getId()!=null){
            message = "New Loan creted by user id: "+loan.getUid();
            notificationService.SendNotification("xudaverdyan@outlook.com", message);
        }else{
            message = "Your loan by id: "+loan.getId()+" edited by admin";
            notificationService.SendNotification(userEmail, message);
        }
        return true;
    }

    public boolean loanAction(Integer id, Status status) {
        try {
            Loan loan = loanRepository.findById(id).get();
            if (loan!=null){
                User user = userRepository.findById(loan.getUid()).get();
                if (status==Status.DONE){
                    List<Schedule> scheduleList = new ArrayList<>();
                    for (int i = 1; i <= loan.getMonths(); i++) {
                        scheduleList.add(new Schedule(null,loan.getId(),0f,i,LocalDateTime.now().plusMonths(i),loan.getMonthly(),Status.PROCESS));
                    }
                    scheduleRepository.saveAll(scheduleList);
                    loan.setChangetime(LocalDateTime.now());
                    loan.setStatus(Status.DONE);
                    loanRepository.save(loan);
                    String message = "Your Loan id: "+loan.getId()+" Ready!";
                    notificationService.SendNotification(user.getEmail(),message);
                }else{
                    loan.setStatus(status);
                    String message = "Your Loan id: "+loan.getId()+" "+status;
                    notificationService.SendNotification(user.getEmail(),message);
                    loanRepository.save(loan);
                }
                return true;
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }
}
