package jp.co.jim.response;

import lombok.Data;

@Data
public class DGLoginResponse {
    private String message;
    private String status;
    private String token;

    public DGLoginResponse(String status, String message) {
        this.status = status;
        this.message = message;

    }

    public DGLoginResponse(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

}
