package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.Internal;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.InternalRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private InternalRepository internalRepository;

    private String message = "";
    private String errorMessage = "";

    @RequestMapping("/")
    public String viewHomePage(Model model,Authentication authentication) {
        return home(model, 1,authentication);
    }
    @GetMapping(value = "/page/{page}")
    public String home(Model model,@PathVariable("page") int page,Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        List<Transaction> listTransaction = new ArrayList<>();
        if(user.getRole().equals("NONE")){
            model.addAttribute("user", user);
            return "change-pass";
        }else if(user.getRole().equals("USER")){
            List<Account> accountList = accountRepository.findAllByUid(user.getId());
            for (Account accountItem: accountList) {
                List<Transaction> list = repository.findAllBySenderOrReceiver(accountItem.getId(),accountItem.getId());
                listTransaction.addAll(list);
            }
        }else{
            Pageable pageable = PageRequest.of(page-1, 5);
            Page<Transaction> pages = repository.findAll(pageable);
            listTransaction = pages.getContent();

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pages.getTotalPages());
            model.addAttribute("totalItems", pages.getTotalElements());
        }
        model.addAttribute("role",user.getRole());
        model.addAttribute("uid",user.getId());
        model.addAttribute("listTransaction", listTransaction);
        model.addAttribute("message",this.message);
        model.addAttribute("errorMessage",this.errorMessage);
        this.message = "";
        this.errorMessage = "";
        return "index";
    }
    @GetMapping("/add/{id}")
    public String TransactionAdd(@PathVariable(value = "id") Integer id,Model model,Authentication authentication){
        if(id<0){
            return "redirect:/";
        }
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        List<Account> accountListReceiver = accountRepository.findAllByUid(id);
        List<Account> accountList = accountRepository.findAllByUid(user.getId());
        Transaction transaction = new Transaction();
        transaction.setReceiver(id);
        transaction.setSender(user.getId());
        model.addAttribute("transaction", transaction);
        model.addAttribute("accoutList", accountList);
        model.addAttribute("accoutListReceiver", accountListReceiver);
        return "add";
    }
    @GetMapping("/view/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        if (!repository.existsById(id)){
            return "redirect:/";
        }
        Transaction transaction = repository.findById(id).get();
        Account senderAccount = accountRepository.findById(transaction.getSender()).get();
        Account receiverAccount = accountRepository.findById(transaction.getReceiver()).get();
        User sender = userRepository.findById(senderAccount.getUid()).get();
        User receiver = userRepository.findById(receiverAccount.getUid()).get();

        List<Account> senderList = accountRepository.findAllByUid(sender.getId());
        List<Account> receiverList = accountRepository.findAllByUid(receiver.getId());

        model.addAttribute("transaction", transaction);
        model.addAttribute("senderList", senderList);
        model.addAttribute("receiverList", receiverList);
        model.addAttribute("senderName", sender.getName());
        model.addAttribute("receiverName", receiver.getName());
        return "view";
    }

    @PostMapping("/transaction/add")
    public String NewTransaction(Transaction transaction){
        transaction.setSendtime(LocalDateTime.now());
        transaction.setStatus(0);
        if (!transaction.getSender().equals(transaction.getReceiver()) && transaction.getBalance()>0){
            Transaction newtransaction = repository.saveAndFlush(transaction);
            if (Internal(newtransaction.getId())){
                this.message = "Transaction Saved";
            }
        }
        return "redirect:/";
    }
    @PostMapping("/update")
    public String UpdateTransaction(Transaction transaction,Model model){
        repository.save(transaction);
        return "redirect:/";
    }
    @PostMapping("/view/{action}/{id}")
    public String ActionTransaction(@PathVariable(value = "action") String action,@PathVariable(value = "id") Integer id, Model model){
        try {
            Transaction transaction = repository.findById(id).orElseThrow();
            int status = 0;
            if(action.equals("apply")){
                System.out.println(transaction.getId());
                if (DepositWithdrawal(transaction.getId(), true)){
                    this.message = "Transaction success!";
                }
                status = 1;
            }else if(action.equals("refuse")){
                if (DepositWithdrawal(transaction.getId(), false)){
                    this.errorMessage = "Transaction Refused by Admin!";
                }
                status = 2;
            }else if(action.equals("cancel")){
                this.errorMessage = "Transaction Canceled";
                status = 3;
            }else {
                status = 4;
            }
            transaction.setStatus(status);
            repository.save(transaction);
        }catch (Exception e){
            this.errorMessage = e.getMessage();
        }
        return "redirect:/";
    }

    private Boolean DepositWithdrawal(Integer tid, Boolean deposit){
        try {
            Transaction transaction = repository.findById(tid).get();
            if (transaction!=null){
                Integer balane = transaction.getBalance();
                Integer receiver;
                if (deposit){
                    receiver = transaction.getReceiver();
                }else{
                    receiver = transaction.getSender();
                }

                Account account = accountRepository.findById(receiver).get();
                balane = account.getBalance() + balane;
                account.setBalance(balane);
                accountRepository.save(account);

                Internal internal = internalRepository.findByTid(tid);
                internal.setStatus(1);
                internalRepository.save(internal);
                return  true;
            }
        }catch (Exception e){
            this.errorMessage = e.getMessage();
        }
        return false;
    }

    private Boolean Internal(Integer tid){
        try {
            Transaction transaction = repository.findById(tid).get();
            Internal internal = new Internal();
            internal.setTid(tid);
            internal.setBalance(transaction.getBalance());
            internal.setStatus(0);

            Integer sender = transaction.getSender();
            Account account = accountRepository.findById(sender).get();
            Integer balanceSave = account.getBalance() - transaction.getBalance();
            account.setBalance(balanceSave);

            accountRepository.save(account);
            internalRepository.save(internal);
            return true;
        }catch (Exception e){
            this.errorMessage = e.getMessage();
        }
        return false;
    }
}
