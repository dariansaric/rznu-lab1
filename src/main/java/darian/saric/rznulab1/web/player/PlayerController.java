package darian.saric.rznulab1.web.player;

import com.google.gson.Gson;
import darian.saric.rznulab1.model.Player;
import darian.saric.rznulab1.model.PlayerRepository;
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
@RequestMapping("/players")
public class PlayerController {
    private static final Gson GSON = new Gson();
    private final PlayerRepository repository;
    private final PlayerResourceAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> all() {
        List<PlayerResource> players = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Resources<>(players,
                        linkTo(methodOn(PlayerController.class).all()).withSelfRel()));
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createPlayer(@RequestBody String newPlayer) throws URISyntaxException {
        Player p = GSON.fromJson(newPlayer, Player.class);
        Player pojoPlayer = repository.save(p); // newly created player as POJO
        PlayerResource player = assembler.toResource(pojoPlayer);

        return player != null ?
                ResponseEntity.created(
                        new URI(linkTo(Player.class)
                                .slash(pojoPlayer.getId())
                                .withSelfRel().getHref()))
                        .body(player)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Optional<Player> p = repository.findById(id);

        return p.isPresent() ?
                ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(assembler.toResource(p.get())) :
                ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updatePlayer(@RequestBody Player player, @PathVariable Long id) {
        PlayerResource playerResource = repository.findById(id)
                .map(p -> {
                    p.setName(player.getName());
                    p.setAge(player.getAge());
                    p.setCollege(player.getCollege());
                    p.setPosition(player.getPosition());
                    p.setTeam(player.getTeam());
                    return assembler.toResource(repository.save(p));
                })
                .orElseGet(() -> {
                    return null;
                });

        return playerResource != null ?
                ResponseEntity.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(player) :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
