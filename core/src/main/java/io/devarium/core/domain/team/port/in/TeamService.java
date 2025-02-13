package io.devarium.core.domain.team.port.in;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.command.CreateTeam;
import io.devarium.core.domain.team.command.UpdateLeader;
import io.devarium.core.domain.team.command.UpdateTeamInfo;
import io.devarium.core.domain.team.command.UpdateTeamName;
import io.devarium.core.domain.user.User;
import io.devarium.core.storage.Image;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {

    Team createTeam(CreateTeam request, Image image, User user);

    Team getTeam(Long teamId);

    Page<Team> getTeams(String teamName, Pageable pageable);

    List<Team> getTeams(List<Long> teamIds);

    Team updateTeamInfo(Long teamId, UpdateTeamInfo request, User user);

    Team updateTeamName(Long teamId, UpdateTeamName request, User user);

    Team updateTeamProfileImage(Long teamId, Image image, User user);

    Team updateLeader(Long teamId, UpdateLeader request, User user);

    void deleteTeam(Long teamId, User user);

    boolean checkUserIsLeader(Long userId);

    boolean checkTeamExists(Long teamId);
}
