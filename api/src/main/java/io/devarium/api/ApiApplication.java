package io.devarium.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@EntityScan(basePackages = "io.devarium.infrastructure.persistence")
@ComponentScan(basePackages = {
    "io.devarium.api",
    "io.devarium.core",
    "io.devarium.infrastructure"
})
@EnableJpaRepositories(basePackages = "io.devarium.infrastructure.persistence")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
