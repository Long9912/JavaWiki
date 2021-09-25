package com.Long.JavaWiki.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式 工具类
 */
@Component
public class RegExpUtil {
    /**
     * 获得html中的图片文件列表
     */
    public List<String> getImgList(String htmlStr){
        ArrayList<String> pics = new ArrayList<>();
        if (!StringUtils.hasText(htmlStr)){
            return pics;
        }
        // 得到<img />数据
        String regImg = "<img.*?(>|/>)";
        // 得到<img/>中image/xxx.xxx数据
        String regImage = "image/([^'\"]*)";
        Pattern imagePattern=Pattern.compile(regImg);
        Matcher imageMatcher=imagePattern.matcher(htmlStr);

        String img = "";
        while (imageMatcher.find()) {
            // 得到<img />数据
            img = imageMatcher.group();
            // 匹配<img>中的图片名
            Matcher m = Pattern.compile(regImage).matcher(img);
            while (m.find()) {
                //取第一个元组,得到image/后面的文件名
                pics.add(m.group(1));
            }
        }
        return pics;
    }
}
