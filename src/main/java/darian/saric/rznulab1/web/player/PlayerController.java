package darian.saric.rznulab1.web.player;

import darian.saric.rznulab1.model.Player;
import darian.saric.rznulab1.model.PlayerRepository;
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
@RequestMapping("/players")
public class PlayerController {
    //todo dodavanje igraca uz (ne)postojece klubove
    private final PlayerRepository repository;
    private final PlayerResourceAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping()
    ResponseEntity<?> all() {
        List<PlayerResource> players = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Resources<>(players,
                        linkTo(methodOn(PlayerController.class).all()).withSelfRel()));
    }

    @PostMapping("/new")
    ResponseEntity<?> createPlayer(@RequestBody Player newPlayer) throws URISyntaxException {
        Player pojoPlayer = repository.save(newPlayer); // newly created player as POJO
        PlayerResource player = assembler.toResource(pojoPlayer);

        if (player != null) {
            return ResponseEntity.created(
                    new URI(linkTo(Player.class).slash(pojoPlayer.getId()).withSelfRel().getHref()))
                    .body(player);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
        PlayerResource playerEntityModel = repository.findById(id)
                .map(p -> {
                    p.setName(player.getName());
                    p.setAge(player.getAge());
                    p.setCollege(player.getCollege());
                    p.setPosition(player.getPosition());
                    p.setTeam(player.getTeam());
                    return assembler.toResource(repository.save(p));
                })
                .orElseGet(() -> {
                    //todo: sto napraviti ovdje
                    return null;
                });

        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playerEntityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
