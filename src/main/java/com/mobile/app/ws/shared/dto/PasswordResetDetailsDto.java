package com.mobile.app.ws.shared.dto;

import java.io.Serializable;
import java.util.List;

public class PasswordResetDetailsDto implements Serializable {
    private static final Long serialVersionUID = 1L;

    private long id;
    private String email;
    private String password;
    private String token;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
