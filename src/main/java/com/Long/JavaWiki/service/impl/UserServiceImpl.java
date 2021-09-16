package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.User;
import com.Long.JavaWiki.exception.BusinessException;
import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.mapper.UserMapper;
import com.Long.JavaWiki.request.UserLoginReq;
import com.Long.JavaWiki.request.UserQueryReq;
import com.Long.JavaWiki.request.UserResetReq;
import com.Long.JavaWiki.request.UserSaveReq;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.response.UserLoginResp;
import com.Long.JavaWiki.response.UserQueryResp;
import com.Long.JavaWiki.service.UserService;
import com.Long.JavaWiki.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public PageResp<UserQueryResp> getList(UserQueryReq req) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //传入参数有name时模糊查询
        if (req.getLoginName() != null) {
            wrapper.like("login_name", req.getLoginName());
        }
        //获取当前页面和大小进行分页查询
        Page<User> page = new Page<>(req.getPage(), req.getSize());
        userMapper.selectPage(page, wrapper);
        //获取总数和结果列表
        long pageTotal = page.getTotal();
        List<User> userList = page.getRecords();
        //转换为响应类型
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        return new PageResp<>(pageTotal, list);
    }

    @Override
    public void saveOrUpdate(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        //新增
        if (ObjectUtils.isEmpty(user.getId())) {
            //查找用户名是否重复
            User userDB = selectByLoginName(user.getLoginName());
            if (ObjectUtils.isEmpty(userDB)) {
                userMapper.insert(user);
            } else {
                throw new BusinessException(EnumCode.USER_EXIST);
            }
        }
        //用户名不可修改
        user.setLoginName(null);
        user.setPassword(null);
        //更新
        userMapper.updateById(user);
    }

    @Override
    public User selectByLoginName(String loginName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name", loginName);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void resetPassword(UserResetReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateById(user);
    }

    @Override
    public UserLoginResp login(UserLoginReq req) {
        User userDB = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDB)){
            log.info("用户名不存在:{}",req.getLoginName());
            throw new BusinessException(EnumCode.LOGIN_ERROR);
        }else {
            if (userDB.getPassword().equals(req.getPassword())){
                return CopyUtil.copy(userDB, UserLoginResp.class);
            }else {
                log.info("密码不对:{}",req.getPassword());
                throw new BusinessException(EnumCode.LOGIN_ERROR);
            }
        }
    }

}
