package com.liz.fs.fastdfs.controller;

import com.liz.fs.common.utils.FileHelper;
import com.liz.fs.fastdfs.client.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

/**
 * Created by lizhou on 2017年11月19日 22时32分
 */


public class FastDFSUtils {
    protected final static Logger logger = Logger.getLogger(FastDFSUtils.class);
    protected static StorageClient1 storageClient1;
    protected static TrackerServer trackerServer;

    static {
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

    public static void main(String[] args) {
//        try {
//            String masterFileId = uploadFile("C:\\Users\\Administrator\\Desktop\\hashiqi.jpg");
//            System.out.println(masterFileId);
//            download(masterFileId, "C:\\Users\\Administrator\\Desktop\\master.png");
//
//            String slaveFileId = uploadSlaveFile(masterFileId, "_244x244", "C:\\Users\\Administrator\\Desktop\\hashiqi_244x244.jpg");
//            System.out.println(slaveFileId);
//
//            download(slaveFileId, "C:\\Users\\Administrator\\Desktop\\slave.png");
//        } catch (Exception e) {
//            logger.error("upload file to FastDFS failed.", e);
//        }


//        try {
//            /* 上传原图 */
//            String orgPicUrl = "C:\\Users\\Administrator\\Desktop\\hashiqi.jpg";
//            String masterFileId = uploadFile(orgPicUrl);
//            System.out.println(masterFileId);
//
//
//            int length = 150;
//            int width  = 150;
//            String targetUrl = "C:\\Users\\Administrator\\Desktop\\hashiqi_" + width + "x" + length + ".jpg";
////            压缩原图并生成
//            Thumbnails.of(orgPicUrl)
//                    .size(width,length)
//                    .toFile(targetUrl);
//
//            //上传压缩图
//            String slaveFileId = uploadSlaveFile(masterFileId, "_" + width +  "x" + length, targetUrl);
//            System.out.println(slaveFileId);
//        } catch (Exception e) {
//            logger.error("upload file to FastDFS failed.", e);
//        }
//


        generateSmallPic("http://192.168.202.129","/group1/M00/00/00/wKjKgloS3u-AdYdvAABWRV3JmjY199.jpg","C:\\Users\\Administrator\\Desktop",150,150);
    }


    /**
     * 1、先把byte已file
     * @param bytes
     */
    public static void generateSmallPic(byte[] bytes){
        if(null == bytes || bytes.length == 0){
            return;
        }
        int length = 150;
        int width  = 150;
        String targetUrl = "C:\\Users\\Administrator\\Desktop\\hashiqi_" + width + "x" + length + ".jpg";

        try{
            String uploadTempPath = "C:\\Users\\Administrator\\Desktop\\hashiqi.jpg";
            File file = FileHelper.byteToFile(bytes,uploadTempPath);
            //压缩原图并生成
            Thumbnails.of(file)
                    .size(width,length)
                    .toFile(targetUrl);
        }catch (Exception e){

        }
    }


    public static String generateSmallPic(String fileHost,String mainUploadFileId,String uploadPath, int width, int length){
        String targetUrl = null;
        try {
            URL url = new URL(fileHost + "/" +  mainUploadFileId);

            if(!mainUploadFileId.contains(".")){
                return targetUrl;
            }
            String fileName = mainUploadFileId.substring(mainUploadFileId.lastIndexOf("/"),mainUploadFileId.length());
            if(!fileName.contains(".")){
                return targetUrl;
            }
            String [] parts = fileName.split("\\.");
            if(parts.length != 2){
                return targetUrl;
            }
            String targetFileName  = parts[0] + "_" + width + "x" + length + "." +  parts[1];
            targetUrl = uploadPath + File.separator + targetFileName;

//            上传图片后有延迟,需要停顿一会
            Thread.sleep(500);

            //压缩原图并生成
            Thumbnails.of(url)
                    .size(width,length)
                    .toFile(targetUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        return targetUrl;
    }





    public static String uploadFile(String filePath) throws Exception{
        String fileId = "";
        String fileExtName = "";
        if (filePath.contains(".")) {
            fileExtName = filePath.substring(filePath.lastIndexOf(".") + 1);
        } else {
            logger.warn("Fail to upload file, because the format of filename is illegal.");
            return fileId;
        }

        //建立连接
    /*.......*/

        //上传文件
        try {
            fileId = storageClient1.upload_file1(filePath, fileExtName, null);
        } catch (Exception e) {
            logger.warn("Upload file \"" + filePath + "\"fails");
        }finally{
            trackerServer.close();
        }
        return fileId;
    }

    public static String uploadSlaveFile(String masterFileId, String prefixName, String slaveFilePath) throws Exception{
        String slaveFileId = "";
        String slaveFileExtName = "";
        if (slaveFilePath.contains(".")) {
            slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1);
        } else {
            logger.warn("Fail to upload file, because the format of filename is illegal.");
            return slaveFileId;
        }

        //建立连接
    /*.......*/

        //上传文件
        try {
            slaveFileId = storageClient1.upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null);
        } catch (Exception e) {
            logger.warn("Upload file \"" + slaveFilePath + "\"fails");
        }finally{
            trackerServer.close();
        }

        return slaveFileId;
    }

    public static int download(String fileId, String localFile) throws Exception{
        int result = 0;
        //建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);

        //上传文件
        try {
            result = client.download_file1(fileId, localFile);
        } catch (Exception e) {
            logger.warn("Download file \"" + localFile + "\"fails");
        }finally{
            trackerServer.close();
        }
        System.out.println();
        return result;
    }
}
