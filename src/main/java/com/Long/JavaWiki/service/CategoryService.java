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
    /**
     * 查询所有分类
     * @return  返回分类列表
     */
    List<CategoryQueryResp> all();

    /**
     * 分页查询分类
     * @return  分页数据
     */
    PageResp<CategoryQueryResp> list(CategoryQueryReq req);

    /**
     * 保存或更新分类
     */
    boolean saveOrUpdate(CategorySaveReq req);
}
