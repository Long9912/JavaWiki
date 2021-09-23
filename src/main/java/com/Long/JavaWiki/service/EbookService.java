package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.request.EbookSaveReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 电子书 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
public interface EbookService extends IService<Ebook> {
    /**
     * 根据分类id查询电子书
     * @param req 分类id
     * @return  返回分类下电子书列表
     */
    List<EbookQueryResp> all(EbookQueryReq req);

    /**
     * 分页查询电子书
     * @return  分页数据
     */
    PageResp<EbookQueryResp> getList(EbookQueryReq req);

    /**
     * 保存或更新电子书
     */
    boolean saveOrUpdate(EbookSaveReq req);

}
