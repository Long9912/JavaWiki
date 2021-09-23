package com.Long.JavaWiki.mapper;

import com.Long.JavaWiki.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 电子书 Mapper 接口
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
public interface DocMapper extends BaseMapper<Doc> {

    /**
     * 阅读数自增
     * @param id 文档id
     */
    void increaseViewCount(@Param("id") Long id);

    /**
     * 点赞数自增
     * @param id 文档id
     */
    void increaseVoteCount(@Param("id") Long id);

    /**
     * 统计电子书下的文档数据,然后更新电子书的文档数,阅读数,点赞数
     */
    void updateEbookInfo();
}
