package jp.co.jim.bean;

import jp.co.jim.SpringbootBatchApplication;
import jp.co.jim.bean.utils.LogUtil;
import jp.co.jim.db.entity.T_EXECUTION_HIST;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class BatchDetails extends HashMap<String, Objects> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = (Logger) LogManager.getLogger(SpringbootBatchApplication.class);

    private String applicationName;
    private String batchId;

    private Date startTime;

    private String trackingId;
    private int executionStatus;

    private T_EXECUTION_HIST t_EXECUTION_HIST;

    private List<ErrorDTO> warnings = new ArrayList<ErrorDTO>();
    private List<ErrorDTO> errors = new ArrayList<ErrorDTO>();

    public void addWarning(ErrorDTO warning){
        warnings.add(warning);
        LogUtil.info(logger, errors.size() + " : Warning added.", this);
    }


}
