package jp.co.jim.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@MappedSuperclass
public class TimeTrackingUserEntityBase {

    private String ins_by;
    private String upd_by;

    private LocalDateTime ins_dt;

    // 自動設置插入日期
    @PrePersist
    protected void onCreate() {
        this.ins_dt = LocalDateTime.now();
    }

    public String getIns_by() {
        return ins_by;
    }

    public void setIns_by(String ins_by) {
        this.ins_by = ins_by;
    }

    public String getUpd_by() {
        return upd_by;
    }

    public void setUpd_by(String upd_by) {
        this.upd_by = upd_by;
    }

    public void setIns_dt(LocalDateTime ins_dt) {
        this.ins_dt = ins_dt;
    }

    public LocalDateTime getIns_dt() {
        return ins_dt;
    }
}
