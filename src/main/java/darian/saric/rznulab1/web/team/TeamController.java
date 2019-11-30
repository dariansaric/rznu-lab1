package darian.saric.rznulab1.web.team;

import com.google.gson.Gson;
import darian.saric.rznulab1.model.PlayerRepository;
import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.model.TeamRepository;
import darian.saric.rznulab1.web.player.PlayerResource;
import darian.saric.rznulab1.web.player.PlayerResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //    @Autowired
    private final PlayerRepository playerRepository;
    private final TeamAssemblerNoPlayers teamAssemblerNoPlayers;
    private final TeamAssemblerPlayers teamAssemblerPlayers;
    private final PlayerResourceAssembler playerAssembler;

    public TeamController(TeamRepository repository, PlayerRepository playerRepository,
                          TeamAssemblerNoPlayers teamAssemblerNoPlayers, TeamAssemblerPlayers teamAssemblerPlayers,
                          PlayerResourceAssembler playerAssembler) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.teamAssemblerNoPlayers = teamAssemblerNoPlayers;
        this.teamAssemblerPlayers = teamAssemblerPlayers;
        this.playerAssembler = playerAssembler;
    }

    @GetMapping(value = "/{id}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getPlayersForTeam(@PathVariable Long id) {
        Optional<Team> t = repository.findById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PlayerResource> players = playerRepository.getAllByTeam(t.get()).stream()
                .map(playerAssembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Resources<>(players,
                        linkTo(methodOn(TeamController.class).getPlayersForTeam(id)).withSelfRel()));
    }

    @GetMapping
    ResponseEntity<?> all(@RequestParam(value = "roster", required = false) boolean includeRoster) {
        List<?> teams = includeRoster ?
                repository.findAll().stream()
                        .map(teamAssemblerPlayers::toResource)
                        .collect(Collectors.toList()) :
                repository.findAll().stream()
                        .map(teamAssemblerNoPlayers::toResource)
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
                                teamAssemblerPlayers.toResource(t.get()) :
                                teamAssemblerNoPlayers.toResource(t.get()))
                :
                ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createTeam(@RequestBody String newTeam) throws URISyntaxException {
        Team team = GSON.fromJson(newTeam, Team.class);
        Team pojoTeam = repository.save(team);
        TeamResourceNoPlayers teamResource = teamAssemblerNoPlayers.toResource(pojoTeam);

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

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateTeam(@RequestBody Team team, @PathVariable Long id) {
        TeamResourceNoPlayers teamResource = repository.findById(id)
                .map(t -> {
                    t.setName(team.getName());
                    t.setSuperBowlsWon(team.getSuperBowlsWon());
                    t.setYearFound(team.getYearFound());
                    return teamAssemblerNoPlayers.toResource(repository.save(t));
                }).orElseGet(() -> {
                    // todo dalje
                    return null;
                });

        // privremeno rješenje
        return teamResource != null ?
                ResponseEntity.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(teamResource)
                :
                ResponseEntity.badRequest().build();
//        return ResponseEntity.accepted()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(teamResource);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.accepted().build();
    }

}
