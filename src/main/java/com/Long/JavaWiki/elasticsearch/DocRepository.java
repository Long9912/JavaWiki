package com.Long.JavaWiki.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 ElasticsearchRepository ：ES提供的操作CRUD接口
 *第一个参数：指定对象类型
 *第二个参数：ID类型
 */
public interface DocRepository extends ElasticsearchRepository<ESDoc,Long> {

}
