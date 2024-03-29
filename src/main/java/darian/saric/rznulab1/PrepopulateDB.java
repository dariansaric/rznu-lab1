package darian.saric.rznulab1;

import com.google.gson.Gson;
import darian.saric.rznulab1.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration

@Slf4j
public class PrepopulateDB {
    private static final List<Team> teams = Arrays.asList(
            new Team("New England Patriots", 6, 1969),
            new Team("Kansas City Chiefs", 0, 1950),
            new Team("Pittburgh Steelers", 7, 1933));
    private static final List<Player> players = Arrays.asList(
            new Player("Tom Brady", Position.QB, 42, "Michigan", teams.get(0)),
            new Player("Patrick Mahomes", Position.QB, 23, "Texas", teams.get(1)),
            new Player("Julian Edelman", Position.WR, 30, "Ohio", teams.get(0)));


    @Bean
    CommandLineRunner initDatabase(PlayerRepository playerRepository, TeamRepository teamRepository) {
        return args -> {
            log.info("Preloading teams " + teamRepository.saveAll(teams));
            log.info("Preloading players " + playerRepository.saveAll(players));
            String s = new Gson().toJson(teamRepository.findAll().get(0));
            log.info(s);
        };
    }
}
