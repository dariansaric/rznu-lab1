package darian.saric.rznulab1;

import darian.saric.rznulab1.model.Player;
import darian.saric.rznulab1.model.PlayerRepository;
import darian.saric.rznulab1.model.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@Slf4j
public class PrepopulateDB {

    @Bean
    CommandLineRunner initDatabase(PlayerRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Player("Tom Brady", Position.QB, 42, "Michigan")));
            log.info("Preloading " + repository.save(new Player("Patrick Mahomes", Position.QB, 23, "Texas")));
        };
    }
}
