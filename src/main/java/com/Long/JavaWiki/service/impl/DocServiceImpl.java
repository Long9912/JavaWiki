package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Doc;
import com.Long.JavaWiki.mapper.DocMapper;
import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.DocService;
import com.Long.JavaWiki.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文档 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {
    @Autowired
    DocMapper docMapper;

    @Override
    public List<DocQueryResp> all() {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        //根据sort排序
        wrapper.orderByAsc("sort");
        List<Doc> docList = docMapper.selectList(wrapper);
        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    @Override
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        //根据sort排序
        wrapper.orderByAsc("sort");
        //传入参数有name时模糊查询
        if (req.getName() != null) {
            wrapper.like("name", req.getName());
        }
        //获取当前页面和大小进行分页查询
        Page<Doc> page = new Page<>(req.getPage(), req.getSize());
        docMapper.selectPage(page, wrapper);
        //获取总数和结果列表
        long pageTotal = page.getTotal();
        List<Doc> docList = page.getRecords();
        //转换为响应类型
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return new PageResp<>(pageTotal, list);
    }

    @Override
    public boolean saveOrUpdate(DocSaveReq req) {
        Doc doc=CopyUtil.copy(req,Doc.class);
        return super.saveOrUpdate(doc);
    }
}
