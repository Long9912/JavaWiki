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
 * 专栏 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
public interface EbookService extends IService<Ebook> {
    /**
     * 根据分类id查询专栏
     * @param req 分类id
     * @return  返回分类下专栏列表
     */
    List<EbookQueryResp> all(EbookQueryReq req);

    /**
     * 分页查询专栏,按照id降序排序
     * @return  分页数据
     */
    PageResp<EbookQueryResp> getList(EbookQueryReq req);

    /**
     * 保存或更新专栏
     */
    boolean saveOrUpdate(EbookSaveReq req);

}
