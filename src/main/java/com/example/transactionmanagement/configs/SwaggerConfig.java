package com.example.transactionmanagement.configs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.transactionmanagement.utils.Constants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration class for Swagger.
 * This class configures Swagger for API documentation in the application.
 */
@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    /**
     * Configures the Swagger Docket bean.
     * 
     * @return a configured Docket instance for Swagger 2 documentation.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(Constants.BASE_PACKAGE))
                .paths(PathSelectors.regex(Constants.PATH_REGEX))
                .build().apiInfo(apiInfoMetaData());
    }

    /**
     * Provides API information for Swagger.
     * 
     * @return an ApiInfo instance containing metadata for the API.
     */
    private ApiInfo apiInfoMetaData() {
        return new ApiInfoBuilder()
                .title(Constants.API_TITLE)
                .description(Constants.API_DESCRIPTION)
                .version(Constants.API_VERSION)
                .build();
    }
}