package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.Category;
import com.Long.JavaWiki.request.CategoryQueryReq;
import com.Long.JavaWiki.request.CategorySaveReq;
import com.Long.JavaWiki.response.CategoryQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-12
 */
public interface CategoryService extends IService<Category> {
    List<CategoryQueryResp> all();

    PageResp<CategoryQueryResp> list(CategoryQueryReq req);

    boolean saveOrUpdate(CategorySaveReq req);
}
