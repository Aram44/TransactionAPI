package com.example.transactionapi;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.user.AccountRepository;
import com.example.transactionapi.services.ActionService;
import com.example.transactionapi.services.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoanServiceTests {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Test
    void SendingMailTest() {
        LoanService loanService = new LoanService(loanRepository,accountRepository,scheduleRepository);
        Boolean result = loanService.loanAction(12,Status.DONE);
        assertEquals(result, true);
    }
}
