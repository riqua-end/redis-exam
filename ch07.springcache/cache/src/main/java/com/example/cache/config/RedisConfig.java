package com.example.cache.config;

import com.example.cache.domain.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // User 객체와 Object 객체를 Redis 에 저장하고 읽어오는데 사용되는 Config

    @Bean
    RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // JSON 에서 알려지지 않은 속성이 있어도 예외를 발생시키지 않음
                .registerModule(new JavaTimeModule()) // JAVA8 날짜 및 시간에 대한 직렬화와 역직렬화
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS); // 날짜가 Timestamp 로 사용되지 않고 문자열 형식으로 직렬화

        var template = new RedisTemplate<String, User>(); // Redis 와 통신을 위한 클래스
        template.setConnectionFactory(redisConnectionFactory); // Redis 서버와 연결
        template.setKeySerializer(new StringRedisSerializer()); // Redis Key 의 직렬화
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class)); // User 객체를 Json 형식으로 직렬화

        return template;
    }

    @Bean
    RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator // 다형성 타입 검증
                .builder()
                .allowIfSubType(Object.class) // 모든 Object 타입의 서브타입을 허용
                .build();

        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL) // 기본 다형성 타이핑을 활성화 ,직렬화할 때 타입 정보를 포함시켜 다양한 서브 타입을 처리
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)); // objectMapper 를 사용해서  Json 형식으로 직렬화

        return template;
    }
}
