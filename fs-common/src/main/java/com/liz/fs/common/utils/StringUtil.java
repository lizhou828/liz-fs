package com.liz.fs.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

/**
 * <p>
 * 字符串Util
 * </p>
 * 
 * <p>
 * Copyright: 2009 . All rights reserved.
 * </p>
 * <p>
 * Company: 国裕网络科技有限公司
 * </p>
 * <p>
 * CreateDate:2009-04-14
 * </p>
 * 
 * @author 杨毅
 * @version 1.0
 */
public class StringUtil {
	/**
	 * 获取 字符串是否空
	 * 
	 * @param string
	 *            字符串
	 * @return 字符串是否空
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.equals("") || string.trim().equals("") || "null".equalsIgnoreCase(string);
	}

	/**
	 * 获取 字符串是否空
	 *
	 * @param string
	 *            字符串
	 * @return 字符串是否空
	 */
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}
    
	/**判断字符串是否有效**/
	public static boolean isValid(String string) {
		if (string == null || (string.trim()).equals("") || ((string.trim()).toLowerCase()).equals("null") ){
			return false;
		}else {
			return true;
		}
		
	}
	
	/**
	 * 字符串s1是否等于s2
	 * 
	 * @param s1
	 *            字符1
	 * @param s2
	 *            字符2
	 * @param isIgnoredCase
	 *            是否忽略大小写
	 * @return 是否相等
	 */
	public static boolean isEqual(String s1, String s2, boolean isIgnoredCase) {

		if (s1 == null || s2 == null)
			return false;

		if (isIgnoredCase)
			return s1.equalsIgnoreCase(s2);
		else
			return s1.equals(s2);

	}

	/**
	 * 字符串s1是否包含s2
	 * 
	 * @param s1
	 *            字符串s1
	 * @param s2
	 *            字符串s2
	 * @return 是否包含
	 */
	public static boolean containsString(String s1, String s2) {
		if (s2 == null) {
			return true;
		}

		if (s1 == null) {
			return false;
		}

		return s1.contains(s2);
	}

	/**
	 * 将Hashtable的内容转化成key:value形式的字符串
	 * 
	 * @param hashtable Hashtable的内容
	 * @return key:value形式的字符串
	 * @since 1.1
	 */
	public static String Hashtable2String(Hashtable<String, String> hashtable) {
		StringBuffer buffer = new StringBuffer();
		if (hashtable != null && !hashtable.isEmpty()) {

			Enumeration<String> keys = hashtable.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = hashtable.get(key);
				buffer.append(String.format("%s:%s\n", key, value));
			}

		}
		return buffer.toString();
	}

	/**
	 * 将key:value形式的字符串内容转化成Hashtable
	 * 
	 * @param str
	 * @return Hashtable
	 * @since 1.1
	 */
	public static Hashtable<String, String> String2Hashtable(String str) {
		Hashtable<String, String> result = new Hashtable<String, String>();

		/** 将key:value格式的字符串转换成Hashtable */
		if (!StringUtil.isEmpty(str)) {
			String[] items = str.split("\n");
			for (String item : items) {
				if (!StringUtil.isEmpty(item)) {
					int index = item.indexOf(':');
					if (index > 0) {
						String key = item.substring(0, index - 1);
						String value = item.substring(index + 1);
						result.put(key, value);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 将String[]的内容转化成用逗号分隔的字符串
	 * 
	 * @param array 字符串数组
	 * @return 用逗号分隔的字符串
	 * @since 1.1
	 */
	public static String array2String(String[] array) {
		return array2String(array, ",");
	}

	/**
	 * 将String[]的内容转化成用指定分隔符分隔的字符串
	 * 
	 * @param array
	 *            字符串数组
	 * @param seperator
	 *            分隔符
	 * @return 字符串
	 * @since 1.1
	 */
	public static String array2String(String[] array, String seperator) {
		StringBuffer buffer = new StringBuffer();
		if (array != null && array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				buffer.append(array[i]);
				if (i < array.length - 1) {
					buffer.append(seperator);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取参数信息
	 * 
	 * @param params
	 *            参数字符串，类似name=abc&age=&a=c
	 * @param name
	 *            要获取的参数名称，如name
	 * @return 该名称的值
	 * @throws Exception
	 */
	public static String getParamValue(String params, String name)
			throws Exception {
		return getParamValue(params, name, "utf-8");
	}

	/**
	 * 获取参数信息
	 * 
	 * @param params
	 *            参数字符串，类似name=abc&age=&a=c
	 * @param name
	 *            要获取的参数名称，如name
	 * @param enc
	 *            编码形式，如utf-8
	 * @return 该名称的值
	 * @throws Exception
	 */
	public static String getParamValue(String params, String name, String enc)
			throws Exception {
		String result = "";
		for (String param : params.split("&")) {
			if (!StringUtil.isEmpty(param)) {
				int index = param.indexOf("=");
				String key = param.substring(0, param.indexOf("="));
				if (name.equals(key)) {
					result = param.substring(index + 1);
					if (!StringUtil.isEmpty(enc)) {
						result = URLDecoder.decode(result, enc);
					}
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 获取参数信息
	 * 
	 * @param params
	 *            参数字符串，类似name=abc&age=&a=c
	 * @param name
	 *            要获取的参数名称，如name
	 * @return 该名称的值
	 * @throws Exception
	 */
	public static String[] getParamValues(String params, String name)
			throws Exception {
		return getParamValues(params, name, "utf-8");
	}

	/**
	 * 获取参数信息
	 * 
	 * @param params
	 *            参数字符串，类似name=abc&age=&a=c
	 * @param name
	 *            要获取的参数名称，如name
	 * @param enc
	 *            编码形式，如utf-8
	 * @return 该名称的值
	 * @throws Exception
	 */
	public static String[] getParamValues(String params, String name, String enc)
			throws Exception {
		ArrayList<String> values = new ArrayList<String>();

		for (String param : params.split("&")) {
			if (!StringUtil.isEmpty(param)) {
				int index = param.indexOf("=");
				String key = param.substring(0, param.indexOf("="));
				if (name.equals(key)) {
					String value = param.substring(index + 1);
					if (!StringUtil.isEmpty(enc)) {
						value = URLDecoder.decode(value, enc);
					}

					values.add(value);
				}
			}
		}

		String[] result = new String[values.size()];
		values.toArray(result);

		return result;
	}

	/**
	 * 替换HTML的特殊字符，如&替换成&amp;
	 * 
	 * @param str
	 *            字符串
	 * @return 替换特殊字符后的字符串
	 */
	public static String replaceHtmlCharater(String str) {
		if (isEmpty(str)) {
			return str;
		}

		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("\"", "&quot;");

		return str;
	}

	public static String replaceSign(String str){
		if (isEmpty(str)) {
			return str;
		}
		str=str.replaceAll("<.*?>","");
		str=str.replaceAll("</?[^>]+>","");
		str=str.replaceAll("&rdquo;","");
		str=str.replaceAll("&middot;","");
		str=str.replaceAll("&ldquo;","");
		str=str.replaceAll("&nbsp;","");
		str=str.replaceAll("&mdash;","");
		return str;
	}
	
	/**
	 * 将异常的printStackTrace转换成String类型
	 * 
	 * @param t 抛出的异常
	 * @return 异常信息
	 */
	public static String stack2String(Throwable t) {
		String result = "";
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			result = sw.toString();
		} catch (Exception e) {
		}
		return result;
	}


	public static String toStr(String str) {
        StringBuffer strBuf = new StringBuffer();
        byte []arry = str.getBytes();
        for (int i = 0; i < arry.length; i++) {
        	char c = (char) (arry[i]);
        	if(i%2==0&&c!=0&&c!=48){
        		strBuf.append(String.valueOf((char) (arry[i] + 48)));
        	}else{
        		strBuf.append(String.valueOf(c));
        	}
        	if(i==2||i==6){
    			strBuf.append("-");
        	}
        	
		}  
        return strBuf.toString();
    } 
	
	// 将字母转换成数字_1  
    public static String toNum(String input) {
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();  
        if (null != input && !"".equals(input)) {  
            for (char c : input.toCharArray()) {  
                if (String.valueOf(c).matches(reg)) {
                    strBuf.append(c - 96);  
                } else {  
                    strBuf.append(c);  
                }  
            }  
            return strBuf.toString().replaceAll("-", "");  
        } else {  
            return input;  
        }  
    }

	/**
	 *	检查一个字符串是否是一个合法的密码（8-20位的数字和字符串组合）
	 *	要求：
	 *		1、必须要要有8-20位(含)
	 *		2、不能全是数字
	 *		3、不能全是字母
	 * @param source
	 * @return
	 */
	public static Boolean validPassword(String source){
		if(isEmpty(source) || "null".equalsIgnoreCase(source) ){
			return false;
		}
		if(source.length() < 8  || source.length() > 20){
			return  false;
		}


		String regEx = "^[A-Za-z]+$";
		if(Pattern.compile(regEx).matcher(source).matches()){
			//纯字母
			return false;
		}
		regEx = "^[0-9]+$";
		if(Pattern.compile(regEx).matcher(source).matches()){
			//纯数字
			return false;
		}
		return true;
	}

	/**
	 * 根据文件的全名称，获取文件的扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileExtName(String fileName){
		if(isEmpty(fileName)){
			return "";
		}
		if(!fileName.contains(".")){
			return "";
		}
		String [] strings = fileName.split("\\.");
		return strings[strings.length-1];
	}


	public static void main(String[] args) {
		System.out.println(validPassword("123456"));
		System.out.println(validPassword("1234561237"));
		System.out.println(validPassword("asdfadadsfads"));
		System.out.println(validPassword("asdfadadsfad1231s"));
		System.out.println(validPassword("asdfadadsfad1231sasdfa3234242"));
		System.out.println(getFileExtName("你好爱好.jpg"));

	}
}
