package com.example.transactionapi.models;

import com.example.transactionapi.models.utils.LocalDateTimeConverter;
import com.example.transactionapi.models.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer aid;
    private float amount;
    private float interest;
    private float monthly;
    private int months;
    private Status status;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime requesttime;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime changetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public float getMonthly() {
        return monthly;
    }

    public void setMonthly(float monthly) {
        this.monthly = monthly;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(LocalDateTime requesttime) {
        this.requesttime = requesttime;
    }

    public LocalDateTime getChangetime() {
        return changetime;
    }

    public void setChangetime(LocalDateTime changetime) {
        this.changetime = changetime;
    }
}
