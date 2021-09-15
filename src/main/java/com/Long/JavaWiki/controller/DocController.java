package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.ContentService;
import com.Long.JavaWiki.service.DocService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    ContentService contentService;

    @ApiOperation("传入电子书id查询电子书的文档")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/all/{ebookId}")
    public List<DocQueryResp> all(@PathVariable String ebookId) {
        List<DocQueryResp> list = docService.all(ebookId);
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

    @ApiOperation("通过一个或多个id逻辑删除文档")
    @ApiImplicitParam(name = "id", value = "传入一个或多个ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/delete/{idsStr}")
    public String delete(@PathVariable String idsStr) {
        //将string通过逗号分割成字符数组,再转为List
        List<String> ids = Arrays.asList(idsStr.split(","));
        docService.removeByIds(ids);
        contentService.removeByIds(ids);
        return "删除成功";
    }

    @ApiOperation("通过id查找文档内容")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/findContent/{id}")
    public String findContent(@PathVariable String id) {
        //获取文档文本,使用Optional包装处理 null,
        Optional<String> text=Optional
                .ofNullable(contentService.getById(id))
                .map(content -> content.getContent());
        return text.orElse("文档内容为空");
    }
}

