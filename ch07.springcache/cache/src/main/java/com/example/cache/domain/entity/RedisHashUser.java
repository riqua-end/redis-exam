package com.example.cache.domain.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash(value = "redishash-user", timeToLive = 30)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RedisHashUser {

    @Id
    private Long id;

    private String name;

    @Indexed
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
