package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Account;
import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.AccountRepository;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public String home(Model model,Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        if(user.getRole().equals("NONE")){
            model.addAttribute("user", user);
            return "change-pass";
        }else if(user.getRole().equals("USER")){

        }
        List<Transaction> listTransaction = new ArrayList<>();
        List<Account> accountList = accountRepository.findAllByUid(user.getId());
        for (Account accountItem: accountList) {
            System.out.println(accountItem.getId());
            List<Transaction> list = repository.findAllBySenderOrReceiver(accountItem.getId(),accountItem.getId());
            listTransaction.addAll(list);
        }
        model.addAttribute("role",user.getRole());
        model.addAttribute("uid",user.getId());
        model.addAttribute("listTransaction", listTransaction);
        return "index";
    }

    @GetMapping("/view/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        if (!repository.existsById(id)){
            return "redirect:/";
        }
        Optional<Transaction> transaction = repository.findById(id);
        Transaction result = transaction.get();
        model.addAttribute("transaction", result);
        return "view";
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

    @PostMapping("/transaction/add")
    public String NewTransaction(Transaction transaction,Authentication authentication){
        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        transaction.setSender(user.getId());
        transaction.setSendtime(LocalDateTime.now());
        transaction.setStatus(0);
        if (!transaction.getSender().equals(transaction.getReceiver()) && transaction.getBalance()>0){
            repository.save(transaction);
        }
        return "redirect:/";
    }
    @PostMapping("/view/update/{id}")
    public String UpdateTransaction(@PathVariable(value = "id") Integer id, Transaction transaction,Model model){
        transaction.setId(id);
        repository.save(transaction);
        return "redirect:/";
    }
    @GetMapping("/view/remove/{id}")
    public String RemoveTransaction(@PathVariable(value = "id") Integer id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        repository.delete(transaction);
        return "redirect:/";
    }
    @GetMapping("/view/apply/{id}")
    public String ApplyTransaction(@PathVariable(value = "id") Integer id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        return "redirect:/";
    }
    @GetMapping("/view/refuse/{id}")
    public String RefuseTransaction(@PathVariable(value = "id") Integer id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        transaction.setStatus(3);
        repository.save(transaction);
        return "redirect:/";
    }

    private String Deposit(Integer fromID, Integer toID, int balance){
        Optional<User> senderOptional = userRepository.findById(fromID);
        User sender = senderOptional.get();
        Optional<User> reciverOptional = userRepository.findById(toID);
        User reciver = reciverOptional.get();
        if(sender==null || reciver == null){
                return "Transaction success";
        }
        return "Transaction faild";
    }
    private String WithDrowal(Integer fromID, Integer toID, int price){
        Optional<User> senderOptional = userRepository.findById(fromID);
        User sender = senderOptional.get();
        Optional<User> reciverOptional = userRepository.findById(toID);
        User reciver = reciverOptional.get();
        if(sender==null || reciver == null){
                return "Transaction success";
        }
        return "Transaction faild";
    }
}
