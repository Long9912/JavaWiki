package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.mapper.EbookMapper;
import com.Long.JavaWiki.request.EbookQueryReq;
import com.Long.JavaWiki.request.EbookSaveReq;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.EbookService;
import com.Long.JavaWiki.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 电子书 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements EbookService {

    @Autowired
    EbookMapper ebookMapper;

    @Override
    public List<EbookQueryResp> all(EbookQueryReq req) {
        List<Ebook> ebookList = ebookMapper.selectList(null);
        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

    @Override
    public PageResp<EbookQueryResp> bookList(EbookQueryReq req) {
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        //传入参数有name时模糊查询
        if (req.getName() != null) {
            wrapper.like("name", req.getName());
        }
        //获取当前页面和大小进行分页查询
        Page<Ebook> page = new Page<>(req.getPage(), req.getSize());
        ebookMapper.selectPage(page, wrapper);
        //获取总数和结果列表
        long pageTotal = page.getTotal();
        List<Ebook> ebookList = page.getRecords();
        //转换为响应类型
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        return new PageResp<>(pageTotal, list);
    }

    @Override
    public boolean saveOrUpdate(EbookSaveReq req) {
        Ebook ebook=CopyUtil.copy(req,Ebook.class);
        return super.saveOrUpdate(ebook);
    }
}
