package com.ikgl.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
}
