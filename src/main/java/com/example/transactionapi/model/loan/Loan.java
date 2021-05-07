package com.example.transactionapi.model.loan;

import com.example.transactionapi.model.utils.Currency;
import com.example.transactionapi.model.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "loan")
public class Loan {
    private long id;
    private User user;
    private float amount;
    private String name;
    private float interest;
    private float monthly;
    private int months;
    private float percent;
    private Currency currency;
    private Status status;
    private Timestamp requesttime;
    private Timestamp changetime;

    public Loan() {
    }

    public Loan(User user, float amount, String name, float interest, float monthly, int months, float percent, Currency currency, Status status, Timestamp requesttime, Timestamp changetime) {
        this.user = user;
        this.amount = amount;
        this.name = name;
        this.interest = interest;
        this.monthly = monthly;
        this.months = months;
        this.percent = percent;
        this.currency = currency;
        this.status = status;
        this.requesttime = requesttime;
        this.changetime = changetime;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(Timestamp requesttime) {
        this.requesttime = requesttime;
    }

    public Timestamp getChangetime() {
        return changetime;
    }

    public void setChangetime(Timestamp changetime) {
        this.changetime = changetime;
    }
}
