package com.example.transactionapi.model.transaction;

import com.example.transactionapi.model.user.Account;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {
    private long id;
    private Account sender;
    private Account receiver;
    private double balance;
    private float fee;
    private int month;
    private Status status;
    private Timestamp sendtime;
    private Type type;

    public Transaction() {
    }

    public Transaction(Account sender, Account receiver, double balance, float fee, int month, Status status, Timestamp sendtime, Type type) {
        this.sender = sender;
        this.receiver = receiver;
        this.balance = balance;
        this.fee = fee;
        this.month = month;
        this.status = status;
        this.sendtime = sendtime;
        this.type = type;
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
    @JoinColumn(name = "sender")
    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "receiver")
    public Account getReceiver() {
        return receiver;
    }


    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getSendtime() {
        return sendtime;
    }

    public void setSendtime(Timestamp sendtime) {
        this.sendtime = sendtime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
