package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.request.EbookSaveReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.EbookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 电子书 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
@Api("电子书控制类")
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    EbookService ebookService;

    @ApiOperation("默认查询全部电子书,传入分类id时查询分类下电子书")
    @GetMapping("/all")
    public List<EbookQueryResp> all(EbookQueryReq req) {
        List<EbookQueryResp> list = ebookService.all(req);
        return list.isEmpty() ? null : list;
    }

    @ApiOperation("分页查询电子书")
    @ApiImplicitParam(name = "req", value = "传入分页参数,如果有传入书名则模糊查询电子书", required = true, dataType = "EbookQueryReq", paramType = "query")
    @GetMapping("/list")
    public PageResp<EbookQueryResp> list(@Validated EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.getList(req);
        return list.getList().isEmpty() ? null : list;
    }

    @ApiOperation("编辑或新增电子书")
    @PostMapping("/save")
    public String save(@Validated @RequestBody EbookSaveReq req) {
        ebookService.saveOrUpdate(req);
        return "保存成功";
    }

    @ApiOperation("通过id逻辑删除电子书")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ebookService.removeById(id);
        return "删除成功";
    }
}

