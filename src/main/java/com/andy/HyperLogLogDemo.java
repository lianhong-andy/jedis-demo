package com.andy;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * HyperLogLog数据结构，实质是String类型，删除的时候可以用del命令
 */
public class HyperLogLogDemo {

    private static final String HOST = "49.234.41.101";
    private static final int PORT = 6382;
    private static final String DATE_UUID_20170305 = "2017_03_05:unique:ids";
    private static final String DATE_UUID_20170306 = "2017_03_06:unique:ids";
    private static final String DATE_UUID_20170305_06 = "2017_03_05_06:unique:ids";
    private static final String UUID = "uuid-";

    private static String getUUID(int i) {
        return UUID+i;
    }


    /**
     * 添加操作
     */
    @Test
    public void testPfAdd() {
        Jedis jedis = new Jedis(HOST,PORT);
        jedis.pfadd(DATE_UUID_20170305,getUUID(1),getUUID(2),getUUID(3),getUUID(4));
        jedis.pfadd(DATE_UUID_20170306,getUUID(4),getUUID(5),getUUID(6),getUUID(7));
    }

    /**
     * 计数操作
     */
    @Test
    public void testPfCount() {
        Jedis jedis = new Jedis(HOST,PORT);
        long pfcount = jedis.pfcount(DATE_UUID_20170305);
        System.out.println("pfcount = " + pfcount);//4
        pfcount = jedis.pfcount(DATE_UUID_20170306);
        System.out.println("pfcount = " + pfcount);//4
        pfcount = jedis.pfcount(DATE_UUID_20170305, DATE_UUID_20170306);//求和计数
        System.out.println("pfcount = " + pfcount);//7
    }

    /**
     * 合并操作
     */
    @Test
    public void testPfMerge() {
        Jedis jedis = new Jedis(HOST,PORT);
        String pfmerge = jedis.pfmerge(DATE_UUID_20170305_06, DATE_UUID_20170305, DATE_UUID_20170306);
        System.out.println("pfmerge = " + pfmerge);
        long pfcount = jedis.pfcount(DATE_UUID_20170305_06);
        System.out.println("pfcount = " + pfcount);
    }


}
