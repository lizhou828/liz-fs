/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.domain.UploadInfo.java <2017年11月15日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.domain;

import java.io.Serializable;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月15日 09时59分
 */
public class UploadInfo implements Serializable {

    private static final long serialVersionUID = -5215021748928812977L;

    /**状态编码*/
    private UploadCode code;

    /**文件类型*/
    private UploadType type;

    /**文件后缀名*/
    private String fileExtension;

    /**文件大小(KB)*/
    private float size;

    /**访问路径:host + rpath*/
    private String url;

    /**访问路径:host + rpath (兼容老版本)*/
    private String path;

    /**当前域名*/
    private String host;

    /**文件相对路径*/
    private String rpath;

    public UploadCode getCode() {
        return code;
    }

    public void setCode(UploadCode code) {
        this.code = code;
    }

    public UploadType getType() {
        return type;
    }

    public void setType(UploadType type) {
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

    public void setUrl(String url) {
        this.url = url;
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
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "UploadInfo{" +
                "code=" + code +
                ", type=" + type +
                ", fileExtension='" + fileExtension + '\'' +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", host='" + host + '\'' +
                ", rpath='" + rpath + '\'' +
                '}';
    }


}
