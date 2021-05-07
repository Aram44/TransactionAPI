package com.example.transactionapi.model.user;

import javax.persistence.*;

@Entity
public class Role {
    private Integer id;
    private String name;

    public Role() {
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}