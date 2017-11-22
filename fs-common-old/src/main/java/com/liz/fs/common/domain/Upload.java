/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.vo.Upload.java  <2015骞�鏈�5鏃�
 ****************************************************************
 * 鐗堟潈鎵�湁@2015 XKSHOW.CN  淇濈暀鎵�湁鏉冨埄.
 ***************************************************************/
package com.liz.fs.common.domain;

import com.liz.fs.common.utils.FieldRegex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * 上传文件VO
 * @Author hardy
 * @Date 2015-5-25
 * @version 1.0
 */
public class Upload implements Serializable{

	private static final long serialVersionUID = -3286207343525375096L;

	/**状态编码*/
	private UploadCode code = UploadCode.SUCCESS;
	/**文件类型*/
	private Type type;
	/**文件后缀名*/
	private String fileExtension;
	/**文件大小(KB)*/
	private float size;
	/**访问路径:host+rpath*/
	private String url;
	@Deprecated
	private String path;
	/**当前域名*/
	private String host;
	/**文件相对路径*/
	private String rpath;

	/** 状态码  */
	public enum UploadCode {
		/**成功*/SUCCESS,
		/**非法参数*/ILLEGAL_ARGUMENT,
		/**内部错误*/ERROR
	}
	/** 文件类型  */
	public enum Type {
		/**图像*/photo,
		/**视频*/video,
		/**文件*/file,
		/**未知的、不和法的*/unknown
	}

	public UploadCode getCode() {
		return code;
	}
	public void setCode(UploadCode code) {
		this.code = code;
	}

	public Type getType() {
		return type;
	}
	/**
	 * 设置文件类型  by 文件
	 * @Author hardy<2015-5>
	 * @param file
	 */
	public void setType(MultipartFile file) {
		this.setType(FilenameUtils.getExtension(file.getOriginalFilename()));
	}
	/**
	 * 设置文件类型  by 后缀名
	 * @Author hardy<2015-5-29>
	 * @param extension 后缀名
	 */
	public void setType(String extension) {
		fileExtension = extension.toLowerCase();
		if(fileExtension.matches(getRegexFilePhoto())){
			this.type = Type.photo;
		}else if(fileExtension.matches(getRegexFileVideo())){
			this.type = Type.video;
		}else if(fileExtension.matches(getRegexFiles())){
			this.type = Type.file;
		}else{
			this.type = Type.unknown;
		}
	}
	@Deprecated
	public void setType(Type type) {
		this.type = type;
	}

	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}
	public String getPath() {
		return this.path;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public String getRpath() {
		return rpath;
	}
	public void setRpath(String rpath) {
		this.rpath = rpath;
		//兼容历史版本
		this.url = rpath;
		this.path = StringUtils.isEmpty(host) ? rpath : MessageFormat.format("{0}{1}", host, rpath);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 文件类型鉴定正则:图片
	 * @Author hardy<2015年7月29日>
	 * @return
	 */
	public static String getRegexFilePhoto() {
		String regex = StringUtils.isNotEmpty(SystemBean.filetypeAllowedPhoto) ? MessageFormat.format("({0})", SystemBean.filetypeAllowedPhoto) : FieldRegex.REGEX_FILE_PHOTO;
		return regex.replace(")", MessageFormat.format("|{0}", regex.substring(1).toUpperCase()));
	}

	/**
	 * 文件类型鉴定正则:媒体
	 * @Author hardy<2015年7月29日>
	 * @return
	 */
	public static String getRegexFileVideo() {
		String regex = StringUtils.isNotEmpty(SystemBean.filetypeAllowedVideo) ? MessageFormat.format("({0})", SystemBean.filetypeAllowedVideo) : FieldRegex.REGEX_FILE_VIDEO;
		return regex.replace(")", MessageFormat.format("|{0}", regex.substring(1).toUpperCase()));
	}

	/**
	 * 文件类型鉴定正则:文件
	 * @Author hardy<2015年7月29日>
	 * @return
	 */
	public static String getRegexFiles() {
		String regex = StringUtils.isNotEmpty(SystemBean.filetypeAllowedFile) ? MessageFormat.format("({0})", SystemBean.filetypeAllowedFile) : FieldRegex.REGEX_FILE_ALLOWED;
		return regex.replace(")", MessageFormat.format("|{0}", regex.substring(1).toUpperCase()));
	}
		
}
