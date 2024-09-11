package jp.co.jim.common;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //success
    public static final String DGRP000 = "DGRP000";
    //login failed
    public static final String DGRP001 = "DGRP001";
    //First time change password.
    public static final String DGRP002 = "DGRP002";
    //Expired date less than 7 days
    public static final String DGRP003 = "DGRP003";

    public static final SimpleDateFormat dateTimeFormatyyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");

    //yyyyMMdd-HHmmss
    public static final SimpleDateFormat dateTimeFormatyyyyMMdd__HHmmss = new SimpleDateFormat("yyyyMMdd-HHmmss");


    //yyyy-MM-dd HH:mm:ss.SSS
    public static final SimpleDateFormat dateTimeFormatyyyyMMdd_HHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

}
