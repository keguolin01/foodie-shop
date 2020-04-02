package com.ikgl.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RedisLock implements AutoCloseable {

    private RedisTemplate redisTemplate;
    private String key;
    private String value;
    //单位：秒
    private int expireTime;

    public RedisLock(RedisTemplate redisTemplate, String key, int expireTime) throws InterruptedException {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime=expireTime;
        this.value = UUID.randomUUID().toString();
    }

    /**
     * 获取分布式锁
     * @return
     */
    public boolean getLock(){
        RedisCallback<Boolean> redisCallback = connection -> {
            //设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(expireTime);
            //序列化key
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            //序列化value
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            //执行setnx操作
            Boolean result = connection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };

        //获取分布式锁
        Boolean lock = (Boolean)redisTemplate.execute(redisCallback);
        return lock;
    }
    // 缺点 不支持阻塞 就是并发下单的时候 后面那位不会等待前面那个释放锁，直接执行后面的
    public boolean unLock() {
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script,Boolean.class);
        List<String> keys = Arrays.asList(key);

        Boolean result = (Boolean)redisTemplate.execute(redisScript, keys, value);
        log.info("释放锁的结果："+result);
        if(!result){
            log.info("前面进程未释放锁，请再次请求");
        }
        return result;
    }


    @Override
    public void close() throws Exception {
        unLock();
    }
}
