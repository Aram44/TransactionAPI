package com.example.transactionapi.controllers;

import com.example.transactionapi.repository.ScheduleRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {
    ScheduleRepository scheduleRepository;

    ScheduleController(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

//    @GetMapping("/")
//    public ResponseEntity<List<Schedule>> findAllSchedulesByDate() {
//        return new ResponseEntity<>(scheduleRepository.findAllByPaymantdateBetweenAndStatusNotOrderById(LocalDateTime.now(),LocalDateTime.now().plusDays(3), Status.DONE), HttpStatus.OK);
//    }
}
