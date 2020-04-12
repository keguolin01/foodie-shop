package com.ikgl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration//为了可以被扫描
@EnableSwagger2//开启配置
public class Swagger2 {

    //http://localhost:8088/swagger-ui.html

    //http://localhost:8088/doc.html
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定api类型是SWAGGER_2
                .apiInfo(apiInfo())                     //用户定义api文档
                .select().apis(RequestHandlerSelectors.basePackage("com.ikgl.controller"))//指定controller包
                .paths(PathSelectors.any())//扫描全部
                .build();
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("kgl的天天吃货")//标题
                .description("专为天天吃货提供的api文档")
                .version("1.0.1")
                .contact(new Contact("kgl","http://47.98.138.56:90/","987810481@qq.com"))
                .termsOfServiceUrl("http://47.98.138.56:90/")
                .build();
    }
}
