package org.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

//                jedis.set("users:300:email","kim@fastcampus.com");
//                jedis.set("users:300:name","kim");
//                jedis.set("users:300:age","10");
//
//
//                var userEmail = jedis.get("users:300:email");
//                System.out.println(userEmail);
//
//                List<String> userInfo = jedis.mget("users:300:email", "users:300:name", "users:300:age");
//                userInfo.forEach(System.out::println);
//
//                long counter = jedis.incr("counter");
//                System.out.println(counter);
//
//                long counter = jedis.incrBy("counter", 10L);
//                System.out.println(counter);
//
//                long counter = jedis.decr("counter");
//                System.out.println(counter);
//
//                long counter = jedis.decrBy("counter", 10L);
//                System.out.println(counter);


                // Pipeline 은 데이터를 한번에 묶어서 처리 가능
                Pipeline pipelined = jedis.pipelined();
                pipelined.set("users:400:email", "greg@naver.com");
                pipelined.set("users:400:name","greg");
                pipelined.set("users:400:age","30");
                List<Object> objects = pipelined.syncAndReturnAll();
                objects.forEach(i-> System.out.println(i.toString()));
            }
        }

    }
}