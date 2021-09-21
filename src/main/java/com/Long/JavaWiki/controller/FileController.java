package com.Long.JavaWiki.controller;

import com.Long.JavaWiki.service.impl.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api("文件控制类")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/fileUpload")
    @ApiOperation(value = "上传下载图片")
    public String filesUpload(@RequestParam("file") MultipartFile image) throws IOException {
        return fileService.uploadFile(image);
    }

    @ApiOperation(value = "删除文件")
    @DeleteMapping("/deleteFile/{path}/{fileName}")
    public String delFile(@PathVariable String path,@PathVariable String fileName) {
        return fileService.delFile(fileName);
    }
}
