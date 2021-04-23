package com.example.transactionapi.models;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.utils.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
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
    @Column(name="user_id")
    private Integer uid;
    private float amount;
    private String name;
    private float interest;
    private float monthly;
    private int months;
    private float percent;
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

    @Column(name="user_id")
    public Integer getUid() {
        return uid;
    }

    @Column(name="user_id")
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
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
