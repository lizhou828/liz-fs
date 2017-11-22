/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.utils.DateUtil.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 12时30分
 */
public class DateUtil {
    public static final String[] FMT_DATE_PATTERNS = new String[]{"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss", "yyyyMMddHHmmssSSS", "yyyyMMddHH", "yyyyMMddHHmm"};
    public static final String FMT_MONTH;
    public static final String FMT_DATE;
    public static final String FMT_DATE_TIME;
    public static final String FMT_TIME = "HH:mm:ss";
    public static final String FMT_TIME_MINU = "HH:mm";
    public static final String FMT_UN_DAY;
    public static final String FMT_UN_DATE_TIME;
    public static final String FMT_UN_DATE_TIME_MILL;

    static {
        FMT_MONTH = FMT_DATE_PATTERNS[2];
        FMT_DATE = FMT_DATE_PATTERNS[4];
        FMT_DATE_TIME = FMT_DATE_PATTERNS[7];
        FMT_UN_DAY = FMT_DATE_PATTERNS[5];
        FMT_UN_DATE_TIME = FMT_DATE_PATTERNS[8];
        FMT_UN_DATE_TIME_MILL = FMT_DATE_PATTERNS[10];
    }


    public static String toUnsignedDay(Date date) {
        return toFmtString(FMT_UN_DAY, date);
    }

    public static String toUnsignedDateTime(Date date) {
        return toFmtString(FMT_UN_DATE_TIME, date);
    }

    public static String toUnsignedDateTimeMill(Date date) {
        return toFmtString(FMT_UN_DATE_TIME_MILL, date);
    }

    public static String toStringMonth(Date date) {
        return toFmtString(FMT_MONTH, date);
    }

    public static String toStringDate(Date date) {
        return toFmtString(FMT_DATE, date);
    }

    public static String toStringDateHH(Date date) {
        return toFmtString(FMT_DATE_PATTERNS[11], date);
    }

    public static String toStringDateHHmm(Date date) {
        return toFmtString(FMT_DATE_PATTERNS[12], date);
    }

    public static String toStringDateTime(Date date) {
        return toFmtString(FMT_DATE_TIME, date);
    }

    public static String toStringTime(Date date) {
        return toFmtString("HH:mm:ss", date);
    }

    public static String toStringTimeMinu(Date date) {
        return toFmtString("HH:mm", date);
    }

    public static String toFmtString(String fmt, Date date) {
        return date != null?(new SimpleDateFormat(StringUtils.isNotEmpty(fmt)?fmt:FMT_DATE)).format(date):null;
    }

    public static Date toDate(String dateString) {
        return toFmtDate(FMT_DATE, dateString);
    }

    public static Date toDateTime(String dateString) {
        return toFmtDate(FMT_DATE_TIME, dateString);
    }

    public static Date toFmtDate(String fmt, String dateString) {
        if(StringUtils.isNotEmpty(dateString)) {
            try {
                return (new SimpleDateFormat(StringUtils.isNotEmpty(fmt)?fmt:FMT_DATE)).parse(dateString);
            } catch (ParseException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static int getDeltaTYear(Date sdate, Date edate) {
        return sdate != null && edate != null?(edate.getYear() > sdate.getYear()?edate.getYear() - sdate.getYear():0):-1;
    }

    public static int getDeltaTDay(Date sdate, Date edate) {
        return sdate != null && edate != null?(edate.getTime() > sdate.getTime()?Integer.parseInt(String.valueOf((edate.getTime() - sdate.getTime()) / 86400000L)):0):-1;
    }

    public static Date getLatelyDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static Date getLatelyThreeDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -3);
        return calendar.getTime();
    }

    public static Date getLatelyWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -7);
        return calendar.getTime();
    }

    public static Date getLatelyMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -1);
        return calendar.getTime();
    }

    public static Date getLatelyThreeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -3);
        return calendar.getTime();
    }
}
