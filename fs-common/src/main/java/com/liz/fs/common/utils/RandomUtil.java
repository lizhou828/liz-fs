/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.utils.RandomUtil.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 12时30分
 */
public class RandomUtil {
    public static final Random random = new Random();
    private static int sequence = 0;

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int number) {
        return random.nextInt(number);
    }

    public static int nextInt(int minNumber, int maxNumber) {
        return nextInt(maxNumber - minNumber + 1) + minNumber;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDA3() {
        return UUID.randomUUID().toString() + "-" + sequenceNext(999);
    }

    public static String getSn() {
        return getSnByDTime() + sequenceNext(999);
    }

    public static String getSnByDay() {
        return DateUtil.toUnsignedDay(new Date());
    }

    public static String getSnByDTime() {
        return DateUtil.toUnsignedDateTime(new Date());
    }

    public static String getSnByMill() {
        return DateUtil.toUnsignedDateTimeMill(new Date());
    }

    public static String getSnByMillA3() {
        return DateUtil.toUnsignedDateTimeMill(new Date()) + sequenceNext(999);
    }

    public static String sequenceNext(int maxvalue) {
        if(sequence >= maxvalue) {
            sequence = 0;
        }

        return StringUtils.leftPad(String.valueOf(++sequence), String.valueOf(maxvalue).length(), "0");
    }
}
