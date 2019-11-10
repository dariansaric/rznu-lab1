package darian.saric.rznulab1.web.team;

import com.google.gson.Gson;
import darian.saric.rznulab1.RznuLab1Application;
import darian.saric.rznulab1.model.Team;
import darian.saric.rznulab1.model.TeamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RznuLab1Application.class)
@AutoConfigureMockMvc
public class TeamControllerTest {
    private static final Gson GSON = new Gson();
    @Autowired
    WebApplicationContext context;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TeamRepository repository;

//    @Before
//    public void setUp() {
//        mvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }

    @Test
    public void all() {

    }

    @Test
    public void one() throws Exception {
        Team t = new Team("New England Patriots", 6, 1969);
        Long id = repository.findByTeamName("New England Patriots").getId();

        mvc.perform(
                get("/teams/" + id)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t)));
    }

    @Test
    public void createTeam() throws Exception {
        Team t = new Team("Buffalo Bills", 0, 1990);

        mvc.perform(
                put("/teams/new")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(GSON.toJson(t)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t)));
    }

    @Test
    public void updateTeam() throws Exception {
        Team t = repository.findAll().get(0);
        t.setSuperBowlsWon(10);
        Team t1 = new Team(t.getName(), t.getSuperBowlsWon(), t.getYearFound());

        mvc.perform(post("/teams/" + t.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(GSON.toJson(t)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t1)));
    }

    @Test
    public void deleteTeam() throws Exception {
        Long id = repository.findAll().get(0).getId();

        mvc.perform(delete("/teams/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isAccepted());
    }
}