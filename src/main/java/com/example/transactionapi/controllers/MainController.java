package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Transaction;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.TransactionRepository;
import com.example.transactionapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model){
        Iterable<Transaction> listTransaction = repository.findAll(Sort.by(Sort.Direction.DESC, "sendTime"));
        model.addAttribute("listTransaction", listTransaction);
        return "index";
    }

    @GetMapping("/view/{id}")
    public String show(@PathVariable("id") String id, Model model){
        if (!repository.existsById(id)){
            return "redirect:/";
        }
        Optional<Transaction> transaction = repository.findById(id);
        Transaction result = transaction.get();
        model.addAttribute("transaction", result);
        return "view";
    }

    @GetMapping("/add")
    public String TransactionAdd(Model model){
        model.addAttribute("transaction", new Transaction());
        return "add";
    }

    @PostMapping("/addtransaction")
    public String NewTransaction(Transaction transaction){
        transaction.setSendTime(LocalDateTime.now());
        repository.save(transaction);
        return "redirect:/";
    }
    @PostMapping("/view/{id}/update")
    public String UpdateTransaction(@PathVariable(value = "id") String id, Transaction transaction,Model model){
        transaction.setId(id);
        transaction.setSendTime(LocalDateTime.now());
        repository.save(transaction);
        return "redirect:/";
    }
    @GetMapping("/view/{id}/remove")
    public String RemoveTransaction(@PathVariable(value = "id") String id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        repository.delete(transaction);
        return "redirect:/";
    }
    @GetMapping("/view/{id}/apply")
    public String ApplyTransaction(@PathVariable(value = "id") String id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        String senderId = transaction.getSenderUserId();
        String reciverId = transaction.getRecevierUserId();
        int price = transaction.getPrice();
        Deposit(senderId,reciverId,price);
        return "redirect:/";
    }
    @GetMapping("/view/{id}/refuse")
    public String RefuseTransaction(@PathVariable(value = "id") String id, Model model){
        Transaction transaction = repository.findById(id).orElseThrow();
        transaction.setStatus(3);
        repository.save(transaction);
        return "redirect:/";
    }

    private String Deposit(String fromID, String toID, int price){
        Optional<User> senderOptional = userRepository.findById(fromID);
        User sender = senderOptional.get();
        Optional<User> reciverOptional = userRepository.findById(toID);
        User reciver = reciverOptional.get();
        if(sender==null || reciver == null){
            if(sender.getMoney()<price && price>0){
                sender.setMoney(sender.getMoney()-price);
                reciver.setMoney(reciver.getMoney()+price);
                return "Transaction success";
            }
        }
        return "Transaction faild";
    }
    private String WithDrowal(String fromID, String toID, int price){
        Optional<User> senderOptional = userRepository.findById(fromID);
        User sender = senderOptional.get();
        Optional<User> reciverOptional = userRepository.findById(toID);
        User reciver = reciverOptional.get();
        if(sender==null || reciver == null){
            if(reciver.getMoney()<price && price>0){
                sender.setMoney(sender.getMoney()+price);
                reciver.setMoney(reciver.getMoney()-price);
                return "Transaction success";
            }
        }
        return "Transaction faild";
    }
}
