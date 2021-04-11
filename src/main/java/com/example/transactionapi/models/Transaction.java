package com.example.transactionapi.models;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Transaction {
    @Id
    private String id;
    private String senderUserId, recevierUserId;
    private int status;
    private int price;
    private LocalDateTime sendTime;

    public Transaction(String senderUserId, String recevierUserId, int price) {
        this.senderUserId = senderUserId;
        this.recevierUserId = recevierUserId;
        this.status = 0;
        this.price = price;
        this.sendTime = LocalDateTime.now();
    }

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getRecevierUserId() {
        return recevierUserId;
    }

    public void setRecevierUserId(String recevierUserId) {
        this.recevierUserId = recevierUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
