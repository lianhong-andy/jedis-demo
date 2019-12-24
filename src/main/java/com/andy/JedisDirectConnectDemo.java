package com.andy;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisDirectConnectDemo {
    private static final String HOST = "49.234.41.101";
    private static final int PORT = 6382;
    @Test
    public void testString() {
        Jedis jedis = new Jedis(HOST,PORT);
        jedis.set("hello","world");
        String hello = jedis.get("hello");
        System.out.println("hello = " + hello);

        jedis.set("counter","1");
        String counter = jedis.get("counter");
        System.out.println("counter = " + counter);
        jedis.incr("counter");
        System.out.println("counter = " + jedis.get("counter"));
    }

    @Test
    public void testHash() {
        Jedis jedis = new Jedis(HOST,PORT);
        jedis.hset("myhash","k1","v1");
        jedis.hset("myhash","k2","v2");
        Map<String, String> myhash = jedis.hgetAll("myhash");
        System.out.println("myhash = " + myhash);

    }

    @Test
    public void testList() {
        Jedis jedis = new Jedis(HOST,PORT);
        jedis.rpush("mylist","1");
        jedis.rpush("mylist","2");
        jedis.rpush("mylist","3");
        jedis.rpush("mylist","4");
        List<String> mylist = jedis.lrange("mylist", 0, -1);
        System.out.println("mylist = " + mylist);
    }

    @Test
    public void testSet() {
        Jedis jedis = new Jedis(HOST,PORT);
        jedis.sadd("myset","a");
        jedis.sadd("myset","b");
        jedis.sadd("myset","c");
        jedis.sadd("myset","d");
        Set<String> myset = jedis.smembers("myset");
        System.out.println("myset = " + myset);
    }

    @Test
    public void testZset() {
        Jedis jedis = new Jedis(HOST,PORT);
        if (jedis.exists("myzset")) {
            jedis.del("myzset");
        }
        jedis.zadd("myzset",100,"James");
        jedis.zadd("myzset",90,"Bill");
        jedis.zadd("myzset",80,"Kelly");
        jedis.zadd("myzset",70,"Andy");
        Set<Tuple> myzset = jedis.zrangeWithScores("myzset", 0, -1);
        for (Tuple tuple : myzset) {
            String element = tuple.getElement();
            double score = tuple.getScore();
            System.out.println("element = " + element+",score="+score);
        }
    }

}
