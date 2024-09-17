package jp.co.jim.common;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //success
    public static final String WSW001 = "WSW001";
    //login failed
    public static final String WSW002 = "WSW002";
    //First time change password.
    public static final String WSW003 = "WSW003";

    //DB Exception
    public static final String WSE001 = "WSE001";

    public static final String WSE007 = "WSE007";

    public static final SimpleDateFormat dateTimeFormatyyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");

    //yyyyMMdd-HHmmss
    public static final SimpleDateFormat dateTimeFormatyyyyMMdd__HHmmss = new SimpleDateFormat("yyyyMMdd-HHmmss");


    //yyyy-MM-dd HH:mm:ss.SSS
    public static final SimpleDateFormat dateTimeFormatyyyyMMdd_HHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    //Message of no record found. WSW001
    public static final String noRecordFound = "No record found.";

    //Message of WSW001_1

    public static final String noRecordFound_toFrontEnd = "No record found. Please change another criteria.";

    //Message of No Criteria Specified. WSW002
    public static final String noCriteriaSpecified = "No Criteria Specified.";

    //Message of search result exceeds  WSW003
    public static final String searchResultCountExceeds = "Count of search result exceeds limit :: %s.";


}
