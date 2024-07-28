package jp.co.jim.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class T_basic_kuruma_info_entity {
    private String FIN;
    private String FIN_FAMILY;
    private String CDU_CMNMN;
    private String CDU_CMNMN_VAL;
    private String INS_DT;
    private String INS_BY;
    private String UPD_DT;
    private String UPD_BY;
}
