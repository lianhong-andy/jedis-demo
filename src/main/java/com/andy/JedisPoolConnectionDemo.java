package com.andy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisPoolConnectionDemo {
    private static final String HOST = "49.234.41.101";
    private static final int PORT = 6382;
    private JedisPool jedisPool;

    @Before
    public void init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxWaitMillis(1000);
        jedisPool = new JedisPool(poolConfig, HOST, PORT);

    }

    @Test
    public void testString() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set("hello","world");
            String hello = jedis.get("hello");
            System.out.println("hello = " + hello);

            jedis.set("counter","1");
            String counter = jedis.get("counter");
            System.out.println("counter = " + counter);
            jedis.incr("counter");
            System.out.println("counter = " + jedis.get("counter"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//将链接归还给连接池
            }
        }
    }

    @Test
    public void testHash() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists("myhash")) {
                jedis.del("myhash");
            }
            jedis.hset("myhash","k1","v1");
            jedis.hset("myhash","k2","v2");
            Map<String, String> myhash = jedis.hgetAll("myhash");
            System.out.println("myhash = " + myhash);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//将链接归还给连接池
            }
        }

    }

    @Test
    public void testList() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists("mylist")) {
                jedis.del("mylist");
            }
            jedis.rpush("mylist","1");
            jedis.rpush("mylist","2");
            jedis.rpush("mylist","3");
            jedis.rpush("mylist","4");
            List<String> mylist = jedis.lrange("mylist", 0, -1);
            System.out.println("mylist = " + mylist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//将链接归还给连接池
            }
        }
    }

    @Test
    public void testSet() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists("myset")) {
                jedis.del("myset");
            }
            jedis.sadd("myset","a");
            jedis.sadd("myset","b");
            jedis.sadd("myset","c");
            jedis.sadd("myset","d");
            Set<String> myset = jedis.smembers("myset");
            System.out.println("myset = " + myset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//将链接归还给连接池
            }
        }
    }

    @Test
    public void testZset() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//将链接归还给连接池
            }
        }
    }

    /**测试链接不是放耗尽问题*/
    @Test
    public void testErr() {
        Jedis jedis = null;
        try {
            for (int i = 0; i < 10; i++) {
                jedis = jedisPool.getResource();
                jedis.ping();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jedis = jedisPool.getResource();
    }

}
