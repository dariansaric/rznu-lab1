package darian.saric.rznulab1.web.team;

import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.web.player.PlayerResourceAssembler;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TeamAssemblerPlayers extends ResourceAssemblerSupport<Team, TeamResourcePlayers> {
    private static final PlayerResourceAssembler playerAssembler = new PlayerResourceAssembler();

    public TeamAssemblerPlayers() {
        super(TeamController.class, TeamResourcePlayers.class);
    }

    @Override
    public TeamResourcePlayers toResource(Team team) {
        TeamResourcePlayers teamResourcePlayers = super.createResourceWithId(team.getId(), team);
        teamResourcePlayers.setName(team.getName());
        teamResourcePlayers.setSuperBowlsWon(team.getSuperBowlsWon());
        teamResourcePlayers.setYearFound(team.getYearFound());
        teamResourcePlayers.setPlayers(
                team.getPlayers()
                        .stream()
                        .map(playerAssembler::toResource)
                        .collect(Collectors.toList()));
        teamResourcePlayers.setAll(linkTo(methodOn(TeamController.class).all(true)).withRel("teams"));
        teamResourcePlayers.setLinkNoPlayers(linkTo(methodOn(TeamController.class).all(false)).withRel("teams"));
        return teamResourcePlayers;
    }
}
