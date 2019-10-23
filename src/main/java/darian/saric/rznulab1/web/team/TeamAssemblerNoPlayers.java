package darian.saric.rznulab1.web.team;

import darian.saric.rznulab1.model.Team;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TeamAssemblerNoPlayers extends ResourceAssemblerSupport<Team, TeamResourceNoPlayers> {

    public TeamAssemblerNoPlayers() {
        super(TeamController.class, TeamResourceNoPlayers.class);
    }

    @Override
    public TeamResourceNoPlayers toResource(Team team) {
        TeamResourceNoPlayers teamResourceNoPlayers = super.createResourceWithId(team.getId(), team);
        teamResourceNoPlayers.setName(team.getName());
        teamResourceNoPlayers.setSuperBowlsWon(team.getSuperBowlsWon());
        teamResourceNoPlayers.setYearFound(team.getYearFound());

        teamResourceNoPlayers.add(linkTo(methodOn(TeamController.class).all(false)).withRel("teams"));
        teamResourceNoPlayers.add(linkTo(methodOn(TeamController.class).all(true)).withRel("teams"));

        return teamResourceNoPlayers;
    }
}
