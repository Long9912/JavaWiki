package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/list")
    public List<Ebook> list(){
        return ebookService.list();
    }
}

