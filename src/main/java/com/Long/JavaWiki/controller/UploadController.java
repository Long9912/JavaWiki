package com.Long.JavaWiki.controller;

import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Api("上传控制类")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${file.localUrl}")
    private String uploadPath;

    @PostMapping("/fileUpload")
    @ApiOperation(value = "上传下载图片")
    public String filesUpload(@RequestParam("file") MultipartFile image) throws IOException {

        File filePath = new File(uploadPath);
        //如果文件夹不存在，创建
        if (!filePath.isDirectory()) {
            //递归生成文件夹
            filePath.mkdirs();
        }
        String fileName = "";
        if (image.getOriginalFilename().endsWith(".jpg")) {
            fileName = String.format("%s.jpg", System.currentTimeMillis());
        } else if (image.getOriginalFilename().endsWith(".png")) {
            fileName = String.format("%s.jpg", System.currentTimeMillis());
        } else if (image.getOriginalFilename().endsWith(".jpeg")) {
            fileName = String.format("%s.jpeg", System.currentTimeMillis());
        } else if (image.getOriginalFilename().endsWith(".bmp")) {
            fileName = String.format("%s.bmp", System.currentTimeMillis());
        } else {
            throw  new BusinessException(EnumCode.PICTURE_FORMAT_ERROR);
        }
        image.transferTo(new File(filePath, fileName));
        return "/image/"+fileName;
    }

}
