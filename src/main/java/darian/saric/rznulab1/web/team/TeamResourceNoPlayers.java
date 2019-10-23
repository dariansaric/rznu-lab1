package darian.saric.rznulab1.web.team;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

@Component
@Data
public class TeamResourceNoPlayers extends ResourceSupport {
    private String name;
    private int superBowlsWon;
    private int yearFound;
}
