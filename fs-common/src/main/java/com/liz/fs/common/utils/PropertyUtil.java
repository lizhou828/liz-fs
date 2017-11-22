package com.liz.fs.common.utils;


import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * 属性文件工具类
 * </p>
 * 
 * <p>
 * Copyright: 2015 . All rights reserved.
 * </p>
 * <p>
 * Company: 国裕网络科技有限公司
 * </p>
 * <p>
 * CreateDate:2015-09-15
 * </p>
 * 
 * @author 杨毅修改
 * @version 1.0
 */
public class PropertyUtil {

	/**
	 * 默认属性文件名称
	 */
	public static String FILENAME = "system.properties";
	private static Properties prop;

	static {
		try {
			prop = new Properties();
			InputStream inStream = getResourceAsStream(FILENAME);
			prop.load(inStream);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static Properties getProperties(String fileName) throws Exception {
		try {
			Properties prop = new Properties();
			InputStream inStream = getResourceAsStream(fileName);
			prop.load(inStream);
			return prop;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return prop;
		}

	}

	/**
	 * 获取属性内容
	 * 
	 * @param key
	 *            key值
	 * @return 属性内容
	 */
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

	/**
	 * 读取资源文件内容（根据Hibernate改写）
	 *
	 * @param resource
	 *            资源文件路径
	 * @return 资源文件内容
	 * @throws Exception
	 */
	private static InputStream getResourceAsStream(String resource) throws Exception {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			stream = CommonUtil.class.getResourceAsStream(resource);
		}
		if (stream == null) {
			stream = CommonUtil.class.getClassLoader().getResourceAsStream(stripped);
		}
		if (stream == null) {
			throw new Exception(resource + " not found");
		}
		return stream;
	}
}
