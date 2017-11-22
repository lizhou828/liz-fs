/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.fastdfs.exception.AppException.java <2017年11月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.fastdfs.exception;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月22日 14时30分
 */
public class AppException extends Exception{
    private static final long serialVersionUID = -1848618491499044704L;

    private String code;
    private String description;


    public AppException( String code, String message) {
        super(message);
        this.code = code;
    }

    public AppException( String code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }


    /**
     * 错误码
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 用户可读描述信息
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(": ");
        sb.append("");
        sb.append(code);
        sb.append(" - ");
        sb.append(getMessage());
        if (getDescription() != null) {
            sb.append(" - ");
            sb.append(getDescription());
        }
        return sb.toString();
    }
}
