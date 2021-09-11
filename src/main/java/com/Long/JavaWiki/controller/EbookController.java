package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.request.EbookSaveReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    EbookService ebookService;

    @GetMapping("/all")
    public List<EbookQueryResp> all(EbookQueryReq req) {
        return ebookService.all(req);
    }

    @GetMapping("/list")
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.bookList(req);
        return list.getList().isEmpty() ? null : list;
    }

    @PostMapping("/save")
    public String save(@RequestBody EbookSaveReq req) {
        ebookService.saveOrUpdate(req);
        return "保存成功";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ebookService.removeById(id);
        return "删除成功";
    }
}

