package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Category;
import com.Long.JavaWiki.mapper.CategoryMapper;
import com.Long.JavaWiki.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
