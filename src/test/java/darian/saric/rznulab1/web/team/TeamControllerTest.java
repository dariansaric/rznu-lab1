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
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import org.apache.tomcat.jni.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RznuLab1Application.class)
@AutoConfigureMockMvc()
//@AutoConfigureRestDocs(outputDir = "target/snippets")
//@WebMvcTest(TeamController.class)
public class TeamControllerTest {
    //    private static final String BASE_URL = "http://localhost:8080/nfl";
    private static final Gson GSON = new Gson();
    //    @Autowired
//    private WebApplicationContext context;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TeamRepository repository;

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//
//    }

    @Test
    public void one() throws Exception {
//        TestRestTemplate template = new TestRestTemplate("Roger Godell", "ihatetombrady");
        Team t = new Team("New England Patriots", 6, 1969);
        Long id = repository.findByTeamName("New England Patriots").getId();

//        ResponseEntity<?> re = template
//                .getForEntity(BASE_URL + "/teams/" + id, String.class);
//        assertEquals(HttpStatus.OK, re.getStatusCode());
//        assertEquals(MediaType.APPLICATION_JSON, re.getHeaders().getContentType());
        mvc.perform(
                get("/api/teams/" + id)
                        .with(user("admin"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t)));
    }

    //    @WithMockUser("admin")
    @Test
    public void createTeam() throws Exception {
        Team t = new Team("Buffalo Bills", 0, 1990);

        mvc.perform(
                put("/api/teams/new")
                        .with(user(User.withDefaultPasswordEncoder().username("admin").roles("ADMIN").build()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(GSON.toJson(t)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t)));
    }

    //    @WithMockUser("Roger Godell")
    @Test
    public void updateTeam() throws Exception {
        Team t = repository.findAll().get(1);
        t.setSuperBowlsWon(10);
        Team t1 = new Team(t.getName(), t.getSuperBowlsWon(), t.getYearFound());

        mvc.perform(post("/api/teams/" + t.getId())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(GSON.toJson(t)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(GSON.toJson(t1)));
    }

    //    @WithMockUser("Roger Godell")
    @Test
    public void deleteTeam() throws Exception {
        Long id = repository.findAll().get(0).getId();

        mvc.perform(delete("/api/teams/" + id)
                .with(user("admin"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlayers() throws Exception {
        Team t = repository.findAll().get(0); //dohvacam tim koji ima 2 igraca pri pocetnom pokretanju

        mvc.perform(
                get("/api/teams/" + t.getId() + "/players")
                        .with(user("admin"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
        ;
    }
}