/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.utils.FieldRegex.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 12时24分
 */
public class FieldRegex {
    public static String REGEX_Field_MobileNo = "(13|15|18)(\\d{9})";
    public static String REGEX_Field_TPhoneNo = "((\\(\\d{3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}";
    public static String REGEX_Field_PASNo = "0\\d{10,11}";
    public static String REGEX_Field_Email = "\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+";
    public static final String REGEX_FILE_PHOTO = "(jpg|jpeg|gif|png|bmp)";
    public static final String REGEX_FILE_VIDEO = "(flv|avi|mp4)";
    public static final String REGEX_FILE_ALLOWED = "(txt|xls|xlsx|doc|docs|pdf)";

    public FieldRegex() {
    }

    public boolean isPhoneNo(String PhoneNo) {
        return this.isMobileNo(PhoneNo) || this.isTelePhoneNo(PhoneNo) || this.isPASNo(PhoneNo);
    }

    public boolean isMobileNo(String PhoneNo) {
        return StringUtils.isNotEmpty(PhoneNo)?PhoneNo.matches(REGEX_Field_MobileNo):false;
    }

    public boolean isTelePhoneNo(String PhoneNo) {
        return StringUtils.isNotEmpty(PhoneNo)?PhoneNo.matches(REGEX_Field_TPhoneNo):false;
    }

    public boolean isPASNo(String PhoneNo) {
        return StringUtils.isNotEmpty(PhoneNo)?PhoneNo.matches(REGEX_Field_PASNo):false;
    }

    public boolean isEmail(String email) {
        return StringUtils.isNotEmpty(email)?email.matches(REGEX_Field_Email):false;
    }
}
