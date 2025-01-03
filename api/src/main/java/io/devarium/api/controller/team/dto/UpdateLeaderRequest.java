package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.UpdateLeader;

public record UpdateLeaderRequest(
    Long leaderId
) implements UpdateLeader {

}
