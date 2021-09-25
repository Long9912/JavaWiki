package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.util.RegExpUtil;
import com.Long.JavaWiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService {

    @Value("${file.localUrl}")
    private String uploadPath;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RegExpUtil regExpUtil;


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

    /**
     * 更新时对比文本获取被删除的图片
     */
    public void compareDeleteImg(String oldText, String newText) {
        List<String> oldImgList = regExpUtil.getImgList(oldText);
        List<String> newImgList = regExpUtil.getImgList(newText);

        List<String> notExist =new ArrayList<>();
        for (String imgFile : oldImgList) {
            //当新图片列表里不存在旧图片时,说明图片被删除了
            if (!newImgList.contains(imgFile)){
                notExist.add(imgFile);
            }
        }
        notExist.forEach(imgFile->{
            log.info("更新文章时发现被删除的图片: {}",imgFile);
            delFile(imgFile);
        });
    }

    /**
     * 删除文章时获取被删除的图片
     */
    public void deleteImg(String htmlText) {
        List<String> imgList = regExpUtil.getImgList(htmlText);
        imgList.forEach(imgFile->{
            log.info("删除文章时发现被删除的图片: {}",imgFile);
            delFile(imgFile);
        });
    }
}
