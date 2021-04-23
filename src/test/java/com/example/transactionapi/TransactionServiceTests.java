package com.example.transactionapi;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.enums.Type;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.repository.user.TransactionRepository;
import com.example.transactionapi.services.ActionService;
import com.example.transactionapi.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class TransactionServiceTests {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private NotificationService notificationService;
    @Test
    void SendingMailTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        Boolean result = actionService.CheckAndSend("xudaverdyan1998@yandex.ru","", Status.DONE,20);
        assertEquals(result, true);
    }
    @Test
    void TransactionLoanTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        Boolean result = actionService.TransactionLoan(90,17,419,2);
        assertEquals(result, true);
    }
    @Test
    void TransactionSaveTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        float result = actionService.TransactionSave(90,2000, Type.WITHDRAWAL);
        assertEquals(result, 1000);
    }
    @Test
    void TransactionDepositSaveTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        float result = actionService.TransactionSave(91,2000, Type.DEPOSIT);
        assertEquals(result, 1);
    }
    @Test
    void TransactionTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        Boolean result = actionService.Transaction(109, Status.DONE);
        assertEquals(result, true);
    }
    @Test
    void TransactionInternalTest() {
        ActionService actionService = new ActionService(transactionRepository, userRepository, accountRepository,scheduleRepository,loanRepository,notificationService);
        Boolean result = actionService.TransactionInternalSave(90,91,1000);
        assertEquals(result, true);
    }
}
