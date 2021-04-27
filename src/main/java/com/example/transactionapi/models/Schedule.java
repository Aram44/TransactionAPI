package com.example.transactionapi.models;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.utils.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="loan_id")
    private Integer lid;
    private double balance;
    private int month;
    private LocalDateTime paymantdate;
    private float monthly;
    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="loan_id")
    public Integer getLid() {
        return lid;
    }

    @Column(name="loan_id")
    public void setLid(Integer lid) {
        this.lid = lid;
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

    @Convert(converter = LocalDateTimeConverter.class)
    public LocalDateTime getPaymantdate() {
        return paymantdate;
    }

    @Convert(converter = LocalDateTimeConverter.class)
    public void setPaymantdate(LocalDateTime paymantdate) {
        this.paymantdate = paymantdate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
