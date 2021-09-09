package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.mapper.EbookMapper;
import com.Long.JavaWiki.response.EbookQueryResp;
import com.Long.JavaWiki.service.EbookService;
import com.Long.JavaWiki.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public List<EbookQueryResp> bookList() {
        List<Ebook> ebookList = ebookMapper.selectList(null);
        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

    @Override
    public List<EbookQueryResp> listByName(QueryWrapper<Ebook> wrapper) {
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

}
