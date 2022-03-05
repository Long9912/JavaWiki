package com.Long.JavaWiki.controller;

import com.Long.JavaWiki.elasticsearch.SearchService;
import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.response.PageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api("搜索控制类")
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequiresRoles("admin")
    @ApiOperation("导入数据库中的全部文章到搜索引擎索引中")
    @GetMapping("/importDoc")
    public String importDoc() {
        String text = searchService.importDoc();
        return text;
    }

    @ApiOperation("高亮模糊分页查询")
    @GetMapping("/HighLight/{text}/{page}/{size}")
    public PageResp<Content> HighLightSearch(@PathVariable String text,
                                             @PathVariable int page,
                                             @PathVariable int size) throws IOException {
        PageResp<Content> pageResp = searchService.HighLightSearch(text, page, size);
        return pageResp;
    }

}
