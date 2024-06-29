package jp.co.jim.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class HisUserOperation {

    private Long ROW_ID;
    private String USER_ID;
    private String USER_OPERATION_DETAILS;

    private String INS_BY;
    private LocalDateTime INS_DT;


}
