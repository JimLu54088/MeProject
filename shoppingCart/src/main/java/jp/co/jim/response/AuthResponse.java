package jp.co.jim.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private String userId;

    public AuthResponse(){

    }

    public AuthResponse(String jwt, String message, String userId) {
        this.jwt = jwt;
        this.message = message;
        this.userId = userId;
    }
}
