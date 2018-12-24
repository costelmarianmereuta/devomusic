package com.devoteam.devomusic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
public class GoogleUser extends User {

    @NotBlank
    private String provider_pic;

    public GoogleUser() {

    }

    public GoogleUser(String email, String name, String provider_pic) {
        super(email, name);
        this.provider_pic = provider_pic;
    }

    public String getProvider_pic() {
        return provider_pic;
    }

    public void setProvider_pic(String provider_pic) {
        this.provider_pic = provider_pic;
    }
}
