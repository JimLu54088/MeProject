package jp.co.jim.common;

import jp.co.jim.processor.BatchProcessorIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppFactoryImpl implements AppFactory{

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public BatchProcessorIf createInstance(String className){
        return (BatchProcessorIf) applicationContext.getBean(className);
    }
}
