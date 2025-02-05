package io.devarium.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${springdoc.version}")
    private String springdocVersion;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .title(applicationName + " API Documentation")
            .version(springdocVersion)
            .description("API 문서")
            .license(new License()
                .name("Apache License Version 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0"));

        // API 서버 정보 추가
        Server devServer = new Server()
            .url("http://localhost:8080")
            .description("Development Server");

        Server prodServer = new Server()
            .url("https://api.devarium.io")
            .description("Production Server");

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        // Security 요구사항 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .info(info)
            .addServersItem(devServer)
            .addServersItem(prodServer)
            .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))
            .addSecurityItem(securityRequirement);
    }
}
