package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.CreateMembers;
import io.devarium.core.domain.team.port.DeleteMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;

public interface TeamService {

    Team createTeam(UpsertTeam request, User user);

    Page<Team> getTeams(User user);

    Team updateTeam(UpsertTeam request, User user);

    Team updateLeader(UpdateLeader request, User user);

    Team createMembers(CreateMembers request, User user);

    Team deleteMembers(DeleteMembers request, User user);

    void deleteTeam(Long teamId, User user);
}