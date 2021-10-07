package com.Long.JavaWiki.mapper;

import com.Long.JavaWiki.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户 Mapper 接口
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 设置管理员
     * @param id 用户id
     */
    void setAdmin(Long id);
}
