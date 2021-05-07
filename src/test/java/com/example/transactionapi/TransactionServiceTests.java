package com.example.transactionapi;

import com.example.transactionapi.model.transaction.Transaction;
import com.example.transactionapi.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TransactionServiceTests {
    @Autowired
    TransactionService transactionService;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void criterias(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = query.from(Transaction.class);

        query.select(transactionRoot).where(cb.equal(transactionRoot.get("sender").get("user"), 64), cb.equal(transactionRoot.get("receiver").get("user"), 64));

        TypedQuery<Transaction> query1 = entityManager.createQuery(query);

        List<Transaction> list = query1.getResultList();

        for(Transaction item: list){
            System.out.println(item.getId());
            System.out.println(item.getSender().getUser().getId());
            System.out.println(item.getReceiver().getUser().getId());
            System.out.println(item.getBalance());
        }
    }
//    @Test
//    void SendingMailTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        Boolean result = actionService.CheckAndSend("xudaverdyan1998@yandex.ru","", Status.DONE,20);
//        assertEquals(result, true);
//    }
//    @Test
//    void TransactionLoanTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        Boolean result = actionService.TransactionLoan(90,17,419,2);
//        assertEquals(result, true);
//    }
//    @Test
//    void TransactionSaveTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        float result = actionService.TransactionSave(90,2000, Type.WITHDRAWAL);
//        assertEquals(result, 1000);
//    }
//    @Test
//    void TransactionDepositSaveTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        float result = actionService.TransactionSave(91,2000, Type.DEPOSIT);
//        assertEquals(result, 1);
//    }
//    @Test
//    void TransactionTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        Boolean result = actionService.Transaction(109, Status.DONE);
//        assertEquals(result, true);
//    }
//    @Test
//    void TransactionInternalTest() {
//        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService,currencyConverter);
//        Boolean result = actionService.TransactionInternalSave(90,91,1000);
//        assertEquals(result, true);
//    }
}
