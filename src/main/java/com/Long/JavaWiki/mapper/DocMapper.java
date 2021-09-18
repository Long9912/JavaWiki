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

    void increaseViewCount(@Param("id") Long id);

}
