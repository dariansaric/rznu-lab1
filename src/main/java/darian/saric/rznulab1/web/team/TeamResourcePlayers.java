package darian.saric.rznulab1.web.team;

import darian.saric.rznulab1.web.player.PlayerResource;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class TeamResourcePlayers extends ResourceSupport {
    private String name;
    private int superBowlsWon;
    private int yearFound;
    private List<PlayerResource> players;
    private Link all;
    private Link linkNoPlayers;
}
