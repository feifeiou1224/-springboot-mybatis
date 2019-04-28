package com.oyf.config;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

/**
 * Create Time: 2019年04月27日 15:26
 * Create Author: 欧阳飞
 **/

@Configuration
@EnableConfigurationProperties(JedisConfig.class)
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "redis")
@Data
public class JedisConfig {

    private String host;
    private int port;
    private int timeout;

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    }

    @Bean
    public JedisShardInfo jedisShardInfo(){
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host,port,timeout);
        return jedisShardInfo;
    }

    @Bean(value = "shardedJedisPool")
    public ShardedJedisPool shardedJedisPool(){
        ArrayList<JedisShardInfo> jedisShardInfos = Lists.newArrayList(jedisShardInfo());
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig(),jedisShardInfos);
        return shardedJedisPool;
    }
}
