package com.example.transactionapi.controllers;

import com.example.transactionapi.exceptions.TransactionException;
import com.example.transactionapi.model.transaction.Transaction;
import com.example.transactionapi.model.transaction.TransactionPage;
import com.example.transactionapi.model.transaction.TransactionSearchCriteria;
import com.example.transactionapi.repository.transaction.TransactionRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.services.ActionService;
import com.example.transactionapi.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ActionService actionService;
    private final TransactionService transactionService;
    private final MessageSource messageSource;

    TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository, ActionService actionService, TransactionService transactionService, MessageSource messageSource){
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.actionService = actionService;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public ResponseEntity<Page<Transaction>> gatTransactions(TransactionPage transactionPage,
                                                             TransactionSearchCriteria transactionSearchCriteria){
        return new ResponseEntity<>(transactionService.getTransactions(transactionPage,transactionSearchCriteria), HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Transaction> findTransactionByID(@PathVariable long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> {
            throw new TransactionException(messageSource.getMessage("loan.not.found", new Object[]{id}, Locale.ENGLISH));});
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String,String>> save(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }


//    @GetMapping("/alllist")
//    @ResponseBody
//    public ResponseEntity<Page<Map<?, ?>>> findAllList(@PageableDefault(page = 0, size = 20) Pageable pageable){
//        int start = (int)pageable.getOffset();
//        actionService.checkForPenalty();
//        int end = Math.min((start + pageable.getPageSize()), transactionRepository.joinTransactionAndUser().size());
//        Page<Map<?, ?>> page = new PageImpl<>(transactionRepository.joinTransactionAndUser().subList(start, end), pageable, transactionRepository.joinTransactionAndUser().size());
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
//
//
//    @GetMapping("/transaction/{action}/{id}")
//    public ResponseEntity<String> action(@PathVariable Integer action,@PathVariable Integer id) {
//        JSONObject jsonObject = new JSONObject();
//        //TODO add exception
//        Transaction transaction = transactionRepository.findById(id).get();
//            try {
//                if(transaction!=null){
//                    Status status;
//                    if (action==1){
//                        status = Status.DONE;
//                    }else if(action==2){
//                        status = Status.REFUSED;
//                    }else {
//                        status = Status.CANCELED;
//                    }
//                    if (actionService.Transaction(transaction.getId(),status)){
//                        actionService.TransactionNotify(id,status);
//                        transaction.setStatus(status);
//                        transactionRepository.save(transaction);
//                    }
//                    jsonObject.put("message","Email notification sended!");
//                }else {
//                    jsonObject.put("message","Transaction not found");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
//    }
//
//
//
//
//
//
//    @DeleteMapping("/transaction/{id}")
//    public ResponseEntity<String> deleteById(@PathVariable Integer id){
//        JSONObject jsonObject = new JSONObject();
//        try {
//            transactionRepository.deleteById(id);
//            jsonObject.put("message","Deleted");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
//    }
//    //    required = false,
//    @GetMapping("/filter")
//    @ResponseBody
//    public ResponseEntity<Page<Transaction>> withFilter(@RequestParam(defaultValue = "none") String start, @RequestParam(defaultValue = "none") String finish, @RequestParam(defaultValue = "5") int status, @RequestParam(defaultValue = "none") String uid, @PageableDefault(page = 0, size = 20) Pageable pageable) {
//        return transactionService.ShowTransactions(start,finish,status,uid,pageable);
//    }
}