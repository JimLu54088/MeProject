package jp.co.jim.processor;

import jp.co.jim.bean.utils.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("jp.co.jim.processor.Jim001Processor")
public class Jim001Processor extends BatchProcessor {

    @Autowired
    private static final Logger logger = (Logger) LogManager.getLogger(Jim002Processor.class);


    @Override
    public void process() throws Exception{
        LogUtil.info(logger,"Process..Jim001..", batchDetails);

    }
}
