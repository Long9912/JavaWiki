package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.User;
import com.Long.JavaWiki.mapper.UserMapper;
import com.Long.JavaWiki.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
