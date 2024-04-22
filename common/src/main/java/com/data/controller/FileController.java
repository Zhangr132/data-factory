package com.data.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @Author zhangr132
 * @Date 2024/4/19 8:55
 * @注释
 */
@Slf4j
@Api("文件管理")
@RestController
@RequestMapping("/file")
public class FileController {
    //项目端口
    @Value("${server.port}")
    public String port;
    //文件磁盘路径
    @Value("${file.upload.path}")
    private String fileUploadPath;


    /**
     * 文件上传
     * 返回文件路径
     * @param file
     * @return uploadFile
     * @throws IOException
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(@RequestParam MultipartFile file) throws IOException {
        log.info("文件上传开始");

        if (file.isEmpty()) {
            return R.Failed("上传文件不能为空");
        }

        //获取 localhost 的ip地址
        String localhost = InetAddress.getLocalHost().getHostAddress();
        log.info("localhost的ip地址是：" + localhost);

        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件的类型
        String type = FileUtil.extName(originalFilename);
        log.info("文件类型是：" + type);
        //获取文件大小
        long size = file.getSize();
        log.info("文件大小是：" + size+"字节");

        //获取文件
        File uploadParentFile = new File(fileUploadPath+File.separator+type);
        //判断文件目录是否存在
        if(!uploadParentFile.exists()) {
            //如果不存在就创建文件夹
            uploadParentFile.mkdirs();
        }
        //定义一个文件唯一标识码（UUID）
        String uuid = UUID.randomUUID().toString();

        // 获取当前工作目录
        Path basePaths = Paths.get(System.getProperty("user.dir"));
        // 解析文件上传路径
        Path filePath = basePaths.resolve(fileUploadPath+File.separator+type);
        System.out.println("当前工作目录"+basePaths);
        System.out.println("文件上传路径"+filePath);

        /*
         * File.separator 系统文件分隔符，windows下为“\”，linux下为“/”
         * StrUtil.DOT 文件扩展名分隔符，windows下为“.”，linux下为“.”
         */
        File uploadFile = new File(filePath + File.separator + uuid + StrUtil.DOT + type);
        //将临时文件转存到指定磁盘位置
        file.transferTo(uploadFile);
        String url= "http://"+localhost+":"+port+"/file/download?path="+uploadFile.toString();
        System.out.println("url="+url);
        System.out.println("上传文件路径"+uploadFile);

        return R.Success("文件上传成功",uploadFile.toString());
    }

    /**
     * 图片上传
     * @param file
     * @return fileName
     * @throws IOException
     */
    @ApiOperation("图片上传")
    @PostMapping("/uploadImage")
    public R uploadImage(@RequestParam MultipartFile file) throws IOException {
        log.info("图片上传开始");

        if (file.isEmpty()) {
            return R.Failed("上传文件不能为空");
        }
        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件的类型
        String type = FileUtil.extName(originalFilename);
        log.info("文件类型是：" + type);
        //获取文件大小
        long size = file.getSize();
        log.info("文件大小是：" + size+"字节");

        //获取文件
        File uploadParentFile = new File(fileUploadPath+File.separator+"images");
        //判断文件目录是否存在
        if(!uploadParentFile.exists()) {
            //如果不存在就创建文件夹
            uploadParentFile.mkdirs();
        }
        //定义一个文件唯一标识码（UUID）
        String uuid = UUID.randomUUID().toString();

        // 获取当前工作目录
        Path basePaths = Paths.get(System.getProperty("user.dir"));
        // 解析文件上传路径
        Path filePath = basePaths.resolve(fileUploadPath+File.separator+"images");
        System.out.println("文件上传路径"+filePath);

        String fileName = uuid + StrUtil.DOT + type;

        /*
         * File.separator 系统文件分隔符，windows下为“\”，linux下为“/”
         * StrUtil.DOT 文件扩展名分隔符，windows下为“.”，linux下为“.”
         */
        File uploadFile = new File(filePath + File.separator + uuid + StrUtil.DOT + type);
        //将临时文件转存到指定磁盘位置
        file.transferTo(uploadFile);
        System.out.println("上传图片"+uploadFile);

        return R.Success("图片上传成功",fileName);
    }
}
