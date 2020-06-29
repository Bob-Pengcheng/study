package Redis;

import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yd
 * @date 2020/1/8 1:42 下午
 */
public class RedisClient {
    private static final String KEY = "test";
    private static final String DEV_ID = "dev_test";
    public static void main(String[] args){


        Jedis jedis = new Jedis("localhost");
        System.out.println("************ REDIS CONNECTED");

        jedis.set(KEY,"Hello Redis");

        System.out.println("************ get value:" + jedis.get(KEY));


        Map<String, String> mapHash = new LinkedHashMap<String,String>();
        mapHash.put("dp1","dp1");
        mapHash.put("dp2","dp2");
        mapHash.put("dp3","dp3");
        mapHash.put("dp4","dp4");

        jedis.hmset(DEV_ID,mapHash);

        String value = jedis.hget(DEV_ID,"dp1");
        System.out.println("************ get value:" + value);
    }
}
