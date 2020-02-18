package com.spring.boot.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final Contact CONTACT = new Contact("Giancarlo Yarleque", "https://github.com/Giancarlo2709", "giancarlo2709@gmail.com");
    public static final ApiInfo API_INFO = new ApiInfo("Programming Reactive With RxJava", "Programming Reactive With RxJava", "1.0",
            "Terms of Service", CONTACT, "Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");

    @Bean
    public Docket apiDocument(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spring.boot.reactive"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(API_INFO);
    }
}
