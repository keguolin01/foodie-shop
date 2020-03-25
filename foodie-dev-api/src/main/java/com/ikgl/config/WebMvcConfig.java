package com.ikgl.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebMvcConfig {

    @Bean
    public RestTemplate initRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
