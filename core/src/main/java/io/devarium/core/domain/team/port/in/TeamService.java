package io.devarium.core.domain.team.port.in;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.command.UpdateLeader;
import io.devarium.core.domain.team.command.UpsertTeam;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {

    Team createTeam(UpsertTeam request, User user);

    Team getTeam(Long teamId);

    Page<Team> getTeams(Pageable pageable, User user);

    Team updateTeam(Long teamId, UpsertTeam request, User user);

    Team updateLeader(Long teamId, UpdateLeader request, User user);

    void deleteTeam(Long teamId, User user);
}
