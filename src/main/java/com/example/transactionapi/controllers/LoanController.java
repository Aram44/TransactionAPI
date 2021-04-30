package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Loan;
import com.example.transactionapi.models.Schedule;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.services.LoanService;
import com.example.transactionapi.services.NotificationService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private LoanRepository loanRepository;
    private ScheduleRepository scheduleRepository;
    private LoanService loanService;
    private NotificationService notificationService;

    @Autowired
    LoanController(LoanRepository loanRepository, ScheduleRepository scheduleRepository, LoanService loanService, NotificationService notificationService){
        this.loanRepository = loanRepository;
        this.scheduleRepository = scheduleRepository;
        this.loanService = loanService;
        this.notificationService = notificationService;
    }

    @GetMapping("/allloans")
    public ResponseEntity<Page<Loan>> findAll(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String end, @RequestParam(defaultValue = "5") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return loanService.ShowLaoads(start,end,status,uid,pageable);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Page<Schedule>> findByID(@PathVariable Integer id, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return new ResponseEntity<>(scheduleRepository.findAllByLid(id,pageable), HttpStatus.OK);
    }

    @GetMapping("/viewinfo/{id}")
    public ResponseEntity<Map<String,String>> findInfoByID(@PathVariable Integer id) {
        Map<String,String> result = new HashMap<>();
        float balance = 0;
        List<Schedule> allschedules = scheduleRepository.findAllByLid(id);
        for(Schedule schedule : allschedules){
            balance+= schedule.getBalance();
        }
        result.put("balance", String.valueOf(balance));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/viewloan/{id}")
    public ResponseEntity<Loan> findAllByID(@PathVariable Integer id) {
        return new ResponseEntity<>(loanRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/loan/{action}/{id}")
    public ResponseEntity<String> action(@PathVariable Integer action,@PathVariable Integer id) {
        JSONObject jsonObject = new JSONObject();
        Loan loan = loanRepository.findById(id).get();
            try {
                if(loan!=null){
                    Status status;
                    if (action==1){
                        status = Status.DONE;
                    }else if(action==2){
                        status = Status.REFUSED;
                    }else {
                        status = Status.CANCELED;
                    }
                    if (loanService.loanAction(loan.getId(),status)){
                        loan.setStatus(status);
                        loanRepository.save(loan);
                    }
                    jsonObject.put("message","Email notification sended!");
                }else {
                    jsonObject.put("message","Transaction not found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/loan/{uid}")
    public ResponseEntity<Page<Loan>> findByUID(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String end, @RequestParam(defaultValue = "5") int status, @PathVariable String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return loanService.ShowLaoads(start,end,status,uid,pageable);
    }

    @PostMapping("/loan")
    public ResponseEntity<String> save(@RequestBody Loan loan) {
        JSONObject jsonObject = new JSONObject();
        try {
            loanService.loanSaveorChange(loan);
            jsonObject.put("message","Loan saved successfuly!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.CREATED);
    }

    @DeleteMapping("/loan/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id){
        JSONObject jsonObject = new JSONObject();
        try {
            loanRepository.deleteById(id);
            jsonObject.put("message","Deleted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<Page<Loan>> withFilter(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String finish, @RequestParam(defaultValue = "5") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return loanService.ShowLaoads(start,finish,status,uid,pageable);
    }

    @GetMapping("/report")
    public ResponseEntity<List<Object[]>> balanceInfo(){
        return new ResponseEntity<>(loanRepository.statusGroup(), HttpStatus.OK);
    }
}