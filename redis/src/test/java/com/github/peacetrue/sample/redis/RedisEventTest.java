package com.github.peacetrue.sample.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.List;

/**
 * @author : xiayx
 * @since : 2021-03-15 17:28
 **/
public class RedisEventTest {

    public static class KeyExpiredListener extends JedisPubSub {
        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
            System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            System.out.println("onPMessage pattern " + pattern + " " + channel + " " + message);
        }
    }

    @Test
    void subscribe() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = pool.getResource();
        String parameter = "notify-keyspace-events";
        List<String> notify = jedis.configGet(parameter);

        if (notify.get(1).equals("")) {
            jedis.configSet(parameter, "KEA");
        }

        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

    }

    @Test
    void push() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = pool.getResource();
        jedis.set("notify", "umq");
        jedis.expire("notify", 10);
    }
}
