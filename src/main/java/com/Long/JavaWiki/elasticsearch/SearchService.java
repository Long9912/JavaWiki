package com.Long.JavaWiki.elasticsearch;

import com.Long.JavaWiki.entity.Content;
import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.mapper.ContentMapper;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.util.CopyUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 导入数据库数据到搜索索引
     */
    public String importDoc(){
        long startTime = System.currentTimeMillis();
        //从数据库获取所有Doc文档内容
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        List<Content> contentList = contentMapper.selectList(wrapper);
        //清洗富文本数据,获得纯文本
        for (Content content :contentList) {
            String richText = content.getContent();
            String text = richText.replaceAll("(<.*?>|(\\n)|(&nbsp;))", "");
            content.setContent(text);
        }
        //使用复制工具类,从数据库对象转到搜索引擎对象
        List<ESDoc> esDocList = CopyUtil.copyList(contentList, ESDoc.class);
        //更新搜索引擎索引
        docRepository.saveAll(esDocList);
        long runTime = System.currentTimeMillis() - startTime;
        return "更新[搜索索引]文档数:"+esDocList.size()+" 处理时间:"+runTime+"ms";
    }

    /**
     * 高亮模糊分页查询
     * @param page 当前页码
     * @param size 每页大写
     * @param text 搜索关键词
     * @return PageResp 总数和列表
     */
    public PageResp<Content> HighLightSearch( String text, int page, int size) throws IOException {
        if(text.length()>15){
            throw new BusinessException(EnumCode.SEARCH_FAIL);
        }
        //集合存放查找到的数据
        List<Content> list = new ArrayList<>();
        //高亮配置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("*").requireFieldMatch(false).preTags("<span style='color:red'>").postTags("</span>");

        //查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页及模糊查询
        searchSourceBuilder.from((page-1)*size).size(size)
                .highlighter(highlightBuilder)
                .query(QueryBuilders.multiMatchQuery(text, "name", "content"));

        //查询请求
        SearchRequest searchRequest = new SearchRequest();
        //从哪个索引查找
        searchRequest.indices("doc").source(searchSourceBuilder);
        //查询方法
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //获取搜索结果总数
        long pageTotal=searchResponse.getHits().getTotalHits().value;
        //总数为0时,直接返回搜索为空异常
        if (pageTotal == 0){
            throw new BusinessException(EnumCode.SEARCH_EMPTY);
        }

        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            //原文档
            System.out.println(hit.getSourceAsString());
            //使用Fastjson转换后返回对象
            Content content = JSON.parseObject(hit.getSourceAsString(), Content.class);

            //高亮部分
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("name")) {
                content.setName(highlightFields.get("name").fragments()[0].toString());
            }
            if (highlightFields.containsKey("content")) {
                content.setContent(highlightFields.get("content").fragments()[0].toString());
            }
            list.add(content);
        }
        return new PageResp<>(pageTotal, list);
    }
}
