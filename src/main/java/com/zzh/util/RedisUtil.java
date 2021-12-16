package com.zzh.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ：zz
 * @date ：Created in 2021/12/14 15:28
 * @description：redis工具类
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * create by: zz
     * description: 指定缓存实效时间
     * @param: key
     * @param: time
     * @return boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.MINUTES);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 获取redis实效时间
     * @param: key
     * @return long  分，0代表永不失效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }

    /**
     * create by: zz
     * description: key是否存在
     * @param: key
     * @return boolean
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 删除redis（一个或多个）
     * @param: key
     * @return void
     */
    public void delRedis(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * create by: zz
     * description: 获取普通redis
     * @param: key
     * @return java.lang.Object
     */
    public Object getRedis(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * create by: zz
     * description: 获取字符串redis
     * @param: key
     * @return java.lang.Object
     */
    public String get(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * create by: zz
     * description: 存redis，永不失效
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean setRedis(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 存string
     * @param key
     * @param value
     * @return
     */
    public boolean setStringRedis(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zzh
     * description: 存redis
     * @param: key
     * @param: value
     * @param: time
     * @return boolean
     */
    public boolean setStringRedis(String key, String value, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setRedis(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 存redis
     * @param: key
     * @param: value
     * @param: time 分
     * @return boolean
     */
    public boolean setRedis(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setRedis(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: redis递增
     * @param: key
     * @param: delta
     * @return long
     */
    public long incrRedis(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * create by: zz
     * description: 递减
     * @param: key
     * @param: delta
     * @return long
     */
    public long decrRedis(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * create by: zz
     * description: 获取hash Redis
     * @param: key
     * @param: item
     * @return java.lang.Object
     */
    public Object hGet(String key, String item) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(item)){
            throw new RuntimeException("key和item不可为空");
        }
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * create by: zz
     * description: 获取hashKey对应的所有值
     * @param: key
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object, Object> hmGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * create by: zz
     * description: 存放hash Redis
     * @param: key
     * @param: map
     * @return boolean
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 存放Hash Redis并设置有效时间
     * @param: key
     * @param: map
     * @param: time 分
     * @return boolean
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 向一张hash表中放入数据,如果不存在将创建
     * @param: key
     * @param: item
     * @param: value
     * @return boolean
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 向一张hash表中放入数据,如果不存在将创建
     * @param: key
     * @param: item
     * @param: value
     * @param: time 分:如果已存在的hash表有时间,将会替换原有的时间
     * @return boolean
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 删除hash表中的值
     * @param: key
     * @param: item 可以多个
     * @return void
     */
    public void hdelRedis(String key, Object... item) {
        if(StringUtils.isEmpty(key) || item == null){
            throw new RuntimeException("删除失败，key或者item不可为空");
        }
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * create by: zz
     * description: hash表中是否有该项的值
     * @param: key
     * @param: item
     * @return boolean
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * create by: zz
     * description: hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param: key
     * @param: item
     * @param: by
     * @return double
     */
    public double hincrRedis(String key, String item, double by) {
        if(by <= 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * create by: zz
     * description: hash递减
     * @param: key
     * @param: item
     * @param: by
     * @return double
     */
    public double hdecrRedis(String key, String item, double by) {
        if(by <= 0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * create by: zz
     * description: 获取set集合所有值
     * @param: key
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * create by: zz
     * description: 查询set中value是否存在
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 存入set
     * @param: key
     * @param: values
     * @return long
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * create by: zz
     * description: 将数据放入set缓存
     * @param: key
     * @param: time 分
     * @param: values 可以多个
     * @return long
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * create by: zz
     * description: 获取set缓存长度
     * @param: key
     * @return long
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * create by: zz
     * description: 移除值为value的redis
     * @param: key
     * @param: values 可以是多个
     * @return long
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * create by: zz
     * description: 获取List中指定长度的缓存
     * @param: key
     * @param: start 开始
     * @param: end  结束 0 到 -1代表所有值
     * @return java.util.List<java.lang.Object>
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * create by: zz
     * description: 获取list长度
     * @param: key
     * @return long
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * create by: zz
     * description: 通过指定位置的值
     * @param: key
     * @param: index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return java.lang.Object
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * create by: zz
     * description: 将list存入缓存
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @param: time 分
     * @return boolean
     */
    public boolean lPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean lPush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @param: time
     * @return boolean
     */
    public boolean lPush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 将list存入缓存
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean rPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @param: time 分
     * @return boolean
     */
    public boolean rPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @return boolean
     */
    public boolean rPush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * create by: zz
     * description: 将List存入缓存
     * @param: key
     * @param: value
     * @param: time
     * @return boolean
     */
    public boolean rPush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 根据索引修改指定位置的值
     * @param: key
     * @param: index
     * @param: value
     * @return boolean
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * create by: zz
     * description: 移除指定数量的值为value
     * @param: key
     * @param: count
     * @param: value
     * @return long
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
