package io.devarium.api.controller.skill.dto;

import io.devarium.core.domain.skill.Skill;

public record SkillResponse(String name, String displayName) {

    public static SkillResponse from(Skill skill) {
        return new SkillResponse(skill.name(), skill.getDisplayName());
    }
}
