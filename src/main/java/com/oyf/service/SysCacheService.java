package com.oyf.service;

import com.oyf.beans.CacheKeyPrefix;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * Create Time: 2019年03月30日 11:16
 * Create Author: 欧阳飞
 **/

@Service
@Slf4j
    public class SysCacheService {

    @Resource
    private RedisPoolService redisPoolService;

    //写缓存
    public void saveIntoCache(String toSaveValue, int timeoutSeconds, String key, CacheKeyPrefix prefix){
        if (StringUtils.isBlank(toSaveValue)){
            return;
        }
        //缓存值的key
        String cacheKey = key + "_" + prefix;
        ShardedJedis jedis = null;
        try {
            //获取jedis对象
            jedis = redisPoolService.getJedis();
            jedis.setex(cacheKey,timeoutSeconds,toSaveValue);
        }catch (Exception e){
            log.error("redis save exception:{}",e.getMessage());
            log.error("prefix:{},key{}",prefix.name(),key);
        }finally {
            redisPoolService.close(jedis);
        }

    }

    //读缓存
    public String getFromCache(String key, CacheKeyPrefix prefix){
        //获取key
        String cacheKey = key + "_" + prefix;
        ShardedJedis jedis = null;
        String value = null;
        try{
            jedis = redisPoolService.getJedis();
            value = jedis.get(cacheKey);
        }catch (Exception e){
            log.error("redis read exception:{}",e.getMessage());
            log.error("prefix:{},key{}",prefix.name(),key);
        }finally {
            redisPoolService.close(jedis);
        }

        return value;
    }

}
