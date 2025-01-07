package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.user.User;

public interface TeamService {

    Team createTeam(UpsertTeam request, User user);

    Team getTeam(Long teamId);

    Team updateTeam(Long teamId, UpsertTeam request, User user);

    Team updateLeader(Long teamId, UpdateLeader request, User user);

    void deleteTeam(Long teamId, User user);
}
