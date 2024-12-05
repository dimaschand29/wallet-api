package org.example.wallet.dto;

public class LoginResponse {

    private String token;

    // Constructor
    public LoginResponse() {}

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter dan Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

