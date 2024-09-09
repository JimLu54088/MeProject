package jp.co.jim.entity;

import lombok.Data;

@Data
public class ErrorDTO {


    private String errorCode;
    private String errorMessage;

    public ErrorDTO(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


}
