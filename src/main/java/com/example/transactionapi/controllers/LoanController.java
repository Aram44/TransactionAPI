package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Loan;
import com.example.transactionapi.models.Schedule;
import com.example.transactionapi.models.utils.Status;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.services.LoanService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private LoanService loanService;

    @GetMapping("/allloans")
    public ResponseEntity<Page<Loan>> findAll(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String end, @RequestParam(defaultValue = "4") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return loanService.ShowLaoads(start,end,status,uid,pageable);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Page<Schedule>> findByID(@PathVariable Integer id, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return new ResponseEntity<>(scheduleRepository.findAllByLid(id,pageable), HttpStatus.OK);
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
    public ResponseEntity<Page<Loan>> findByUID(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String end, @RequestParam(defaultValue = "4") int status, @PathVariable String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return loanService.ShowLaoads(start,end,status,uid,pageable);
    }

    @PostMapping("/loan")
    public ResponseEntity<String> save(@RequestBody Loan loan) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", "Loan Saved");
            loan.setRequesttime(LocalDateTime.now());
            loan.setChangetime(LocalDateTime.now());
            loanRepository.save(loan);
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
    public ResponseEntity<Page<Transaction>> withFilter(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String finish, @RequestParam(defaultValue = "4") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
//        return transactionService.ShowTransactions(start,finish,status,uid,pageable);
        return null;
    }
}