/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.common.SystemService.java  <2015年7月28日>
 ****************************************************************
 * 版权所有@2015 XKSHOW.CN  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.helper;

import java.util.ArrayList;
import java.util.List;

import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.liz.fs.common.domain.SystemBean;

/**
 * @Author hardy
 * @Date 2015年7月28日 下午6:55:27
 * @version 1.0
 */
@Component("systemComponent")
public class SystemService extends LoggerBase {
	@Value("${fs.openFolders}")String[] openFolders;
	@Value("${fs.isRedirectOriginalImage}")boolean isRedirectOriginalImage;

	@Value("${fs.fileType.allowed.photo}")String filetypeAllowedPhoto;
	@Value("${fs.fileType.allowed.video}")String filetypeAllowedVideo;
	@Value("${fs.fileType.allowed.file}")String filetypeAllowedFile;

	@Value("${fs.watermark.open}")boolean watermarkOpen;
	@Value("${fs.watermark.positions}")Integer watermarkPositions;
	@Value("${fs.watermark.notLessSizes}")String[] watermarkNotLessSizes;  //300x300,100x100
	@Value("${fs.watermark.relativePaths}")String[] watermarkRelativePaths; //注：watermarkRelativePaths.length=watermarkNotLessSizes.length
	@Value("${domain_fs}") String domainFS;

	public void init(){
		try {
			SystemBean.isRedirectOriginalImage = isRedirectOriginalImage;
			/*允许上传文件类型*/
			SystemBean.filetypeAllowedPhoto = filetypeAllowedPhoto;
			SystemBean.filetypeAllowedVideo = filetypeAllowedVideo;
			SystemBean.filetypeAllowedFile = filetypeAllowedFile;

			initWatermark();
		} catch (NumberFormatException e) {
			logger.error("配置文件初始化失败：数字转换异常...");
			e.printStackTrace();
		}  catch (IllegalArgumentException e) {
			logger.error("配置文件初始化失败：非法参数...");
			e.printStackTrace();
		}
	}
	/** 水印图属性 */
	private void initWatermark(){
		SystemBean.watermarkOpen = watermarkOpen;
		if(!SystemBean.watermarkOpen)
			return;
		if(null == watermarkPositions){
			throw new IllegalArgumentException();
		}else if(watermarkNotLessSizes.length<1){
			throw new IllegalArgumentException();
		}else if(null==watermarkRelativePaths || watermarkRelativePaths.length<1){
			throw new IllegalArgumentException();
		}else if(watermarkRelativePaths.length != watermarkNotLessSizes.length){ //“水印图路径数”必须等于“水印图规格数”
			throw new IllegalArgumentException();
		}

		// 水印图位置
		switch (watermarkPositions) {
			case 10: SystemBean.watermarkPositions = Positions.TOP_LEFT;break;
			case 11: SystemBean.watermarkPositions = Positions.TOP_CENTER;break;
			case 12: SystemBean.watermarkPositions = Positions.TOP_RIGHT;break;

			case 20: SystemBean.watermarkPositions = Positions.CENTER_LEFT;break;
			case 21: SystemBean.watermarkPositions = Positions.CENTER;break;
			case 22: SystemBean.watermarkPositions = Positions.CENTER_RIGHT;break;

			case 30: SystemBean.watermarkPositions = Positions.BOTTOM_LEFT;break;
			case 31: SystemBean.watermarkPositions = Positions.BOTTOM_CENTER;break;
			case 32: SystemBean.watermarkPositions = Positions.BOTTOM_RIGHT;break;

			default: SystemBean.watermarkPositions = Positions.CENTER;break;
		}

		SystemBean.openFolders = openFolders;
		List<Integer[]> lstWatermarkNotLessSizes = new ArrayList<Integer[]>();
		for(String wsize: watermarkNotLessSizes){
			if(StringUtils.isEmpty(wsize))
				continue;
			lstWatermarkNotLessSizes.add(new Integer[]{Integer.parseInt(wsize.split("x")[0]), Integer.parseInt(wsize.split("x")[1])});
		}
		SystemBean.watermarkNotLessSizes = lstWatermarkNotLessSizes.isEmpty() ? null : lstWatermarkNotLessSizes;
		SystemBean.watermarkRelativePaths = watermarkRelativePaths;
	}

	public String getDomainFS() {
		return domainFS;
	}

	public void setDomainFS(String domainFS) {
		this.domainFS = domainFS;
	}
}
