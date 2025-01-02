package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.CreateMembers;
import io.devarium.core.domain.team.port.DeleteMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {

    Team createTeam(UpsertTeam request, User user);

    Team getTeam(Long teamId);

    Page<Team> getTeams(Pageable pageable, User user);

    Team updateTeam(Long teamId, UpsertTeam request, User user);

    Team updateLeader(Long teamId, UpdateLeader request, User user);

    Team createMembers(Long teamId, CreateMembers request, User user);

    Team deleteMembers(Long teamId, DeleteMembers request, User user);

    void deleteTeam(Long teamId, User user);
}
