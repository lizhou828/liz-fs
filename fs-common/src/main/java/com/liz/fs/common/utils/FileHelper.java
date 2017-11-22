/*
 *Project: glorypty-fs
 *File: com.glorypty.fs.common.utils.FileHelper.java <2017年11月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.fs.common.utils;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年11月13日 12时27分
 */
public class FileHelper {

    static Logger logger = Logger.getLogger(FileHelper.class);

    public static boolean createDir(String path) {
        try {
            File e = new File(path);
            return !e.exists() && !e.isDirectory()?e.mkdirs():true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean isExist(String path) {
        return (new File(path)).exists();
    }

    /**
     * 文件转字节数组
     * @param file 文件
     * @return 字节数组
     */
    public static byte[] fileToByte( File file ) {
        if(null == file || file.length() == 0){
            return null;
        }
        byte[] byteArray = null;
        try{
            InputStream input = new FileInputStream(file);
            byteArray = new byte[input.available()];
            input.read(byteArray);
        }catch (Exception e){
            System.out.println("文件转字节发生异常！" + e.getMessage());
        }
        return byteArray;
    }


    /**
     * 字节数组转文件
     * @param byteArray 字节数组
     * @param filePath 目标文件路径
     * @return 文件
     */
    public static File byteToFile( byte[] byteArray , String filePath ){
        File file = null;
        if(null == byteArray || byteArray.length == 0){
            return file;
        }
        try{
            file = new File(filePath);
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(byteArray);
        }catch (Exception e){
            System.out.println("字节转文件发生异常！" + e.getMessage());
        }
        return file;
    }

    /**
     * 从指定目录中删除文件，并删除指定尺寸的图片
     * @param uploadedFileId
     * @param targetDirectory
     * @param width
     * @param length
     */
    public static void deleteFromDirectory(String uploadedFileId, String targetDirectory, int width, int length) {
        if(!uploadedFileId.contains(".")){
            return ;
        }
        String fileName = uploadedFileId.substring(uploadedFileId.lastIndexOf("/"), uploadedFileId.length());
        if(!fileName.contains(".")){
            return ;
        }
        File file = new File(targetDirectory + File.separator + fileName);
        if(file.exists()){
            boolean result = file.delete();
            logger.info("文件 ：" + targetDirectory + File.separator + fileName +  " 删除" + ( result ? "成功" : "失败") + "!");
        }else{
            logger.error("文件 ：" + targetDirectory + File.separator + fileName +  " 不存在!");
        }

        String[] parts = fileName.split("\\.");

        String smallFileName = parts[0] + "_" + width + "x" + length + "." + parts[1];
        file = new File(targetDirectory + File.separator + smallFileName);
        if(file.exists()){
            boolean result = file.delete();
            logger.info("文件 ：" + targetDirectory + File.separator + smallFileName + " 删除" + (result ? "成功" : "失败") + "!");
        }else{
            logger.error("文件 ：" + targetDirectory + File.separator + smallFileName + " 不存在!");
        }
    }
}
