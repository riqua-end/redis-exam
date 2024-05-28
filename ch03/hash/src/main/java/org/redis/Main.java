package org.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                // hashset
                jedis.hset("users:2:info","name","kj1");

                // Hashmap 을 이용한 방법
                var userInfo = new HashMap<String, String>();
                userInfo.put("email","kj1@naver.com");
                userInfo.put("phone","010-1234-1234");

                jedis.hset("users:2:info", userInfo);

                // hash del
                jedis.hdel("users:2:info", "phone");

                // get, mget
                String email = jedis.hget("users:2:info", "email");
                System.out.println(email);

                Map<String, String> getAll = jedis.hgetAll("users:2:info");
//                System.out.println(getAll);
                getAll.forEach((k,v) -> System.out.printf("%s %s%n", k,v));

                // hincrby
                jedis.hincrBy("users:2:info","visit", 10);
            }
        }
    }
}