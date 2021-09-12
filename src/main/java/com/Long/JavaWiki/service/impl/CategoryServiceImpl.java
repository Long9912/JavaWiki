package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Category;
import com.Long.JavaWiki.mapper.CategoryMapper;
import com.Long.JavaWiki.request.CategoryQueryReq;
import com.Long.JavaWiki.request.CategorySaveReq;
import com.Long.JavaWiki.response.CategoryQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.CategoryService;
import com.Long.JavaWiki.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分类 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-12
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<CategoryQueryResp> all() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        //根据sort排序
        wrapper.orderByAsc("sort");
        List<Category> categoryList = categoryMapper.selectList(wrapper);
        return CopyUtil.copyList(categoryList, CategoryQueryResp.class);
    }

    @Override
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        //根据sort排序
        wrapper.orderByAsc("sort");
        //传入参数有name时模糊查询
        if (req.getName() != null) {
            wrapper.like("name", req.getName());
        }
        //获取当前页面和大小进行分页查询
        Page<Category> page = new Page<>(req.getPage(), req.getSize());
        categoryMapper.selectPage(page, wrapper);
        //获取总数和结果列表
        long pageTotal = page.getTotal();
        List<Category> categoryList = page.getRecords();
        //转换为响应类型
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return new PageResp<>(pageTotal, list);
    }

    @Override
    public boolean saveOrUpdate(CategorySaveReq req) {
        Category category=CopyUtil.copy(req,Category.class);
        return super.saveOrUpdate(category);
    }
}
