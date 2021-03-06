package com.flockit.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        /*.paths(Predicates.not(PathSelectors.regex("/error")))*/
        .build()
        .apiInfo(metadata())
        .useDefaultResponseMessages(false)
        .securitySchemes(new ArrayList<>(Arrays.asList(new ApiKey("Bearer %token", "Authorization", "Header"))))
        .tags(new Tag("users", "Operations about users"))
        .tags(new Tag("access", "Operations about login"))
        .tags(new Tag("external", "Operations about external apis"))
        .genericModelSubstitutes(Optional.class);
  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title("Challenge")
        .description(".")
        .version("1.0.0")
        .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")
        .contact(new Contact("Alejandro Pariz", null, "challenge@mycompany.com"))
        .build();
  }
  
}
