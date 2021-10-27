package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Content;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文档内容 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-14
 */
public interface ContentService extends IService<Content> {
    /**
     * 根据id删除文档,并在服务器中删除文档图片
     * @param id 文档id
     */
    void deleteContent(String id);

    /**
     * 根据id列表删除文档,并在服务器中删除文档图片
     * @param idList 文档id列表
     */
    void deleteContents(List<String> idList);

    /**
     * 通过id查询,有文档时更新,对比文本获取被删除的图片,
     * 然后在服务器中删除对应图片,没有文档时增加新增
     */
    void saveContent(Content content);
}
