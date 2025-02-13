package io.devarium.api.controller.skill;

import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.controller.skill.dto.SkillResponse;
import io.devarium.core.domain.skill.Skill;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
@RestController
public class SkillController {

    @GetMapping
    public ResponseEntity<ListResponse<SkillResponse>> getSkills() {
        List<SkillResponse> skills = Stream.of(Skill.values()).map(SkillResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(skills));
    }
}
