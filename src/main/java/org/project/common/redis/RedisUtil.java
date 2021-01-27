package org.project.common.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.project.entity.UserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author YuSai
 */
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.113.191.24", 6379);
        jedis.auth("123456");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("liebgott");
        userEntity.setPassword("liebgott");
        userEntity.setSalt("liebgott");
        userEntity.setNickname("liebgott");
        userEntity.setEmail("liebgott");
        userEntity.setAvatar("liebgott");
        userEntity.setCreateTime(new Date());
        userEntity.setModifyTime(new Date());

        List<Object> list = new ArrayList<>();
        list.add(userEntity.getClass());
        list.add(userEntity);

        jedis.set("wxf", JSON.toJSONString(list));
        String value = jedis.get("wxf");
        System.out.println(value);
        //释放资源
        jedis.close();

    }

}
