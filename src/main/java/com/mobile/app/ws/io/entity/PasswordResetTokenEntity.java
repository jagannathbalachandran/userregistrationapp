package com.mobile.app.ws.io.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="passwordresettoken")
public class PasswordResetTokenEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

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

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }
}
