package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.entity.Doc;
import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.mapper.ContentMapper;
import com.Long.JavaWiki.mapper.DocMapper;
import com.Long.JavaWiki.request.DocQueryReq;
import com.Long.JavaWiki.request.DocSaveReq;
import com.Long.JavaWiki.response.DocQueryResp;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.service.ContentService;
import com.Long.JavaWiki.service.DocService;
import com.Long.JavaWiki.util.CopyUtil;
import com.Long.JavaWiki.util.RedisUtil;
import com.Long.JavaWiki.util.RequestContext;
import com.Long.JavaWiki.webSocket.WsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
@Slf4j
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {
    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private ContentService contentService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsService wsService;

    @Override
    public List<DocQueryResp> all(String ebookId) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        //根据专栏id查询
        wrapper.eq("ebook_id", ebookId);
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
    @Transactional(rollbackFor=Exception.class)
    public void saveOrUpdate(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        //保存文章消息
        super.saveOrUpdate(doc);
        //获取到文章的雪花id,再保存到单独文章内容表
        content.setId(doc.getId());
        //保存文章内容,对比文本在服务器删除对应图片
        contentService.saveContent(content);
    }

    @Override
    public void deleteEbookDoc(Long ebookId) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        //根据专栏id查询
        wrapper.eq("ebook_id", ebookId);
        log.info("删除专栏:{}",ebookId);
        List<Doc> docList = docMapper.selectList(wrapper);
        for (Doc doc : docList) {
            log.info("删除文章:{}",doc.getName());
            docMapper.deleteById(doc.getId());
            contentService.deleteContent(String.valueOf(doc.getId()));
        }
    }

    @Override
    public String findContent(String id) {
        //文章阅读数加1
        docMapper.increaseViewCount(Long.valueOf(id));
        //使用Optional包装处理 null
        return Optional.ofNullable(contentMapper.selectById(id))
                .map(Content::getContent)
                .orElseThrow(() -> new BusinessException(EnumCode.DOC_EMPTY));
    }

    @Override
    public String editContent(String id) {
        //使用Optional包装处理 null
        return Optional.ofNullable(contentMapper.selectById(id))
                .map(Content::getContent)
                .orElseThrow(() -> new BusinessException(EnumCode.DOC_EMPTY));
    }

    @Override
    public void vote(String id) {
        //远程ip+文章Id作为key,24小时不能重复
        String key = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + key, 24)) {
            //使用完ThreadLocal后移除数据,防止内存泄漏
            RequestContext.removeRemoteAddr();
            log.info("点赞成功,清空线程本地变量ThreadLocal: {}",key);
            docMapper.increaseVoteCount(Long.valueOf(id));
        } else {
            RequestContext.removeRemoteAddr();
            log.info("重复点赞,清空线程本地变量ThreadLocal: {}",key);
            throw new BusinessException(EnumCode.VOTE_REPEAT);
        }
        //推送消息
        Doc docDB = docMapper.selectById(id);
        String logId= MDC.get("LOG_ID");
        //异步处理
        wsService.sendInfo("[" + docDB.getName() + "]被点赞",logId);
    }

    /**
     * 按专栏分组统计文章数据,并更新到对应的专栏中
     */
    @Override
    public void updateEbookInfo() {
        docMapper.updateEbookInfo();
    }
}
