package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    List<EbookQueryResp> bookList();
    List<EbookQueryResp> listByName(QueryWrapper<Ebook> wrapper);
}
