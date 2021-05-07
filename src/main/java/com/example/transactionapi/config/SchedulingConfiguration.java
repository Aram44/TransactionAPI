package com.example.transactionapi.config;


import com.example.transactionapi.controllers.TransactionController;
import com.example.transactionapi.services.ActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scehduling.enabled", matchIfMissing = true)
public class SchedulingConfiguration {
    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @Scheduled(cron = "0 0 7 * * *")
    void SchedulingConfiguration() {
//        actionService.NotifyAllAboutPayment();
        logger.info("Notification sended");
//        actionService.checkForPenalty();
    }
}
