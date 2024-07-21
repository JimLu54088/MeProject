package jp.co.jim.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEntity2 {

    public BaseEntity2() {
        INS_DT = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INS_DT", nullable = false, updatable = false)
    private Date INS_DT;

    @Column(name = "UPD_DT")
    private Date UPD_DT;


    @Column(name = "INS_BY")
    private String INS_BY;

    @Column(name = "UPD_BY")
    private String UPD_BY;


}
