package com.ikgl.config;

import com.ikgl.resource.ZookeeperResource;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ZookeeperResource zookeeperResource;

    @Bean
    public RestTemplate initRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    //实现静态资源的映射 swagger包之前会自动映射  如果设置了这边需要手动映射一下
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file:" + File.separator + "workspaces"
                + File.separator + "foodie"
                + File.separator + "images"
                + File.separator + "faces" + File.separator;
             //映射本地资源
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/META-INF/resources/")
                    .addResourceLocations(path);//映射本地静态资源
    }

    @Bean(initMethod="start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperResource.getAddr(), retryPolicy);
        return client;
    }
}
