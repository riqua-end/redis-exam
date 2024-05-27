package org.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        try (var jedisPool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // list
                // 1. stack
                /*
                jedis.rpush("stack1", "aaaa");
                jedis.rpush("stack1", "bbbb");
                jedis.rpush("stack1", "cccc");

                // rpop 은 rpush에서 가장 마지막에 저장된 데이터를 꺼내옴
                System.out.println(jedis.rpop("stack1")); // cccc
                System.out.println(jedis.rpop("stack1")); // bbbb
                System.out.println(jedis.rpop("stack1")); // aaaa

                List<String> stack1 = jedis.lrange("stack1", 0, -1);
                stack1.forEach(System.out::println);

                // 2. queue
                jedis.rpush("queue2","zzzz");
                jedis.rpush("queue2","dddd");
                jedis.rpush("queue2","eeee");

                // rpush 로 저장된 데이터 중 lpop은 가장 먼저 등록된 데이터를 꺼내옴
                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));
                */

                // 3. block brpop, blpop
//                while (true) {
//                    List<String> blpop = jedis.blpop(10, "queue:blocking");
//                    if (blpop != null) {
//                        blpop.forEach(System.out::println);
//                    }
//                }

                // Set

                jedis.sadd("users:500:follow","100","200","300");
                jedis.srem("users:500:follow","100");

                Set<String> smembers = jedis.smembers("users:500:follow");
                smembers.forEach(System.out::println);

                System.out.println(jedis.sismember("users:500:follow","200")); // true
                System.out.println(jedis.sismember("users:500:follow","120")); // false

                System.out.println(jedis.scard("users:500:follow")); // 2

                // sadd users:100:follow 200
                Set<String> sinter = jedis.sinter("users:500:follow", "users:100:follow");
                sinter.forEach(System.out::println); // 200
            }
        }
    }
}