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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RznuLab1Application.class)
@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "target/snippets")
//@WebMvcTest(TeamController.class)
public class TeamControllerTest {
    private static final String BASE_URL = "http://localhost:8080/nfl";
    private static final Gson GSON = new Gson();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TeamRepository repository;
    //    @Autowired


    //    @WithMockUser("Roger Godell")
    @Test
    public void one() throws Exception {
//        TestRestTemplate template = new TestRestTemplate("Roger Godell", "ihatetombrady");
        Team t = new Team("New England Patriots", 6, 1969);
        Long id = repository.findByTeamName("New England Patriots").getId();

//        ResponseEntity<?> re = template
//                .getForEntity(BASE_URL + "/teams/" + id, String.class);
//        assertEquals(HttpStatus.OK, re.getStatusCode());
//        assertEquals(MediaType.APPLICATION_JSON, re.getHeaders().getContentType());
//        mvc.perform(
//                get("/teams/" + id)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().json(GSON.toJson(t)));
    }

    //    @WithMockUser("Roger Godell")
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

    @WithMockUser("Roger Godell")
    @Test
    public void updateTeam() throws Exception {
        Team t = repository.findAll().get(1);
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

    @WithMockUser("Roger Godell")
    @Test
    public void deleteTeam() throws Exception {
        Long id = repository.findAll().get(0).getId();

        mvc.perform(delete("/teams/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk());
    }
}