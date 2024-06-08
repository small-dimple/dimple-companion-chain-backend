package com.smlDimple.dimpleCompanionChain.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 具体看官方文档
 * Knife4j在线API接口文档配置
 *
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Profile("prod")//这里可以指定什么环境这个Bean生效，在配置文件中可以指定默认的环境
public class Knife4jConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的路径
                .apis(RequestHandlerSelectors.basePackage("com.smlDimple.dimpleCompanionChain.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("knife4j在线API接口文档")
                .contact(new Contact("Roc-xb", "https://yang-roc.blog.csdn.net/", "aida_pc@qq.com"))
                .version("v3.0.0")
                .title("knife4j在线API接口文档")
                .build();
    }

}