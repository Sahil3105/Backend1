package com.codewithdurgesh.blog.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
public class SwagggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getInfo())
                .securityContexts(securityContexts())
                .securitySchemes(Arrays.asList(apiKeys()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKeys() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(SecurityContext.builder().securityReferences(sf()).build());
    }

    private List<SecurityReference> sf() {
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[]{scope}));
    }

    private ApiInfo getInfo() {
        return new ApiInfo("Blogging Application : Backend Course",
                "This project is developed by Learn Code With Durgesh",
                "1.0",
                "Terms of Service",
                new Contact("Durgesh", "https://learncodewithdurgesh.com", "learncodewithdurgesh@gmail.com"),
                "License of APIs",
                "API license URL",
                Collections.emptyList());
    }

    // ⚡ Compatibility Fix using Reflection
    @Autowired
    public void configureSpringfoxCompatibility(List<WebMvcRequestHandlerProvider> providers) {
        for (WebMvcRequestHandlerProvider provider : providers) {
            try {
                Field field = WebMvcRequestHandlerProvider.class.getDeclaredField("handlerMappings");
                field.setAccessible(true);
                List<RequestMappingHandlerMapping> mappings = (List<RequestMappingHandlerMapping>) field.get(provider);

                List<RequestMappingHandlerMapping> filteredMappings = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());

                mappings.clear();
                mappings.addAll(filteredMappings);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to configure Springfox compatibility", e);
            }
        }
    }
}

