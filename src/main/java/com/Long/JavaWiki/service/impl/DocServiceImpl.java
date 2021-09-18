package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.entity.Doc;
import com.Long.JavaWiki.mapper.ContentMapper;
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

    @Autowired
    ContentMapper contentMapper;

    @Override
    public List<DocQueryResp> all(String ebookId) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        //根据电子书id查询
        wrapper.eq("ebook_id",ebookId);
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
        Content content=CopyUtil.copy(req,Content.class);
        //保存文档和文档内容
        super.saveOrUpdate(doc);
        //获取到文档的雪花id,再保存到单独文档内容表
        content.setId(doc.getId());
        //尝试更新
        int count = contentMapper.updateById(content);
        //文档内容表没有此数据时插入新数据
        if (count == 0){
            contentMapper.insert(content);
        }
        return true;
    }

    @Override
    public Content findContent(String id) {
        //文档阅读数加1
        docMapper.increaseViewCount(Long.valueOf(id));
        return contentMapper.selectById(id);
    }

    @Override
    public boolean vote(String id) {
        docMapper.increaseVoteCount(Long.valueOf(id));
        return true;
    }
}
