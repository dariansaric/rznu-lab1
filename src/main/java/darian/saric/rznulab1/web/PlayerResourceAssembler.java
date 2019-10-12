package darian.saric.rznulab1.web;

import darian.saric.rznulab1.model.Player;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PlayerResourceAssembler implements ResourceAssembler<Player, Resource<Player>> {
    @Override
    public Resource<Player> toResource(Player player) {
        return new Resource<>(player,
                linkTo(methodOn(PlayerController.class).one(player.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("players")
        );
    }
}
