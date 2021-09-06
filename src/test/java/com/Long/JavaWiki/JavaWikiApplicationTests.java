package com.Long.JavaWiki;

import com.Long.JavaWiki.config.JavaWikiApplication;
import com.Long.JavaWiki.mapper.UserMapper;
import com.Long.JavaWiki.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {JavaWikiApplication.class})
class JavaWikiApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
