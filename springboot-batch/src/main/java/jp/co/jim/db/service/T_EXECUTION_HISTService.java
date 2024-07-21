package jp.co.jim.db.service;

import jp.co.jim.db.entity.T_EXECUTION_HIST;
import jp.co.jim.db.repository.T_EXECUTION_HIST_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class T_EXECUTION_HISTService {

    private final T_EXECUTION_HIST_Repository repository;

    @Autowired
    public T_EXECUTION_HISTService(T_EXECUTION_HIST_Repository repository) {
        this.repository = repository;
    }

    public long getRunningBatchCountByBatchID(String batchId) {
        return repository.getRunningBatchCountByBatchID(batchId);
    }

    public long getStartFailedCountByBatchId(String batchId) {
        return repository.getStartFailedCountByBatchId(batchId);
    }

    public T_EXECUTION_HIST save(T_EXECUTION_HIST t_EXECUTION_HIST) {
        return repository.save(t_EXECUTION_HIST);
    }


}
