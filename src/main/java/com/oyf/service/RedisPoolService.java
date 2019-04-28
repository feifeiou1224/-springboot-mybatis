package com.oyf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Create Time: 2019年03月30日 11:08
 * Create Author: 欧阳飞
 **/

@Service
@Slf4j
public class RedisPoolService {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    //从连接池中获取Jedis对象
    public ShardedJedis getJedis(){
        return shardedJedisPool.getResource();
    }

    //将Jedis对象归还给连接池
    public void close(ShardedJedis jedis){
        try {
            if (jedis != null)jedis.close();
        }catch (Exception e){
            log.error("jedis close exception:{}",e.getMessage());
        }
    }


}
