package jp.com.jim.processor;

import jp.com.jim.dto.BatchDTO;

public interface BatchProcessorIF {

    void process(BatchDTO batchDTO);

}
