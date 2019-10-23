package darian.saric.rznulab1.web.player;

import darian.saric.rznulab1.model.Player;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PlayerResourceAssembler extends ResourceAssemblerSupport<Player, PlayerResource> {

    public PlayerResourceAssembler() {
        super(PlayerController.class, PlayerResource.class);
    }

    @Override
    public PlayerResource toResource(Player player) {
        PlayerResource playerResource = super.createResourceWithId(player.getId(), player);
        playerResource.setName(player.getName());
        playerResource.setAge(player.getAge());
        playerResource.setCollege(player.getCollege());
        playerResource.setPosition(player.getPosition());
        playerResource.setTeam(player.getTeam().getName());
        playerResource.add(linkTo(methodOn(PlayerController.class).all()).withRel("players"));

        return playerResource;
    }
}
