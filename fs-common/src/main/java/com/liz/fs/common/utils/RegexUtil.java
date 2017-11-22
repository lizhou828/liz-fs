/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.utils.RegexUtil.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 12时26分
 */
public class RegexUtil {
    public static String findMatchContent(String regx, String source) {
        if(StringUtils.isNotEmpty(regx) && StringUtils.isNotEmpty(source)) {
            Matcher m = Pattern.compile(regx).matcher(source);
            if(m.find()) {
                return m.group();
            }
        }

        return null;
    }

    public static List<String> findMatchContents(String regx, String source) {
        if(StringUtils.isNotEmpty(regx) && StringUtils.isNotEmpty(source)) {
            ArrayList lst = new ArrayList();
            Matcher m = Pattern.compile(regx).matcher(source);

            while(m.find()) {
                lst.add(m.group());
            }

            return lst.isEmpty()?null:lst;
        } else {
            return null;
        }
    }
}
