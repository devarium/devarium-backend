package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.CreateMembers;
import io.devarium.core.domain.team.port.DeleteMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(UpsertTeam request, User user) {
        return null;
    }

    @Override
    public Page<Team> getTeams(User user) {
        return null;
    }

    @Override
    public Team updateTeam(UpsertTeam request, User user) {
        return null;
    }

    @Override
    public Team updateLeader(UpdateLeader request, User user) {
        return null;
    }

    @Override
    public Team createMembers(CreateMembers request, User user) {
        return null;
    }

    @Override
    public Team deleteMembers(DeleteMembers request, User user) {
        return null;
    }

    @Override
    public void deleteTeam(Long teamId, User user) {

    }
}
