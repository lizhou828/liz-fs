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

    /* 默认缩略图宽度 */
    public static final int DEFAULT_SMALL_IMG_WIDTH = 100;

    /* 默认缩略图长度 */
    public static final int DEFAULT_SMALL_IMG_LENGTH = 100;

}
