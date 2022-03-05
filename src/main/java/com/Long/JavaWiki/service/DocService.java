package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Doc;
import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
public interface DocService extends IService<Doc> {
    /**
     * 根据专栏id查询所有文章
     * @param ebookId 专栏id
     * @return  返回文章列表
     */
    List<DocQueryResp> all(String ebookId);

    /**
     * 分页查询文章
     * @return  分页数据
     */
    PageResp<DocQueryResp> list(DocQueryReq req);

    /**
     * 保存或更新文章信息,文章内容,更新时对比文本获取被删除的图片,然后在服务器中删除文件
     */
    void saveOrUpdate(DocSaveReq req);

    /**
     * 删除专栏下的全部文章
     */
    void deleteEbookDoc(Long ebookId);

    /**
     * 查看文章内容,阅读数加1
     * @param id 文章id
     * @return  文章内容
     */
    String findContent(String id);

    /**
     * 编辑文章内容
     * @param id 文章id
     * @return  文章内容
     */
    String editContent(String id);

    /**
     * 对文章点赞时,通过Aop获取远程ip保存在ThreadLocal
     * 获取到ip后,加上文章Id作为key存入redis,24小时不能重复
     * @param id 文章id
     */
    void vote(String id);

    /**
     * 通过定时任务更新专栏数据
     */
    void updateEbookInfo();
}
