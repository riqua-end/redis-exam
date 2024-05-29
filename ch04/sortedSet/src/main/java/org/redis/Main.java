package org.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;
import redis.clients.jedis.util.KeyValue;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // sorted set
                HashMap<String, Double> scores = new HashMap<>();
                scores.put("users1",100.0);
                scores.put("users2",30.0);
                scores.put("users3",50.0);
                scores.put("users4",70.0);
                scores.put("users5",15.0);

                jedis.zadd("game2:scores", scores);

                List<String> zrange = jedis.zrange("game2:scores", 0, Long.MAX_VALUE);
                zrange.forEach(System.out::println);

//                List<Tuple> tuples = jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE);
//                tuples.forEach(System.out::println);

                long zcard = jedis.zcard("game2:scores");
                System.out.println(zcard);

                jedis.zincrby("game2:scores",500, "users5");

//                List<Tuple> tuples = jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE);
//                tuples.forEach(i -> System.out.println("%s %f".formatted(i.getElement(), i.getScore())));

                List<Tuple> tuples1 = jedis.zrevrangeWithScores("game2:scores", 0, Long.MAX_VALUE);
                tuples1.forEach(i -> System.out.println("%s %f".formatted(i.getElement(), i.getScore())));

            }
        }
    }
}