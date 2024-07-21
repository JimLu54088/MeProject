package jp.co.jim.processor;

import jp.co.jim.SpringbootBatchApplication;
import jp.co.jim.bean.BatchDetails;
import jp.co.jim.bean.utils.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("jp.co.jim.processor.BatchProcessor")
public class BatchProcessor implements BatchProcessorIf {

    @Autowired
    public BatchDetails batchDetails;

    private static final Logger logger = (Logger) LogManager.getLogger(BatchProcessor.class);

    @Override
    public void process() throws Throwable {
        LogUtil.info(logger,"Process....", batchDetails);
    }
}
