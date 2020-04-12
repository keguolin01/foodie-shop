package com.ikgl.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfig {
//    @Bean
//    public TransportClient esClient() throws UnknownHostException {
//        Settings settings = Settings.builder()
//                .put("cluster.name", "kgl-cluster")
//				.put("client.transport.sniff", false) // 自动发现节点
//                .build();
//
//        TransportAddress master = new TransportAddress(
//                InetAddress.getByName("47.98.135.169"),9300
//        );
//
//        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(master);
//        return client;
//    }

}
