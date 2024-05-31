package org.redis;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // geo add
                jedis.geoadd("stores:geo",126.84502512216567993,37.54230385810020465,"some3");
                jedis.geoadd("stores:geo",126.84047609567642212,37.54164483059877711,"some4");

                // geo dist
                Double geodist = jedis.geodist("stores:geo", "some3", "some4");
                System.out.println(geodist);

                // geo search , 반경 ~ M 인지 확인
                List<GeoRadiusResponse> geosearch = jedis.geosearch("stores:geo", new GeoCoordinate(126.843, 37.540), 300, GeoUnit.M);

                geosearch.forEach(res -> System.out.println(res.getMemberByString()));

                List<GeoRadiusResponse> geosearch1 = jedis.geosearch("stores:geo", new GeoSearchParam()
                        .fromLonLat(new GeoCoordinate(126.843, 37.540))
                        .byRadius(500, GeoUnit.M)
                        .withCoord()
                );

                geosearch1.forEach(res -> {
                    System.out.println("%s %f %f".formatted(
                            res.getMemberByString(),
                            res.getCoordinate().getLatitude(),
                            res.getCoordinate().getLongitude()
                    ));
                });

                List<GeoCoordinate> geopos = jedis.geopos("stores:geo","some3");

                geopos.forEach(it -> {
                    System.out.println("%s %f".formatted(it.getLatitude(),it.getLongitude()));
                });

                // key 삭제
                jedis.unlink("stores:geo");
            }
        }
    }
}