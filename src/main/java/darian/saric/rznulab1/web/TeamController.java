package darian.saric.rznulab1.web;

import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.model.TeamRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/teams")
public class TeamController {
    //todo: popravi resurs Team
    private final TeamRepository repository;
    private final TeamAssembler assembler;

    public TeamController(TeamRepository repository, TeamAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping()
    ResponseEntity<?> all() {
        List<Resource<Team>> teams = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Resources<>(teams,
                        linkTo(methodOn(TeamController.class).all()).withSelfRel()));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Optional<Team> t = repository.findById(id);

        return t.isPresent() ?
                ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(assembler.toResource(t.get())) :
                ResponseEntity.notFound().build();
    }
}
