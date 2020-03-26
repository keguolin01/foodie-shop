package com.ikgl.controller;

import com.ikgl.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisTemplate;

    @GetMapping("get")
    public Object get(String key){
        String o = redisTemplate.get(key);
        return o;
    }

    @GetMapping("set")
    public Object set(String key,String value){
        redisTemplate.set(key,value);
        return "ok";
    }

    @GetMapping("delete")
    public Object delete(String key){
        redisTemplate.del(key);
        return "ok";
    }

//    @GetMapping("batchGet")
//    public List<String> batchGet(String... keys){
//        List<String> strings = new ArrayList<>();
//        for(String key : keys){
//            String s = redisTemplate.get(key);
//            strings.add(s);
//        }
//        return strings;
//    }
    @GetMapping("mget")
    public List<String> mget(String... keys){
        List<String> strings = Arrays.asList(keys);
        return redisTemplate.get(strings);
    }
    @GetMapping("batchGet")
    public List<Object> batchGet(String... keys){
        List<String> strings = Arrays.asList(keys);
        return redisTemplate.batchGet(strings);
    }
}
