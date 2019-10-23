package darian.saric.rznulab1.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "team")
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "t_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "superBowlsWon")
    private int superBowlsWon;
    @Column(name = "yearFound")
    private int yearFound;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
    private List<Player> players;

    public Team() {

    }

    public Team(String name, int superBowlsWon, int yearFound
//            , Set<Player> players
    ) {
        this.name = name;
        this.superBowlsWon = superBowlsWon;
        this.yearFound = yearFound;
//        this.players = players;
    }
}
