package io.devarium.api.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "ProfileLogger")
public class ProfileLogger {

    private final Environment env;

    @PostConstruct
    public void logActiveProfiles() {
        log.info("Active profiles: {}", String.join(", ", env.getActiveProfiles()));
    }
}
