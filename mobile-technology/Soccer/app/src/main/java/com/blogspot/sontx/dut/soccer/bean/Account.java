package com.blogspot.sontx.dut.soccer.bean;

import java.lang.String; /**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 08/04/2016.
 */
public class Account {
    private int id;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
