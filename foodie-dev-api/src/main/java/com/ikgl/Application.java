package com.ikgl;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEncryptableProperties
//(exclude = {SecurityAutoConfiguration.class})
//@EnableAutoConfiguration
//扫描mybatis通用mapper所在的包
@MapperScan(basePackages = "com.ikgl.mapper")
//扫描所有包以及相关组件包
@ComponentScan(basePackages={"org.n3r.idworker","com.ikgl"})
//@ImportResource("classpath*:redisson.xml") 结合spring做法
//@EnableRedisHttpSession // 开始redis 作为spring session
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
