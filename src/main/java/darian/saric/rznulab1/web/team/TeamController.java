package darian.saric.rznulab1.web.team;

import com.google.gson.Gson;
import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.model.TeamRepository;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private static final Gson GSON = new Gson();
    private final TeamRepository repository;
    private final TeamAssemblerNoPlayers noPlayersAssembler;
    private final TeamAssemblerPlayers playersAssembler;

    public TeamController(TeamRepository repository, TeamAssemblerNoPlayers noPlayersAssembler, TeamAssemblerPlayers playersAssembler) {
        this.repository = repository;
        this.noPlayersAssembler = noPlayersAssembler;
        this.playersAssembler = playersAssembler;
    }

    @GetMapping
    ResponseEntity<?> all(@RequestParam(value = "roster", required = false) boolean includeRoster) {
        List<?> teams = includeRoster ?
                repository.findAll().stream()
                        .map(playersAssembler::toResource)
                        .collect(Collectors.toList()) :
                repository.findAll().stream()
                        .map(noPlayersAssembler::toResource)
                        .collect(Collectors.toList());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Resources<>(teams,
                        linkTo(methodOn(TeamController.class).all(includeRoster)).withSelfRel()));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> one(@PathVariable Long id, @RequestParam(value = "roster", required = false) boolean includeRoster) {
        Optional<Team> t = repository.findById(id);

        return t.isPresent() ?
                ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                        includeRoster ?
                                playersAssembler.toResource(t.get()) :
                                noPlayersAssembler.toResource(t.get()))
                :
                ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createTeam(@RequestBody String newTeam) throws URISyntaxException {
        Team team = GSON.fromJson(newTeam, Team.class);
        Team pojoTeam = repository.save(team);
        TeamResourceNoPlayers teamResource = noPlayersAssembler.toResource(pojoTeam);

        return teamResource != null ?
                ResponseEntity.created(
                        new URI(linkTo(
                                methodOn(TeamController.class)
                                        .one(pojoTeam.getId(), false))
                                .withSelfRel().getHref()))
//                        .contentType(MediaType.APPLICATION_JSON)
                        .body(teamResource)
                :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/{id}")
    ResponseEntity<?> updateTeam(@RequestBody Team team, @PathVariable Long id, HttpServletRequest request) {
        TeamResourceNoPlayers teamResource = repository.findById(id)
                .map(t -> {
                    t.setName(team.getName());
                    t.setSuperBowlsWon(team.getSuperBowlsWon());
                    t.setYearFound(team.getYearFound());
                    return noPlayersAssembler.toResource(repository.save(t));
                }).orElseGet(() -> {
                    // todo dalje
                    return null;
                });

        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(teamResource);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
