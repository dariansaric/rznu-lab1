package darian.saric.rznulab1.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Team {
    //todo: reprezentacija kluba bez popisivanja igrača, ili?
    @Id
    @GeneratedValue
    @Column(name = "t_id")
    private Long id;
    private String name;
    private int superBowlsWon;
    private int yearFound;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
    private Set<Player> players;

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
