package io.devarium.core.domain.team.command;

public interface UpsertTeam {

    String name();

    String description();

    String picture();

    String githubUrl();
}
