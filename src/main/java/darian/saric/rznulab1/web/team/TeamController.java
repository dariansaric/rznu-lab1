package darian.saric.rznulab1.web.team;

import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.model.TeamRepository;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/teams")
public class TeamController {
    //todo put, post, delete
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


}
