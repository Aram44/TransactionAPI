package com.example.transactionapi.models.utils;

import com.example.transactionapi.models.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="user_id")
    private Integer uid;
    private Currency currency;
    private double balance;
    private double reserv;

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
