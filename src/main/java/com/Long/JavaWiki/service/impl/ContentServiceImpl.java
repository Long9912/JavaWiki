package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.mapper.ContentMapper;
import com.Long.JavaWiki.service.ContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 文档内容 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-14
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private FileService fileService;

    @Override
    public void deleteContent(String id) {
        //查出文章内容
        Content contentDB = contentMapper.selectById(id);
        //存在文章内容才删除
        if (!ObjectUtils.isEmpty(contentDB)) {
            //查出文章中的图片并删除
            fileService.deleteImg(contentDB.getContent());
            contentMapper.deleteById(id);
        }
    }

    @Override
    public void deleteContents(List<String> idList) {
        Content contentDB = null;
        for (String id : idList) {
            //查出文章内容
             contentDB = contentMapper.selectById(id);
            //存在文章内容才删除
            if (!ObjectUtils.isEmpty(contentDB)) {
                //查出文章中的图片并删除
                fileService.deleteImg(contentDB.getContent());
                contentMapper.deleteById(id);
            }
        }
    }

    /**
     * 通过id查询,有文档时更新,对比文本获取被删除的图片,
     * 然后在服务器中删除对应图片,没有文档时增加新增
     */
    @Override
    public void saveContent(Content content) {
        //查询数据库有没有该文档
        Content contentDB = contentMapper.selectById(content.getId());
        if (!ObjectUtils.isEmpty(contentDB)) {
            //有文档时更新文档内容
            contentMapper.updateById(content);
            //对比文本获取被删除的图片,然后在服务器中删除文件
            fileService.compareDeleteImg(contentDB.getContent(), content.getContent());
        } else {
            //文档内容表没有此数据时插入新数据
            contentMapper.insert(content);
        }
    }
}
