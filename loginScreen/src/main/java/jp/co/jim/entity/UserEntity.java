package jp.co.jim.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEntity {

    private Long ID;
    private String user_id;
    private String user_password;
    private int role_type;
    private int role_flag;
    private LocalDateTime ins_dt;
    private String ins_by;
    private LocalDateTime upd_dt;
    private String upd_by;


}
