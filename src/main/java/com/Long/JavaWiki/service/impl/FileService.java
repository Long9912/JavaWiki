package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileService {

    @Value("${file.localUrl}")
    private String uploadPath;

    @Autowired
    SnowFlake snowFlake;

    /**
     * 上传图片
     * @param file 文件
     * @return 上传文件的 URL相对地址
     */
    public String uploadFile(MultipartFile file) throws IOException {
        File filePath = new File(uploadPath);
        //如果文件夹不存在，创建
        if (!filePath.isDirectory()) {
            //递归生成文件夹
            filePath.mkdirs();
        }
        String fileName = "";
        String originalName = file.getOriginalFilename();
        long id = snowFlake.nextId();
        if (originalName.endsWith(".jpg")
                || originalName.endsWith(".png") || originalName.endsWith(".jpeg")) {
            fileName = id + "-" + originalName;
        } else {
            throw new BusinessException(EnumCode.PICTURE_FORMAT_ERROR);
        }
        file.transferTo(new File(filePath, fileName));
        log.info("上传文件:{}", fileName);
        return "/image/" + fileName;
    }

    /**
     * 删除文件
     */
    public String delFile(String fileName) {

        String resultInfo = null;
        String imgPath = uploadPath + fileName;
        File file = new File(imgPath);
        //文件是否存在
        if (file.exists()) {
            if (file.delete()) {
                resultInfo = "删除成功";
                log.info("删除文件:{}", fileName);
            } else {
                resultInfo = "删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        return resultInfo;
    }

}
