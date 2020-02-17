package cn.roboteco.springbootstarter.config;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

@Configuration
@PropertySources({ @PropertySource(value = "classpath:config.properties") })
public class PropertyConfig {

    final static int[] SIZE_TABLE = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
            Integer.MAX_VALUE };

    final static String PRE = "000000000";

    static int sizeOfInt(int x) {
        for (int i = 0;; i++)
            if (x <= SIZE_TABLE[i])
                return i + 1;
    }

    @Autowired
    private Environment env;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public String getRedisVal(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (null != obj) {
            return String.valueOf(obj);
        }
        return null;
    }

    public void sendRedisMQ(String ip, String msg) {
        redisTemplate.convertAndSend(ip, msg);
    }

    public void setRedisVal(String key, String val, long seconds) {
        redisTemplate.opsForValue().set(key, val, seconds, TimeUnit.SECONDS);
    }

    public void setMonthRedisVal(String key, String val) {
        Date nowDate = DateUtil.date().toJdkDate();
        Date endDate = DateUtil.endOfMonth(nowDate).toJdkDate();
        long betweenSeconds = DateUtil.between(nowDate, endDate, DateUnit.SECOND);
        redisTemplate.opsForValue().set(key, val, betweenSeconds, TimeUnit.SECONDS);// 自然月底失效
    }

    /**
     * 存储数据或修改数据
     * 
     * @param modelMap
     * @param key
     */
    public void setMinutesRedisMap(String key, Map<String, Object> modelMap) {
        HashOperations<String, String, Object> hps = redisTemplate.opsForHash();
        hps.putAll(key, modelMap);
        redisTemplate.expire(key, 3, TimeUnit.MINUTES);
    }

    /**
     * 获取数据Map
     * 
     * @param key
     * @return
     */
    public Map<String, Object> getRedisMap(String key) {
        HashOperations<String, String, Object> hps = redisTemplate.opsForHash();
        return hps.entries(key);
    }

    public void delRedisVal(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获取数据value
     * 
     * @param mapName
     * @param hashKey
     * @return
     */
    public Object getHashValue(String mapName, String hashKey) {
        HashOperations<String, String, Object> hps = redisTemplate.opsForHash();
        return hps.get(mapName, hashKey);
    }

    /**
     * 批量删除缓存数据
     * 
     * @param keys
     */
    public void batchDelData(List<String> keys) {
        // 执行批量删除操作时先序列化template
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.delete(keys);
    }

    public long incrId(String key) {
        long ins = redisTemplate.opsForValue().increment(key);
        if (ins == 1) {
            Date nowDate = DateUtil.date().toJdkDate();
            Date endDate = DateUtil.endOfDay(nowDate).toJdkDate();
            long betweenSeconds = DateUtil.between(nowDate, endDate, DateUnit.SECOND);
            redisTemplate.expire(key, betweenSeconds, TimeUnit.SECONDS);
        }
        return ins;
    }

    public long incr(String key, long by, long seconds) {
        long ins = redisTemplate.opsForValue().increment(key, by);
        if (seconds > 0 && ins == 1) {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return ins;
    }

}
