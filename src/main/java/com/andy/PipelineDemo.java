package com.andy;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelineDemo {

    private static final String HOST = "49.234.41.101";
    private static final int PORT = 6382;


    @Test
    public void testBatchComd() {
        Jedis jedis = new Jedis(HOST,PORT);
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            jedis.hset("hashKey","field"+i,"value"+i);
        }
        System.out.println("System.currentTimeMillis()-l = " + (System.currentTimeMillis() - l));//System.currentTimeMillis()-l = 30150
    }

    @Test
    public void testPipelineComd() {
        Jedis jedis = new Jedis(HOST,PORT);
        long l = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 1000; i++) {
            pipelined.hset("hashKey-pipeline","field-pipeline"+i,"value-pipeline"+i);
        }
        pipelined.syncAndReturnAll();
        System.out.println("System.currentTimeMillis()-l = " + (System.currentTimeMillis() - l));//System.currentTimeMillis()-l = 316

    }
}
