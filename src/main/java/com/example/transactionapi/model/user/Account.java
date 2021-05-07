package com.example.transactionapi.model.user;

import com.example.transactionapi.model.utils.Currency;
import javax.persistence.*;

@Entity(name = "account")
public class Account {
    private long id;
    private User user;
    private Currency currency;
    private double balance;
    private double reserv;

    public Account() {
    }

    public Account(User user, Currency currency, double balance, double reserv) {
        this.user = user;
        this.currency = currency;
        this.balance = balance;
        this.reserv = reserv;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getReserv() {
        return reserv;
    }

    public void setReserv(double reserv) {
        this.reserv = reserv;
    }
}
