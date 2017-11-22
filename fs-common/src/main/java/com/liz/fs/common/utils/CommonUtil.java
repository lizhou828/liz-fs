/*
 *Project: gyucenter
 *File: com.glorypty.ucenter.util.CommonUtil.java <2016年9月19日>
 ****************************************************************
 * 版权所有@2016 国裕网络科技  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.utils;

import com.liz.fs.common.domain.UploadType;

/**
 * <p>
 * 通用工具类
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
public class CommonUtil {

	/**
	 * 设置文件类型  by 后缀名
	 * @Author hardy<2015-5-29>
	 * @param fileExtensionName 后缀名
	 */
	public static UploadType getUploadInfoType(String fileExtensionName) {
		fileExtensionName = fileExtensionName.toLowerCase();
		if(FieldRegex.REGEX_FILE_PHOTO.contains(fileExtensionName)){
			return UploadType.photo;
		}else if(FieldRegex.REGEX_FILE_VIDEO.contains(fileExtensionName)){
			return  UploadType.video;
		}else if(FieldRegex.REGEX_FILE_ALLOWED.contains(fileExtensionName)){
			return UploadType.file;
		}
		return UploadType.unknown;
	}

	public static void main(String[] args) {
		System.out.println(getUploadInfoType("png"));
		System.out.println(getUploadInfoType("jpg"));
		System.out.println(getUploadInfoType("avi"));
		System.out.println(getUploadInfoType("doc"));
		System.out.println(getUploadInfoType("mpeg2"));
	}

}

