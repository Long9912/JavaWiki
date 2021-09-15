package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.CategoryQueryReq;
import com.Long.JavaWiki.request.CategorySaveReq;
import com.Long.JavaWiki.response.CategoryQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 分类 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-12
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("查询全部分类")
    @GetMapping("/all")
    public List<CategoryQueryResp> all() {
        return categoryService.all();
    }

    @ApiOperation("分页查询分类")
    @ApiImplicitParam(name = "req", value = "传入分页参数,如果有传入书名则模糊查询分类", required = true, dataType = "CategoryQueryReq", paramType = "query")
    @GetMapping("/list")
    public PageResp<CategoryQueryResp> list(@Validated CategoryQueryReq req) {
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        return list.getList().isEmpty() ? null : list;
    }

    @ApiOperation("编辑或新增分类")
    @PostMapping("/save")
    public String save(@Validated @RequestBody CategorySaveReq req) {
        categoryService.saveOrUpdate(req);
        return "保存成功";
    }

    @ApiOperation("通过id删除分类")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return "删除成功";
    }
}
