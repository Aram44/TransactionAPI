package com.example.transactionapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class User {
    @Id
    private String id;

    @NotEmpty(message = "Name is empty")
    private String name;
    private int money;
    @NotEmpty(message = "Name is empty")
    @Indexed(unique=true)
    @Email(message = "Not correct Emeil")
    private String email;
    private String password;
    private String role;

    public User(){ }

    public User(String name, String email, int money, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "USER";
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}