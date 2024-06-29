package jp.co.jim.response;

import lombok.Data;

@Data
public class DGLoginResponse {
    private String message;
    private String status;

    public DGLoginResponse(String status, String message) {
        this.status = status;
        this.message = message;

    }
}
