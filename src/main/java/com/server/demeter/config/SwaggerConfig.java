package com.server.demeter.config;

import javax.swing.text.Document;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_12)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.server.demeter.resources"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(info());
    } 

    private ApiInfo info(){
        return new ApiInfoBuilder()
        .title("API Sever Demeter")
        .description("Documentação padrão do Demeter")
        .version("1.0.0")
        .license("Apache license Version 2.0")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
        .build();
    }


}