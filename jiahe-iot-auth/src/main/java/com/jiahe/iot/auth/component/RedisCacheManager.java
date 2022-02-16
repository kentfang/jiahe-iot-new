package com.jiahe.iot.auth.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisCacheManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    public void set(String key, String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, value.toString());
    }

    public Boolean setIfAbsent(String key, Object value, long expiredTime) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value.toString(), expiredTime, TimeUnit.SECONDS);
    }

    public String hget(String key, Object prop) {
        Object v = stringRedisTemplate.opsForHash().get(key, prop);
        if (v != null) {
            return v.toString();
        }
        return null;
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void hset(String key, Object prop, Object value) {
        stringRedisTemplate.opsForHash().put(key, prop, value);
    }

    public void sset(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public Set<String> smembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public void sremove(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    public boolean sisMember(String key, String value) {
        Boolean r = stringRedisTemplate.opsForSet().isMember(key, value);
        return r != null && r;
    }

    public void lpush(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public String lpop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    public void rpush(String key, String value) {
        stringRedisTemplate.opsForList().rightPush(key, value);
    }

    public String rpop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    public String listIndex(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    public void expire(String key, int seconds) {
        stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

}
