/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.domain.SystemBean.java  <2015年7月29日>
 ****************************************************************
 * 版权所有@2015 XKSHOW.CN  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.domain;

import java.util.List;

import net.coobird.thumbnailator.geometry.Positions;

/**
 * @Author hardy
 * @Date 2015年7月29日 上午10:59:21
 * @version 1.0
 */
public class SystemBean {

	/** 是否重定向原图(为缩略图) */
	public static boolean isRedirectOriginalImage = false;
	/** 开放目录(即直接返回数据，不做任何预处理) */
	public static String[] openFolders;

	/*
	 * 允许上传文件类型
	 */
	/**允许上传类型:图片*/
	public static String filetypeAllowedPhoto;
	/**允许上传类型:媒体*/
	public static String filetypeAllowedVideo;
	/**允许上传类型:文档*/
	public static String filetypeAllowedFile;

	/*
	 * 水印图属性
	 */
	/**是否开启水印图*/
	public static boolean watermarkOpen = false;
	/**水印图位置*/
	public static Positions watermarkPositions;
	/**加水印图尺寸*/
	public static List<Integer[]> watermarkNotLessSizes;
	/**水印图路径 */
	public static String[] watermarkRelativePaths;

}
