package jp.co.jim.bean;

import lombok.Data;

@Data
public class ErrorDTO {
    public ErrorDTO(){

    }

    private String errorCode;
    private String errorMessage;

    public ErrorDTO(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
