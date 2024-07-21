package jp.co.jim.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;

@Entity
@Table(name = "T_EXECUTION_HIST")
@Data
public class T_EXECUTION_HIST extends BaseEntity2 {

    @Column(name = "EXEC_ID")
    private String EXEC_ID;

    @Column(name = "PRCS_ID")
    private String PRCS_ID;

    @Column(name = "START_TIME")
    private Date START_TIME;

    @Column(name = "END_TIME")
    private Date END_TIME;

    @Column(name = "STATUS_CODE")
    private int STATUS_CODE;

    @Column(name = "ERROR_DTL")
    private String ERROR_DTL;


}
