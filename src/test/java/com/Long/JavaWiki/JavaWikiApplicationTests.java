package com.Long.JavaWiki;

import com.Long.JavaWiki.config.JavaWikiApplication;
import com.Long.JavaWiki.entity.User;
import com.Long.JavaWiki.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {JavaWikiApplication.class})
class JavaWikiApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setAge(18);
        user.setEmail("qq.com");
        user.setName("Long");
        int row = userMapper.insert(user);
        System.out.println(row);
    }

    @Test
    public void testUpdate() {
        User user = userMapper.selectById(5L);
        user.setEmail("qq.com");
        user.setName("Long12");
        int row = userMapper.updateById(user);
        System.out.println(row);
    }

    // 测试批量查询
    @Test
    public void testSelectByBatchId() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    // 按条件查询之一使用map操作
    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("name", "Long12");
        map.put("age", 24);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    // 测试分页查询
    @Test
    public void testPage() {
        // 参数一：当前页
        // 参数二：页面大小
        Page<User> page = new Page<>(2, 5);
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    @Test
    public void testDelete() {
        userMapper.deleteById(2L);
    }

    @Test
    void testWrapper() {
        // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于21
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name")
                .isNotNull("email")
                .ge("age", 21);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test2(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","Long12");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    //模糊查询
    @Test
    void test3(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,30)
                .notLike("name","e")
                // 左和右 q%
                .likeRight("email","q");
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
        int count = userMapper.selectCount(wrapper);// 查询结果数
        System.out.println(count);
    }

}
