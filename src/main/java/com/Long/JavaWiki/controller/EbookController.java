package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.service.EbookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<EbookQueryResp> list(){
        return ebookService.bookList();
    }

    @GetMapping("/list/{name}")
    public List<EbookQueryResp> listByName(@PathVariable String name){
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<EbookQueryResp> list = ebookService.listByName(wrapper);
        if (list.isEmpty()){
            return null;
        }
        return list;
    }
}

