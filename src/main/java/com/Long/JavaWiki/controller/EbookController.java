package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/list")
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.bookList(req);
        return list.getList().isEmpty() ? null : list;
    }
}

