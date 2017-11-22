/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.common.SpringUtils.java  <2015年5月30日>
 ****************************************************************
 * 版权所有@2015 XKSHOW.CN  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author hardy
 * @Date 2015年5月30日 下午2:03:40
 * @version 1.0
 */
@Component()
@Lazy(false)
public final class SpringUtils implements ApplicationContextAware, DisposableBean {

	/** applicationContext */
	private static ApplicationContext applicationContext;

	/**
	 * 不可实例化
	 */
	private SpringUtils() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	/**
	 * @param type
	 *            Bean类型
	 * @return
	 * @Description: 获取实例
	 */
	public static <T> T getBean(Class<T> type) {
		Assert.notNull(type);
		return applicationContext.getBean(type);
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}

	/**
	 * 获取HttpSession对象
	 * @return
	 * @Author hardy
	 * 2014年11月7日 下午2:43:12
	 */
	public static HttpSession getSession() {
		try {
			return getRequest().getSession();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取HttpServletRequest对象
	 * @return
	 * @Author hardy
	 * 2014年11月7日 下午2:43:16
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

}
