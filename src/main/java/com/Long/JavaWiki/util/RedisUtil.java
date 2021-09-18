package com.Long.JavaWiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * true：不存在，放一个KEY
     * false：已存在
     */
    public boolean validateRepeat(String key, long hour) {
        if (stringRedisTemplate.hasKey(key)) {
            LOG.info("key已存在:{}", key);
            return false;
        } else {
            LOG.info("key不存在,放入:{}, 有效时间: {} 小时", key, hour);
            stringRedisTemplate.opsForValue().set(key, key, hour, TimeUnit.HOURS);
            return true;
        }
    }
}
