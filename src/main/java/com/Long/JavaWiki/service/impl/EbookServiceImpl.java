package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Ebook;
import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
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
import java.util.Objects;

/**
 * <p>
 * 专栏 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private FileService fileService;

    @Override
    public List<EbookQueryResp> all(EbookQueryReq req) {
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        //传入参数有CategoryId2时按分类id查询
        if (req.getCategoryId2() != null) {
            wrapper.eq("category2_id", req.getCategoryId2());
        }
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        if (ebookList.isEmpty()){
            throw new BusinessException(EnumCode.CATEGORY_EMPTY);
        }
        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

    @Override
    public PageResp<EbookQueryResp> getList(EbookQueryReq req) {
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        //传入参数有name时模糊查询
        if (req.getName() != null) {
            wrapper.like("name", req.getName());
        }
        //获取当前页面和大小进行分页查询
        Page<Ebook> page = new Page<>(req.getPage(), req.getSize());
        //按照id降序排序
        wrapper.orderByDesc("id");
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
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        //更新图片时,删除旧图片
        if (ebook.getId() != null && ebook.getCover() != null) {
            //查出数据库数据
            Ebook ebookDB = ebookMapper.selectById(ebook.getId());
            //对比封面看是否更新
            if (!Objects.equals(ebookDB.getCover(), ebook.getCover())) {
                //得到旧图片地址
                String filePath = ebookDB.getCover();
                //获得文件名
                int lastIndexOf = filePath.lastIndexOf("/");
                String fileName = filePath.substring(lastIndexOf + 1);
                //根据文件名删除旧图片
                fileService.delFile(fileName);
            }
        }
        return super.saveOrUpdate(ebook);
    }
}
