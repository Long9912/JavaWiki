package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.entity.Doc;
import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文档 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
public interface DocService extends IService<Doc> {
    List<DocQueryResp> all(String ebookId);

    PageResp<DocQueryResp> list(DocQueryReq req);

    boolean saveOrUpdate(DocSaveReq req);

    Content findContent(String id);

    void vote(String id);
}
