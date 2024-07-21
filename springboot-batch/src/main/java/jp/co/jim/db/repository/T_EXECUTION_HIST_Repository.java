package jp.co.jim.db.repository;

import jp.co.jim.db.entity.T_EXECUTION_HIST;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface T_EXECUTION_HIST_Repository extends JpaRepository<T_EXECUTION_HIST, Long> {

    @Query("select count(*) from T_EXECUTION_HIST where PRCS_ID = :batchId and END_TIME is NULL")
    Long getRunningBatchCountByBatchID (@Param(value = "batchId") String batchId);


    @Query("select count(e) from T_EXECUTION_HIST e where e.PRCS_ID = :batchId and e.STATUS_CODE = 2 and e.ID > (select max(e2.ID) from T_EXECUTION_HIST e2 where e2.PRCS_ID = :batchId and e2.END_TIME is NULL)")
    Long getStartFailedCountByBatchId (@Param(value = "batchId") String batchId);



}



