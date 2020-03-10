package com.cyz.login.common.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * redis的配置类
 * @author cyz
 *
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class) //当配置这个注解时，引入的配置了@ConfigurationProperties注解的类无需添加@Component注解也会被注入到IOC�?
public class RedisConfig extends CachingConfigurerSupport {
	
	@Autowired
	private RedisProperties properties;

	@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
		configuration.setPassword(RedisPassword.of(properties.getPassword()));
		configuration.setDatabase(properties.getDatabase());
		LettuceConnectionFactory f = new LettuceConnectionFactory(configuration);
		
        return f;
    }
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redis = new RedisTemplate<>();
		redis.setConnectionFactory(factory);
		redis.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		
		StringRedisSerializer serializer = new StringRedisSerializer();
		redis.setKeySerializer(serializer);
		redis.setHashKeySerializer(serializer);
		return redis;
	}
	
	/*@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate redis = new StringRedisTemplate(factory);
		
		return redis;
	}*/
	
	

	/*@Override
	public CacheErrorHandler errorHandler() {
		
		return new IgnoreExceptionCacheErrorHandler
	}

	*/
	
	

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CacheManager cacheManager() {
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问�?
        ObjectMapper om = new ObjectMapper();
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //下面代码解决LocalDateTime序列化与反序列化不一致问�?
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决jackson2无法反序列化LocalDateTime的问�?
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        
        
        
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                            .entryTtl(Duration.ZERO)
                                            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                                            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                                            .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(this.redisConnectionFactory()).cacheDefaults(config).build();
        return cacheManager;
	}

}
