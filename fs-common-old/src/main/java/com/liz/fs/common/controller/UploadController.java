/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.controller.PhotoUploadController.java  <2015年5月25日>
 ****************************************************************
 * 版权所有@2015 XKSHOW.CN  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.controller;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.liz.fs.common.domain.Upload;
import com.liz.fs.common.domain.Upload.UploadCode;
import com.liz.fs.common.utils.DateUtil;
import com.liz.fs.common.utils.RandomUtil;

/**
 * 文件上传控制器
 * @Author hardy
 * @Date 2015-5
 * @version 1.0
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController{
	/** 域名 */
	private String host;
	/** 磁盘跟路径*/
	private String realPath;
	/** 子目录： 勿以'/'符号开始或结束，如"shop"或"shop/product" */
	private String childDir;

	/**
	 * 初始化
	 * @Author hardy<2015-5>
	 * @param request
	 */
	@InitBinder
	public void init(HttpServletRequest request){
		host = request.getScheme() + "://" + request.getServerName() + (request.getServerPort()!=80?":"+request.getServerPort():"") + request.getContextPath();
		realPath = request.getSession().getServletContext().getRealPath("/");
		// 构建子目录
		childDir = request.getParameter("childDir");
		if(StringUtils.isNotEmpty(childDir)){
			if(childDir.indexOf("/")==0)
				childDir = childDir.substring(1);
			if(StringUtils.isNotEmpty(childDir) && childDir.lastIndexOf("/")==(childDir.length()-1))
				childDir = childDir.substring(0, childDir.length()-1);
		}else{
			childDir = "default";
		}
		childDir += "/"+DateUtil.toStringMonth(new Date());
	}

	/**
	 * 文件上传
	 * @Author hardy<2015-5>
	 * @param request
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public List<Upload> simple(@RequestParam(required=true) MultipartFile[] files,
			HttpServletRequest request, ModelMap mmap){
		List<Upload> lst = new ArrayList<Upload>();
		Upload uploadVo = null;
		String dir, filename;
    	for(MultipartFile file : files){
            if(null!=file && !file.isEmpty()){
            	uploadVo = new Upload();
            	try {
            		uploadVo.setSize(file.getSize()/1024);

            		// 文件类型>鉴定符合性
            		if(FilenameUtils.getExtension(file.getOriginalFilename()).matches("(tmp)")){
            			uploadVo.setType(request.getParameter("fileExtension"));
            		}else{
            			uploadVo.setType(file);
            		}
                	if(uploadVo.getType().equals(Upload.Type.unknown))
                		throw new IllegalArgumentException();

                	// 构建目录[文件]>上传>访问路径
                	dir = uploadVo.getType().name() + (StringUtils.isNotEmpty(childDir) ? "/"+childDir : "");
                	filename = MessageFormat.format("{0}.{1}", RandomUtil.getSn(), uploadVo.getFileExtension());
                	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath +"/"+ dir, filename));
                	uploadVo.setHost(host);
                	uploadVo.setRpath(MessageFormat.format("/{0}/{1}", dir, filename));
				} catch (IllegalArgumentException ill) {
					ill.printStackTrace();
					uploadVo.setCode(UploadCode.ILLEGAL_ARGUMENT);
				} catch (IOException io) {
					io.printStackTrace();
					uploadVo.setCode(UploadCode.ERROR);
				} finally {
					lst.add(uploadVo);
				}
            }
        }
    	return lst;
	}

}
