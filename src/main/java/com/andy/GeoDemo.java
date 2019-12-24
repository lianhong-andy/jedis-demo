package com.andy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.junit.Test;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;

public class GeoDemo {

    private static final String HOST = "49.234.41.101";
    private static final int PORT = 6382;
    final static String GEOKEY = "cities:locations";
    final static List<JGeo> geoList = CollectionUtil.newArrayList();

    static {
        geoList.add(new JGeo("beijing",116.28, 39.55));
        geoList.add(new JGeo("tianjing",117.12, 39.08));
        geoList.add(new JGeo("shijiazhuang",114.28, 38.02));
        geoList.add(new JGeo("tangshan",118.01, 39.38));
        geoList.add(new JGeo("baoding",115.29, 38.51));
    }


    @Test
    public void testGeoAdd() {
        Jedis jedis = new Jedis(HOST,PORT);
        if (CollectionUtil.isNotEmpty(geoList)) {
            for (JGeo geo : geoList) {
                jedis.geoadd(GEOKEY,geo.getLongitude(),geo.getLatitude(),geo.getMember());
            }
        }
    }

    /**
     * 获取各个做坐标
     */
    @Test
    public void testGeoPos() {
        Jedis jedis = new Jedis(HOST,PORT);
        /*if (CollectionUtil.isNotEmpty(geoList)) {
            geoList.forEach((JGeo geo) ->{
                List<GeoCoordinate> geopos = jedis.geopos(GEOKEY, geo.getMember());
                System.out.println("StrUtil.toString(geopos) = " + StrUtil.toString(geopos));
            });
        }*/
        /**
         * StrUtil.toString(geopos) = [(116.28000229597092,39.55000072454708)]
         * StrUtil.toString(geopos) = [(117.12000042200089,39.080000053576654)]
         * StrUtil.toString(geopos) = [(114.2800024151802,38.019999942510374)]
         * StrUtil.toString(geopos) = [(118.01000028848648,39.37999951111137)]
         * StrUtil.toString(geopos) = [(115.28999894857407,38.50999956342799)]
         *
         * */
    }

    /**
     * 计算两地距离
     */
    @Test
    public void testGeoDist() {
        Jedis jedis = new Jedis(HOST,PORT);
        String bj = "beijing";
        String tj = "tianjing";
        Double geodist = jedis.geodist(GEOKEY, bj, tj);
        System.out.println("geodist = " + geodist);//geodist = 89206.0576

    }

    /**
     * 查询某个范围内的坐标
     */
    @Test
    public void testGeoRadius() {
        Jedis jedis = new Jedis(HOST,PORT);
        String bj = "beijing";
        GeoRadiusParam params = GeoRadiusParam.geoRadiusParam().withCoord().withDist().sortDescending();
        List<GeoRadiusResponse> geoRadiusResponses = jedis.georadiusByMember(GEOKEY, bj, 150, GeoUnit.KM, params);
        if (CollectionUtil.isNotEmpty(geoRadiusResponses)) {
            String s = JSONUtil.toJsonStr(geoRadiusResponses);
            System.out.println("s = " + s);
        }
        /**
         * [
         *     {
         *         "coordinate":{
         *             "latitude":39.37999951111137,
         *             "longitude":118.01000028848648
         *         },
         *         "distance":149.7479,
         *         "member":Array[8]
         *     },
         *     {
         *         "coordinate":{
         *             "latitude":38.50999956342799,
         *             "longitude":115.28999894857407
         *         },
         *         "distance":143.8646,
         *         "member":Array[7]
         *     },
         *     {
         *         "coordinate":{
         *             "latitude":39.080000053576654,
         *             "longitude":117.12000042200089
         *         },
         *         "distance":89.2061,
         *         "member":Array[8]
         *     },
         *     {
         *         "coordinate":{
         *             "latitude":39.55000072454708,
         *             "longitude":116.28000229597092
         *         },
         *         "distance":0,
         *         "member":Array[7]
         *     }
         * ]
         * */
    }

}
