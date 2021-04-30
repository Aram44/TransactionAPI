package com.example.transactionapi.controllers;

import com.example.transactionapi.models.Schedule;
import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleController(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Schedule>> findAllSchedulesByDate() {
        return new ResponseEntity<>(scheduleRepository.findAllByPaymantdateBetweenAndStatusNotOrderById(LocalDateTime.now(),LocalDateTime.now().plusDays(3), Status.DONE), HttpStatus.OK);
    }
}
