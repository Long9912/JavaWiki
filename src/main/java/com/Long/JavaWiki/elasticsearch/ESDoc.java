package com.Long.JavaWiki.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 定义elasticsearch中的文章索引
 * Document作用: 将doc的对象映射成ES中一条json格式文章
 * indexName: 用来指定这个对象的转为json文章存入那个索引中
 */
@Document(indexName = "doc")
@Data
public class ESDoc {
    /**
     * 与ES中_id进行映射
     */
    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long ebookId;

    /**
     * 用在属性上 代表mapping中一个属性 一个字段
     * type:属性 用来指定字段类型 analyzer:指定分词器
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
}
