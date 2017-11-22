/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.fastdfs.utils.FastDfsUtil.java <2017年11月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.fastdfs.utils;

import com.liz.fs.common.utils.FileHelper;
import com.liz.fs.fastdfs.client.StorageClient;
import com.liz.fs.fastdfs.client.StorageClient1;
import com.liz.fs.fastdfs.client.StorageServer;
import com.liz.fs.fastdfs.client.TrackerServer;
import com.liz.fs.fastdfs.common.NameValuePair;
import com.liz.fs.fastdfs.enums.ERRORS;
import com.liz.fs.fastdfs.exception.AppException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.UUID;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月22日 14时24分
 */
public class FastDfsUtil {
    final Logger logger = Logger.getLogger(this.getClass());
    /** 连接池 */
    private ConnectionPool connectionPool = null;
    /** 连接池默认最小连接数 */
    private long minPoolSize = 10;
    /** 连接池默认最大连接数 */
    private long maxPoolSize = 30;
    /** 当前创建的连接数 */
    private volatile long nowPoolSize = 0;
    /** 默认等待时间（单位：秒） */
    private long waitTimes = 30;

    /**
     * 初始化线程池
     *
     * @Description:
     *
     */
    public void init() {
        String logId = UUID.randomUUID().toString();
        logger.info("[初始化线程池(Init)][" + logId + "][默认参数：minPoolSize=" + minPoolSize + ",maxPoolSize=" + maxPoolSize + ",waitTimes=" + waitTimes + "]");
        connectionPool = new ConnectionPool(minPoolSize, maxPoolSize, waitTimes);
    }

    /**
     *
     * @Description: 文件上传
     * @param groupName
     *            组名如group0
     * @param fileBytes
     *            文件字节数组
     * @param extName
     *            文件扩展名：如png
     * @return 图片上传成功后地址
     * @throws Exception
     *
     */
    public String upload(String groupName, byte[] fileBytes, String extName,NameValuePair[] metaList) throws AppException {
        String logId = UUID.randomUUID().toString();
        /** 封装文件信息参数 */
        TrackerServer trackerServer = null;
        try {

            /** 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            /** 以文件字节的方式上传 */
            String result = storageClient1.upload_file1(groupName, fileBytes, extName, metaList);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            logger.info("[上传文件（upload）-fastdfs服务器相应结果][" + logId + "][result=" + result + "]");

            /** results[0]:组名，results[1]:远程文件名 */
            if ( null !=  result && !"".equals(result.trim()) && !"null".equalsIgnoreCase(result) ) {
                return  result;
            } else {
                /** 文件系统上传返回结果错误 */
                throw ERRORS.UPLOAD_RESULT_ERROR.ERROR();
            }
        } catch (AppException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw e;
        } catch (SocketTimeoutException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();
        }

    }

    /**
     * 根据主文件id ,上传压缩后的主文件，得到从文件id
     * @param masterFileId
     * @param prefixName
     * @param slaveFilePath
     * @param slaveFileExtName
     * @param meta_list
     * @return
     * @throws AppException
     */
    public String upload_file1(String masterFileId, String prefixName, String slaveFilePath, String slaveFileExtName, NameValuePair[] meta_list)throws AppException{
        String logId = UUID.randomUUID().toString();
        /** 封装文件信息参数 */
        TrackerServer trackerServer = null;
        try {
            /** 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            String slaveFileId = storageClient1.upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            if ( null !=  slaveFileId && !"".equals(slaveFileId.trim()) && !"null".equalsIgnoreCase(slaveFileId) ) {
                return  slaveFileId;
            } else {
                /** 文件系统上传返回结果错误 */
                throw ERRORS.UPLOAD_RESULT_ERROR.ERROR();
            }
        } catch (AppException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw e;
        } catch (SocketTimeoutException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();
        }
    }


    /**
     * 根据主文件id,上传指定压缩尺寸的图片
     * @param masterFileId
     * @param targetFilePath
     * @param width
     * @param length
     * @return
     */
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
            slaveFileId = upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null);
        } catch (Exception e) {
            logger.warn("Upload file \"" + slaveFilePath + "\"fails");
        }

        return slaveFileId;
    }

    /**
     * 下载文件到指定 目录
     * @param groupName
     * @param fileName
     * @throws AppException
     */
    public void downloadFile(String groupName, String fileName,String targetDirectory) throws AppException{
        String logId = UUID.randomUUID().toString();
        /** 封装文件信息参数 */
        TrackerServer trackerServer = null;
        try {
            /** 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            byte [] bytes = storageClient.download_file(groupName,fileName);

            /** 及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

        } catch (AppException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw e;
        } catch (SocketTimeoutException e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {
            logger.error("[上传文件（upload)][" + logId + "][异常：" + e + "]",e);
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();
        }
    }

        /**
         *
         * @Description: 删除fastdfs服务器中文件
         * @param group_name
         *            组名
         * @param remote_filename
         *            远程文件名称
         * @throws AppException
         *
         */
    public void deleteFile(String group_name, String remote_filename)
            throws AppException {

        String logId = UUID.randomUUID().toString();
        logger.info("[ 删除文件（deleteFile）][" + logId + "][parms：group_name=" + group_name + ",remote_filename=" + remote_filename + "]");
        TrackerServer trackerServer = null;

        try {
            /** 获取可用的tracker,并创建存储server */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer,
                    storageServer);
            /** 删除文件,并释放 trackerServer */
            int result = client1.delete_file(group_name, remote_filename);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            logger.info("[ 删除文件（deleteFile）--调用fastdfs客户端返回结果][" + logId + "][results：result=" + result + "]");

            /** 0:文件删除成功，2：文件不存在 ，其它：文件删除出错 */
            if (result == 2) {
                throw ERRORS.NOT_EXIST_FILE.ERROR();
            } else if (result != 0) {
                throw ERRORS.DELETE_RESULT_ERROR.ERROR();
            }
        } catch (AppException e) {
            logger.error("[ 删除文件（deleteFile）][" + logId + "][异常：" + e + "]",e);
            throw e;
        } catch (SocketTimeoutException e) {
            logger.error("[ 删除文件（deleteFile）][" + logId + "][异常：" + e + "]",e);
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {
            logger.error("[ 删除文件（deleteFile）][" + logId + "][异常：" + e + "]",e);
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();
        }
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public long getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(long minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public long getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(long maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getNowPoolSize() {
        return nowPoolSize;
    }

    public void setNowPoolSize(long nowPoolSize) {
        this.nowPoolSize = nowPoolSize;
    }

    public long getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(long waitTimes) {
        this.waitTimes = waitTimes;
    }

    /**
     * 根据主文件id ,以及需要压缩的尺寸，在项目的目录下压缩成指定尺寸的图片
     * @param fileByte 源文件的字节数组
     * @param mainUploadFileId 主图id
     * @param uploadPath        上传的目录
     * @param width              压缩的宽度
     * @param length            压缩的高度
     * @return 已经生成的文件名
     */
    public String generateSmallPic(byte[] fileByte,String mainUploadFileId,String uploadPath, int width, int length){
        String targetUrl = null;
        try {
            if(!mainUploadFileId.contains(".")){
                return targetUrl;
            }
            String fileName = mainUploadFileId.substring(mainUploadFileId.lastIndexOf("/"), mainUploadFileId.length());
            if(!fileName.contains(".")){
                return targetUrl;
            }
            String targetFileName  = fileName;
            File file = FileHelper.byteToFile(fileByte, uploadPath + File.separator + targetFileName);

            String [] parts = fileName.split("\\.");
            if(parts.length != 2){
                return targetUrl;
            }

            String targetSmallFileName  = parts[0] + "_" + width + "x" + length + "." +  parts[1];
            targetUrl = uploadPath + File.separator + targetSmallFileName ;


            //压缩原图 并生成 压缩图
            Thumbnails.of(file)
                    .size(width,length)
                    .toFile(targetUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        return targetUrl;
    }
}
