package com.example.transactionapi.models;

import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.enums.Type;
import com.example.transactionapi.models.utils.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer sender;
    private Integer receiver;
    @Column(name="sender_id")
    private Integer sid;
    @Column(name="receiver_id")
    private Integer rid;
    private double balance;
    private float fee;
    private int month;
    private Status status;
    private LocalDateTime sendtime;
    private Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    @Column(name="sender_id")
    public Integer getSid() {
        return sid;
    }

    @Column(name="sender_id")
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    @Column(name="receiver_id")
    public Integer getRid() {
        return rid;
    }

    @Column(name="receiver_id")
    public void setRid(Integer rid) {
        this.rid = rid;
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

    @Convert(converter = LocalDateTimeConverter.class)
    @CreationTimestamp
    public LocalDateTime getSendtime() {
        return sendtime;
    }

    @Convert(converter = LocalDateTimeConverter.class)
    @CreationTimestamp
    public void setSendtime(LocalDateTime sendtime) {
        this.sendtime = sendtime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
