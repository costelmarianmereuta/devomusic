package com.devoteam.devomusic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
public class PasswordUser extends User {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public PasswordUser() {

    }

    public PasswordUser(String name, String username, String email, String password) {
        super(email, name);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


