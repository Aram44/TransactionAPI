package com.example.transactionapi.model.schedule;

import com.example.transactionapi.model.loan.Status;
import com.example.transactionapi.model.utils.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Schedule {

    private long id;
    private long lid;
    private double balance;
    private int month;
    private float penalty;
    private LocalDateTime paymantdate;
    private float monthly;
    private Status status;

    public Schedule() {

    }
    public Schedule(long lid, double balance, int month, float penalty, LocalDateTime paymantdate, float monthly, Status status) {
        this.lid = lid;
        this.balance = balance;
        this.month = month;
        this.penalty = penalty;
        this.paymantdate = paymantdate;
        this.monthly = monthly;
        this.status = status;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="loan_id")
    public void setLid(long lid) {
        this.lid = lid;
    }

    @Column(name="loan_id")
    public long getLid() {
        return lid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public float getMonthly() {
        return monthly;
    }

    public void setMonthly(float monthly) {
        this.monthly = monthly;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getPenalty() {
        return penalty;
    }

    public void setPenalty(float penalty) {
        this.penalty = penalty;
    }

    @Convert(converter = LocalDateTimeConverter.class)
    public LocalDateTime getPaymantdate() {
        return paymantdate;
    }

    @Convert(converter = LocalDateTimeConverter.class)
    public void setPaymantdate(LocalDateTime paymantdate) {
        this.paymantdate = paymantdate;
    }

    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
