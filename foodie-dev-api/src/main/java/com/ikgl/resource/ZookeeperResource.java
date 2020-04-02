package com.ikgl.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper")
@PropertySource("classpath:config.properties")
@Data
public class ZookeeperResource {
    private String addr;
}
