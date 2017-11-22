/*
 *Project: jhp-fs
 *File: cn.xkshow.fs.filter.PhotoFilter.java  <2015年5月25日>
 ****************************************************************
 * 版权所有@2015 XKSHOW.CN  保留所有权利.
 ***************************************************************/
package com.liz.fs.common.filter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.log4j.Logger;

import com.liz.fs.common.domain.SystemBean;
import com.liz.fs.common.domain.Upload;
import com.liz.fs.common.utils.FileHelper;
import com.liz.fs.common.utils.RegexUtil;

/**
 * 图像呈现过滤器
 * Servlet Filter implementation class PhotoFilter
 * @Author hardy
 * @Date 2015-5-26
 * @Version 1.0
 */
public class PhotoFilter implements Filter {
	final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Default constructor.
     */
    public PhotoFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
		try {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			logger.debug("-------URL:"+ httpRequest.getRequestURL());
			String uri = httpRequest.getRequestURI();
			logger.debug("-------URI:"+ uri);

			// 开放目录
			boolean ignoreCurrentFolder = false;
			for(String openFolder: SystemBean.openFolders){
				if(uri.startsWith(MessageFormat.format("/{0}/{1}", Upload.Type.photo.name(), openFolder))){
					ignoreCurrentFolder = true;
					break;
				}
			}
			// 非开放目录，需参与条件预处理
			if(!ignoreCurrentFolder){
				/* 缩略图预处理  */
				if(this.doPrefix(uri, httpRequest, httpRequest, response)){//前缀
					return;
				}else if(this.doSuffix(uri, httpRequest, httpRequest, response)){//后缀
					return;
				}else if(SystemBean.isRedirectOriginalImage){//原图访问重定向到缩略图
					this.doOriginal(uri, httpRequest, httpRequest, response);
					return;
				}
			}
			chain.doFilter(request, response);
		} catch (IOException io) {
			io.printStackTrace();
		} catch (ServletException se) {
			se.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 处理(前缀)缩略图"123456_100x100.jpg"
	 * @Author hardy<2015年5月29日>
	 * @param uri
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean doPrefix(String uri, HttpServletRequest httpRequest,
			ServletRequest request, ServletResponse response) throws NumberFormatException, IOException, ServletException{
		if(uri.matches("(.+([\\w]+_\\d+x\\d+)\\."+ Upload.getRegexFilePhoto() +")")){
			this.getThumb(uri, uri, httpRequest, request, response);
			return true;
		}
		return false;
	}

	/**
	 * 处理(后缀)缩略图"123456.jpg_100x100"
	 * @Author hardy<2015年5月29日>
	 * @param uri
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws ServletException
	 */
	private boolean doSuffix(String uri, HttpServletRequest httpRequest,
			ServletRequest request, ServletResponse response) throws NumberFormatException, IOException, ServletException{
		if(uri.matches("(.+[\\w]+\\."+Upload.getRegexFilePhoto()+"_(\\d+x\\d+))")){
			// 将其后缀(*.jpg_100x100)转成前缀(*_100x100.jpg)
			String pathThumb = uri.replaceAll("(\\."+Upload.getRegexFilePhoto()+")", "")+RegexUtil.findMatchContent("(\\."+Upload.getRegexFilePhoto()+")", uri);
			this.getThumb(uri, pathThumb, httpRequest, request, response);
			return true;
		}
		return false;
	}

	/**
	 * 处理(过滤)原图
	 * @author KangLG<2017年1月5日>
	 * @param uri
	 * @param httpRequest
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean doOriginal(String uri, HttpServletRequest httpRequest,
			ServletRequest request, ServletResponse response) throws NumberFormatException, IOException, ServletException{
		BufferedImage bufferedImage = ImageIO.read(new File(httpRequest.getSession().getServletContext().getRealPath("/")+uri));
		return this.doSuffix(String.format("%s_%dx%d", uri, bufferedImage.getWidth(), bufferedImage.getHeight()), httpRequest, request, response);
	}

	/**
	 * 获取缩略图
	 * @Author hardy<2015年5月29日>
	 * @param uri
	 * @param pathThumb
	 * @param httpRequest
	 * @param request
	 * @param response
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getThumb(String uri, String pathThumb, HttpServletRequest httpRequest,
		ServletRequest request, ServletResponse response) throws NumberFormatException, IOException, ServletException{
		pathThumb = pathThumb.replace(Upload.Type.photo.name(), "photothumb");
		logger.debug(MessageFormat.format("-------matches:{0} =>> {1}", uri, pathThumb));

		String realPath = httpRequest.getSession().getServletContext().getRealPath("/");
		String pathOrigin = uri.replaceFirst("(_\\d+x\\d+)", "");

		// 缩略图不存在，创建目录>缩略图文
		if(!FileHelper.isExist(realPath+pathThumb)
				&& FileHelper.createDir(realPath + pathThumb.substring(0, pathThumb.lastIndexOf("/")))){
			// 图片裁剪尺寸
			String[] size = RegexUtil.findMatchContent("(\\d+x\\d+)", uri).split("x");
			int thumbWidth=Integer.valueOf(size[0]), thumbHeight=Integer.valueOf(size[1]);
			if(thumbWidth<=0 || thumbHeight<=0)
				return;
			// 水印图处理
			doWatermark(realPath, pathOrigin, pathThumb, thumbWidth, thumbHeight);
		}
		httpRequest.getRequestDispatcher(pathThumb).forward(request, response);
	}
	private void doWatermark(String realPath, String pathOrigin, String pathThumb, int thumbWidth, int thumbHeight) throws IOException{
		// 开启水印
		if(SystemBean.watermarkOpen){
			// 水印规格
			for(int i=0; i<SystemBean.watermarkNotLessSizes.size(); i++){
				Integer[] argSpec = SystemBean.watermarkNotLessSizes.get(i);
				if(thumbWidth>=argSpec[0] && thumbHeight>=argSpec[1]){
					Thumbnails.of(realPath + pathOrigin)
						  	.size(thumbWidth, thumbHeight)
						  	.watermark(SystemBean.watermarkPositions, ImageIO.read(new File(realPath + SystemBean.watermarkRelativePaths[i])), 0.5f)
						  	.toFile(realPath + pathThumb);
					return;
				}
			}
		}
		Thumbnails.of(realPath + pathOrigin)
			  	.size(thumbWidth, thumbHeight)
			  	.toFile(realPath + pathThumb);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
