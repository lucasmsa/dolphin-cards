package br.com.dolphinCards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Dolphin Cards 🐬")
        .description("Flash cards application, making discipline's contents a lot easier to remember by working with individualized cards and smartly setting new dates to answer them 🃏")
        .license("MIT")
        .licenseUrl("https://opensource.org/licenses/MIT")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("Lucas Moreira", "https://github.com/lucasmsa", "lmsa.moreira@gmail.com"))
        .build();
  }

  @Bean
  public Docket swagger() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("br.com.dolphinCards"))
        .paths(PathSelectors.any()).build().apiInfo(apiInfo());
  }
}