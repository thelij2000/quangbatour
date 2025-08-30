package com.samsung3.quangbatourdulich.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {
    private String email,password;

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
