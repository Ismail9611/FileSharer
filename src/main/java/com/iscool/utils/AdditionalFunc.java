package com.iscool.utils;


import java.sql.Date;

public class AdditionalFunc {

    private AdditionalFunc(){}

    // Checking params before processing
    public static void preCheckingParams(String userFrom, String emailTo, Date activeTo){
        userFrom = userFrom.equals("") ? "UnknownSender" : userFrom;

    }
}
