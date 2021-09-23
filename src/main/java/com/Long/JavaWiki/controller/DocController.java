package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.ContentService;
import com.Long.JavaWiki.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 文档 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
@Api("文档控制类")
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    @Autowired
    private ContentService contentService;

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

    @ApiOperation("通过id查找文档内容,点赞数加1")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/findContent/{id}")
    public String findContent(@PathVariable String id) {
        return docService.findContent(id);
    }

    @ApiOperation("编辑时通过id查找文档内容")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/editContent/{id}")
    public String editContent(@PathVariable String id) {
        return docService.editContent(id);
    }

    @ApiOperation("通过id对文档点赞")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/vote/{id}")
    public String vote(@PathVariable String id) {
        docService.vote(id);
        return "点赞成功";
    }
}

