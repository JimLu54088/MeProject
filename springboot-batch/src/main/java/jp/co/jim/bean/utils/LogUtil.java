package jp.co.jim.bean.utils;

import jp.co.jim.bean.BatchDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

public class LogUtil {

    private LogUtil() {

    }

    public static void info(Logger logger, String message, BatchDetails batchDetails) {
        logger.info("{} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(message)
        );
    }

    public static void info(Logger logger, String code, String message, BatchDetails batchDetails) {
        logger.info("{} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(code),
                StringUtils.normalizeSpace(message)
        );
    }

    public static void debug(Logger logger, String message, BatchDetails batchDetails) {
        logger.debug("{} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(message)
        );
    }

    public static void debug(Logger logger, String message, BatchDetails batchDetails, Throwable th) {
        logger.debug("{} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(message), th);
    }


    public static void warn(Logger logger, String message, BatchDetails batchDetails) {
        logger.warn("{} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(message)
        );
    }

    public static void warn(Logger logger, String code, String message, BatchDetails batchDetails) {
        logger.warn("{} {} {} {}",
                StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(code),
                StringUtils.normalizeSpace(message)
        );
    }

    public static void error(Logger logger, String code, String message, BatchDetails batchDetails, Throwable th) {
        logger.error("{} {} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(code),
                StringUtils.normalizeSpace(message), th);
    }

    public static void error(Logger logger, String code, String message, BatchDetails batchDetails) {
        logger.error("{} {} {} {}", StringUtils.normalizeSpace(getBatchId(batchDetails)),
                StringUtils.normalizeSpace(getTrackingId(batchDetails)),
                StringUtils.normalizeSpace(code),
                StringUtils.normalizeSpace(message));
    }

    private static String getBatchId(BatchDetails batchDetails) {
        if (null == batchDetails) {
            return "";
        }
        return batchDetails.getBatchId();
    }

    private static String getTrackingId(BatchDetails batchDetails) {
        if (null == batchDetails) {
            return "";
        }
        return batchDetails.getTrackingId();
    }

}
