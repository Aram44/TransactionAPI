package com.example.transactionapi;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.repository.LoanRepository;
import com.example.transactionapi.repository.ScheduleRepository;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.services.LoanService;
import com.example.transactionapi.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoanServiceTests {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    UserRepository userRepository;
    @Test
    void SendingMailTest() {
        LoanService loanService = new LoanService(loanRepository,notificationService,scheduleRepository,userRepository);
        Boolean result = loanService.loanAction(12,Status.DONE);
        assertEquals(result, true);
    }
}
