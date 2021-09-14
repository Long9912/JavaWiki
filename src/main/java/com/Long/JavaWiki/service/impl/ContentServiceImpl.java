package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.mapper.ContentMapper;
import com.Long.JavaWiki.service.ContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文档内容 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-14
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

}
