package darian.saric.rznulab1.web;

import darian.saric.rznulab1.model.Player;
import darian.saric.rznulab1.model.PlayerRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
@RequestMapping("/players")
public class PlayerController {
    private final PlayerRepository repository;
    private final PlayerResourceAssembler assembler;


    public PlayerController(PlayerRepository repository, PlayerResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping()
        //todo: popraviti
    Resources<Resource<Player>> all() {
        List<Resource<Player>> players = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(players,
                linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @PostMapping("/new")
    ResponseEntity<?> createPlayer(@RequestBody Player newPlayer) throws URISyntaxException {
        Resource<Player> player = assembler.toResource(repository.save(newPlayer));

        return ResponseEntity.created(new URI(player.getId().expand().getHref()))
                .body(player);
    }


    @GetMapping("/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Optional<Player> p = repository.findById(id);

        return p.isPresent() ?
                ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(assembler.toResource(p.get())) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}")
    ResponseEntity<?> updatePlayer(@RequestBody Player player, @PathVariable Long id, HttpServletRequest request) {
        return null;
    }


}
