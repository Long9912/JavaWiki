package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.request.EbookSaveReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.DocService;
import com.Long.JavaWiki.service.EbookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 笔记 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
@Api("笔记控制类")
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @Autowired
    private DocService docService;

    @ApiOperation("默认查询全部笔记,传入分类id时查询分类下笔记")
    @GetMapping("/all")
    public List<EbookQueryResp> all(EbookQueryReq req) {
        List<EbookQueryResp> list = ebookService.all(req);
        return list.isEmpty() ? null : list;
    }

    @ApiOperation("分页查询笔记,按照id降序排序")
    @ApiImplicitParam(name = "req", value = "传入分页参数,如果有传入笔记名则模糊查询笔记", required = true, dataType = "EbookQueryReq", paramType = "query")
    @GetMapping("/list")
    public PageResp<EbookQueryResp> list(@Validated EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.getList(req);
        return list.getList().isEmpty() ? null : list;
    }

    @RequiresRoles("admin")
    @ApiOperation("编辑或新增笔记")
    @PostMapping("/save")
    public String save(@Validated @RequestBody EbookSaveReq req) {
        ebookService.saveOrUpdate(req);
        return "保存成功";
    }

    @RequiresRoles("admin")
    @ApiOperation("通过id逻辑删除笔记")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ebookService.removeById(id);
        docService.deleteEbookDoc(id);
        return "删除成功";
    }
}

