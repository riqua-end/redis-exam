package com.example.cache.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching // Spring cache 기능을 활성화하는 어노테이션
public class CacheConfig {
    // 캐시 이름을 상수로 정의
    public static final String CACHE1 = "cache1";
    public static final String CACHE2 = "cache2";

    @AllArgsConstructor
    @Getter
    public static class CacheProperty { // 캐시 설정 정보를 담는 클래스
        private String name;
        private Integer ttl;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator // 다형성 타입 검증
                .builder()
                .allowIfSubType(Object.class) // 모든 Object 타입의 서브타입을 허용
                .build();

        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL) // 기본 다형성 타이핑을 활성화 ,직렬화할 때 타입 정보를 포함시켜 다양한 서브 타입을 처리
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        // 캐시 설정 정보 리스트 생성 ( 이름 , 유효시간 )
        List<CacheProperty> properties = List.of(
                new CacheProperty(CACHE1, 300),
                new CacheProperty(CACHE2, 30)
        );

        return (builder -> {
            properties.forEach(i -> {
                builder.withCacheConfiguration(i.getName(), RedisCacheConfiguration
                        .defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                        .entryTtl(Duration.ofSeconds(i.getTtl())));
            });
        });

        /*
         * forEach loop 를 통해 각 캐시 설정 정보 적용
         * withCacheConfiguration: 캐시 설정 정보 적용 메서드
         * defaultCacheConfig(): 기본 캐시 설정
         * disableCachingNullValues: null 값 캐싱 방지
         * serializeKeysWith: 키 직렬화 설정 (StringRedisSerializer 사용)
         * serializeValuesWith: 값 직렬화 설정 (GenericJackson2JsonRedisSerializer 사용 - ObjectMapper 객체 전달)
         * entryTtl: 캐시 유효 시간 설정 (Duration 클래스 사용)
         */
    }
}
