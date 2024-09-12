package jp.co.jim.entity;

import lombok.Data;

@Data
public class WarningDTO {
    private String warningCode;
    private String warningMessage;

    public WarningDTO(String warningCode, String warningMessage) {
        this.warningCode = warningCode;
        this.warningMessage = warningMessage;
    }


}
