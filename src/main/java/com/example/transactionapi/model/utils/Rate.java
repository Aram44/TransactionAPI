package com.example.transactionapi.model.utils;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float usd;
    private float eur;
    private float amd;
    @UpdateTimestamp
    private LocalDateTime updatedtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getUsd() {
        return usd;
    }

    public void setUsd(float usd) {
        this.usd = usd;
    }

    public float getEur() {
        return eur;
    }

    public void setEur(float eur) {
        this.eur = eur;
    }

    public float getAmd() {
        return amd;
    }

    public void setAmd(float amd) {
        this.amd = amd;
    }

    @UpdateTimestamp
    public LocalDateTime getUpdatedtime() {
        return updatedtime;
    }

    @UpdateTimestamp
    public void setUpdatedtime(LocalDateTime updatedtime) {
        this.updatedtime = updatedtime;
    }
}
