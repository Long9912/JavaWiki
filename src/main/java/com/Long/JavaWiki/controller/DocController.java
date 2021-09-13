package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.DocService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 文档 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    DocService docService;

    @ApiOperation("默认查询全部文档,传入分类id时查询分类下文档")
    @GetMapping("/all")
    public List<DocQueryResp> all() {
        List<DocQueryResp> list = docService.all();
        return list.isEmpty() ? null : list;
    }

    @ApiOperation("分页查询文档")
    @ApiImplicitParam(name = "req", value = "传入分页参数,如果有传入参数则模糊查询文档", required = true, dataType = "DocQueryReq", paramType = "query")
    @GetMapping("/list")
    public PageResp<DocQueryResp> list(@Validated DocQueryReq req) {
        PageResp<DocQueryResp> list = docService.list(req);
        return list.getList().isEmpty() ? null : list;
    }

    @ApiOperation("编辑或新增文档")
    @PostMapping("/save")
    public String save(@Validated @RequestBody DocSaveReq req) {
        docService.saveOrUpdate(req);
        return "保存成功";
    }

    @ApiOperation("通过id逻辑删除文档")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        docService.removeById(id);
        return "删除成功";
    }
}

