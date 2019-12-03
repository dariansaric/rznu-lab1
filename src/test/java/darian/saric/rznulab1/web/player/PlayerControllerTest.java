package darian.saric.rznulab1.web.player;

import com.google.gson.Gson;
import darian.saric.rznulab1.RznuLab1Application;
import darian.saric.rznulab1.model.Player;
import darian.saric.rznulab1.model.PlayerRepository;
import darian.saric.rznulab1.model.Position;
import darian.saric.rznulab1.model.TeamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RznuLab1Application.class)
@AutoConfigureMockMvc
public class PlayerControllerTest {
    private static final Gson GSON = new Gson();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PlayerRepository repository;
    @Autowired
    private TeamRepository teamRepository;

//    @Test
//    public void all() {
//    }

    @Test
    public void createPlayer() throws Exception {
        Player p = new Player("Saquon Barkley", Position.RB, 21, "Ohio", teamRepository.findAll().get(0));

        mvc.perform(
                put("/api/players/new")
                        .with(user("admin"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(GSON.toJson(p)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().json(GSON.toJson(p))
        ;
    }

    @Test
    public void one() throws Exception {
        Player p = repository.findAll().get(0);

        mvc.perform(
                get("/api/players/" + p.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
//                        .with(user("admin"))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().json(GSON.toJson(p)));
                .andExpect(jsonPath("$.name").value("Tom Brady"));
    }

    @Test
    public void updatePlayer() throws Exception {
        Player p = repository.findAll().get(1);
        p.setCollege("UniZg");
        Player p1 = new Player(p.getName(), p.getPosition(), p.getAge(), p.getCollege(), p.getTeam());

        mvc.perform(
                post("/api/players/" + p.getId())
                        .with(user("admin"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(GSON.toJson(p)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.college").value("UniZg"))
        ;

    }

    @Test
    public void deletePlayer() throws Exception {
        Long id = repository.findAll().get(0).getId();
        mvc.perform(
                delete("/api/players/" + id)
                        .with(user("admin"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk());
    }
}