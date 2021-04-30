package com.example.transactionapi.config;


import com.example.transactionapi.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scehduling.enabled", matchIfMissing = true)
public class SchedulingConfiguration {
    @Autowired
    private ActionService actionService;
    @Scheduled(cron = "0 0 7 * * *")
    void SchedulingConfiguration() {
        actionService.NotifyAllAboutPayment();
        System.out.println("Notification sended");
    }
}
