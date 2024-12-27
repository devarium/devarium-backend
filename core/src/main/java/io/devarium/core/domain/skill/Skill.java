package io.devarium.core.domain.skill;

import lombok.Getter;

@Getter
public enum Skill {
    // Programming Language
    CPP("C++"),
    CSHARP("C#"),
    GO("Go"),
    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    KOTLIN("Kotlin"),
    PYTHON("Python"),
    RUST("Rust"),
    SWIFT("Swift"),
    TYPESCRIPT("TypeScript"),

    // Backend Framework/Library
    DJANGO("Django"),
    DOTNET(".NET"),
    NESTJS("NestJS"),
    NODEJS("Node.js"),
    SPRING("Spring"),
    SPRING_BOOT("Spring Boot"),

    // Frontend Framework/Library
    ANGULAR("Angular"),
    NEXTJS("Next.js"),
    REACT("React"),
    SVELTE("Svelte"),
    VUE("Vue.js"),

    // DB
    ELASTICSEARCH("Elasticsearch"),
    MONGODB("MongoDB"),
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    REDIS("Redis"),

    // Infrastructure/DevOps
    AWS("AWS"),
    DOCKER("Docker"),
    GITHUB_ACTIONS("GitHub Actions"),
    JENKINS("Jenkins"),
    KUBERNETES("Kubernetes");

    private final String displayName;

    Skill(String displayName) {
        this.displayName = displayName;
    }
}