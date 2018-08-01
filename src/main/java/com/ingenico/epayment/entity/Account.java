package com.ingenico.epayment.entity;

import java.math.BigDecimal;
import javax.persistence.*;


@Entity
@Table(name = "account")
public class Account {

    private Long id;

    private String name;

    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
