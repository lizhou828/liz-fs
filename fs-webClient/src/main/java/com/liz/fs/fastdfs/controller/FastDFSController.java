/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.controller.FastDfsController.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.fastdfs.controller;

import com.liz.fs.common.domain.UploadCode;
import com.liz.fs.common.domain.UploadInfo;
import com.liz.fs.common.utils.CommonUtil;
import com.liz.fs.common.utils.FileHelper;
import com.liz.fs.common.utils.PropertyUtil;
import com.liz.fs.fastdfs.common.NameValuePair;
import com.liz.fs.common.utils.StringUtil;
import com.liz.fs.fastdfs.utils.FastDfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * FastDFS 文件上传controller
 * @author lizhou
 * @version 1.0
 */
@Controller
@RequestMapping("/fastdfs")
public class FastDFSController extends BaseFastDFSController {

    @Autowired
    private FastDfsUtil fastDfsUtil;


    @RequestMapping("/testUpload")
    public ModelAndView testUploadPage(){
        return new ModelAndView("/fastdfs/testUpload");
    }


    /**
     * FastDFS 使用连接池上传：
     * 支持多并发、心跳检测
     * @param files
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/upload",method= RequestMethod.POST) //
    @ResponseBody
    public List<UploadInfo> upload(@RequestParam(value = "fsFile") MultipartFile[] files,String groupName,HttpServletRequest request){
        List<UploadInfo> uploadedFileList = new ArrayList<>();
        UploadInfo upload = null;
        String uploadedFileId = null;
        String fileExtName = null;
        if(null == files ||  files.length == 0 ){
            logger.error("There are no files need to be uploaded !");
            upload = new UploadInfo();
            upload.setCode(UploadCode.ILLEGAL_ARGUMENT);
            uploadedFileList.add(upload);
            return uploadedFileList;
        }



        String domainFS = PropertyUtil.getProperty("domain_fs");
        NameValuePair[] metaList = new NameValuePair[1];
        try {
            for(MultipartFile file : files){
                if(null == file) continue;
                metaList[0] = new NameValuePair("fileName", file.getOriginalFilename());
                /* 以 byte 的方式上传 */
                fileExtName = StringUtil.getFileExtName(file.getOriginalFilename());
                uploadedFileId = fastDfsUtil.upload(groupName,file.getBytes(), fileExtName ,metaList);
                if(StringUtil.isNotEmpty(uploadedFileId)){
                    upload = new UploadInfo();
                    upload.setCode(UploadCode.SUCCESS);
                    upload.setHost(domainFS);
                    upload.setRpath(uploadedFileId);
                    upload.setPath(domainFS + "/" + uploadedFileId);//兼容老版本接口
                    upload.setUrl(domainFS + "/" + uploadedFileId);
                    upload.setFileExtension(fileExtName);
                    upload.setType(CommonUtil.getUploadInfoType(fileExtName));
                    upload.setSize(file.getSize());
                    uploadedFileList.add(upload);

                    int width = DEFAULT_SMALL_IMG_WIDTH;
                    int length = DEFAULT_SMALL_IMG_LENGTH;

                    String targetDirectory = request.getRealPath("/") + "page"+ File.separator + "upload";

                    /* 在临时目录生成压缩图 */
                    String targetFilePath = fastDfsUtil.generateSmallPic(file.getBytes(),uploadedFileId,targetDirectory,width,length);
                    logger.info("在临时目录生成压缩图 targetFilePath  = " + targetFilePath);

                    /* 上传压缩图 */
                    String slaveUploadId = fastDfsUtil.uploadSlaveFile(uploadedFileId, targetFilePath, width, length);
                    logger.info("上传压缩图后的slaveUploadId :" + slaveUploadId);

                    /* 删除 本次 在临时目录生成压缩图  */
                    FileHelper.deleteFromDirectory(uploadedFileId, targetDirectory, width, length);
                }
            }
        } catch (Exception ex) {
            logger.error("上传文件过程中发生异常! message : " + ex.getMessage(),ex);
        }
        if(uploadedFileList.isEmpty()){
            upload = new UploadInfo();
            upload.setCode(UploadCode.ERROR);
            uploadedFileList.add(upload);
            return uploadedFileList;
        }
        return uploadedFileList;
    }
}
