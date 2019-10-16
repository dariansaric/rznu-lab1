package darian.saric.rznulab1.web;

import darian.saric.rznulab1.model.Team;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TeamAssembler implements ResourceAssembler<Team, Resource<Team>> {
    @Override
    public Resource<Team> toResource(Team team) {
        return new Resource<>(team,
                linkTo(methodOn(TeamController.class).one(team.getId())).withSelfRel(),
                linkTo(methodOn(TeamController.class).all()).withRel("teams")
        );
    }
}
