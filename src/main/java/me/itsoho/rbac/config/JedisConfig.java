package me.itsoho.rbac.config;

import me.itsoho.shiro.cluster.JedisManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConfigurationProperties(prefix="jdeis.pool")
public class JedisConfig {
	private String host = "127.0.0.1";
	private int port = 6379;
	private int timeout = 2000;
	private String password = "123qwe";
	
	@Bean
	@ConfigurationProperties(prefix="jdeis.pool.config")
	public JedisPoolConfig jedisPoolConfig(){
		return new JedisPoolConfig();
	}
	
//	@Bean
//	public JedisPool jedisPool(JedisPoolConfig poolConfig){
//		return new JedisPool( poolConfig,  host,  port, timeout, password);
//	}
	
	@Bean
	@Autowired
	public JedisManager jedisManager(JedisPoolConfig poolConfig){
		JedisManager ret = new JedisManager();
		ret.setJedisPool(new JedisPool( poolConfig,  host,  port, timeout, password));
		return ret;
	}
}
