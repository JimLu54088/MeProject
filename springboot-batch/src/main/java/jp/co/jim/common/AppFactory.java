package jp.co.jim.common;

import jp.co.jim.processor.BatchProcessorIf;

public interface AppFactory {

    public BatchProcessorIf createInstance(String className);
}
