/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.fastdfs.controller.BaseFastDFSController.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.fastdfs.controller;

import com.liz.fs.common.controller.BaseController;
import com.liz.fs.fastdfs.client.*;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 15时16分
 */
@Controller
public class BaseFastDFSController extends BaseController {

    protected StorageClient1 storageClient1;
    protected TrackerServer trackerServer;

    public BaseFastDFSController() {

        /* 初始化 */
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();
            storageClient1 = new StorageClient1(trackerServer, null);
        } catch (Exception e) {
            logger.error("FastDFS has initialized failed! Please check config file！");
        }
    }
    public String uploadSlaveFile (String masterFileId,String targetFilePath,int width,int length){
        String prefixName = "_" + width + "x" + length;
        return uploadSlaveFile(masterFileId, prefixName , targetFilePath);
    }

    private String uploadSlaveFile(String masterFileId, String prefixName, String slaveFilePath) {
        String slaveFileId = "";
        String slaveFileExtName = "";
        if (slaveFilePath.contains(".")) {
            slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1);
        } else {
            logger.warn("Fail to upload file, because the format of filename is illegal.");
            return slaveFileId;
        }
        //上传文件
        try {
            slaveFileId = storageClient1.upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null);
        } catch (Exception e) {
            logger.warn("Upload file \"" + slaveFilePath + "\"fails");
        }finally{
            if(null != trackerServer){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("trackerServer.close() occurs exception:" + e.getMessage(),e);
                }
            }
        }

        return slaveFileId;
    }


}
