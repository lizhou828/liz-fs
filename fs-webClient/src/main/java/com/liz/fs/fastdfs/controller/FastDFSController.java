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
import com.liz.fs.common.utils.PropertyUtil;
import com.liz.fs.fastdfs.common.NameValuePair;
import com.liz.fs.common.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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


    @RequestMapping("/testUpload")
    public ModelAndView testUploadPage(){
        return new ModelAndView("/fastdfs/testUpload");
    }

    /**
     *
     * 简单的文件上传(支持表单提交的方式、ajax的方式)
     *  e.g. : <input type="file" name="fsFile" />  name 属性必传
     * @param files 待上传的文件
     * @param groupName 组名可以为空(除非是已经知道 该组名在FastDFS服务器中 确实存在，否则不要传该参数)
     * @return  上传后的文件信息
     */
    @RequestMapping(value = "/upload",method= RequestMethod.POST) //
    @ResponseBody
    public List<UploadInfo> simple(@RequestParam(value = "fsFile") MultipartFile[] files,String groupName ,HttpServletRequest request){
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

        if(null == super.storageClient1 || null == super.trackerServer ){
            logger.error("FastDFS has initialized failed!");
            upload = new UploadInfo();
            upload.setCode(UploadCode.ERROR);
            uploadedFileList.add(upload);
            return uploadedFileList;
        }

        NameValuePair[] metaList = new NameValuePair[1];
        String domainFS = PropertyUtil.getProperty("domain_fs");
        try {
            for(MultipartFile file : files){
                if(null == file) continue;
                /* 以 byte 的方式上传（另外还以以file_name的方式上传） */
                metaList[0] = new NameValuePair("fileName", file.getOriginalFilename());

                fileExtName = StringUtil.getFileExtName(file.getOriginalFilename());
                uploadedFileId = super.storageClient1.upload_file1(groupName,file.getBytes(), fileExtName , metaList);
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


                    int width = 150;
                    int length = 150;

                    String targetDirectory = request.getRealPath("/") + "page"+ File.separator + "upload";

                    /* 在临时目录生成压缩图 */
                    String targetFilePath = FastDFSUtils.generateSmallPic(domainFS,uploadedFileId,targetDirectory,width,length);
                    System.out.println("在临时目录生成压缩图 targetFilePath  = " + targetFilePath );

                    /* 上传压缩图 */
                    String slaveUploadId = super.uploadSlaveFile(uploadedFileId,targetFilePath,width,length);
                    System.out.println("上传压缩图后的slaveUploadId :" + slaveUploadId );
                }
            }
        } catch (Exception ex) {
            logger.error("上传文件过程中发生异常! message : " + ex.getMessage(),ex);
        }finally {
            if(null != super.trackerServer){
                try {
                    super.trackerServer.close();
                } catch (IOException e) {
                    logger.error("关闭FastDFS跟踪服务器时发生异常：" + e.getMessage(),e);
                }
            }
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
