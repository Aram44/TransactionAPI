package com.example.transactionapi;

import com.example.transactionapi.services.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoanServiceTests {

    @Autowired
    LoanService loanService;

    @Test
    void SendingMailTest() {
//        Boolean result = loanService.loanAction(12,Status.DONE);
//        assertEquals(result, true);
    }
}
