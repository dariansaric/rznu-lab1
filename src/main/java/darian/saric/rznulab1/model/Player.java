package darian.saric.rznulab1.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "p_id")
    private Long id;
    private String name;
    private Position position;
    private int age;
    private String college;
    @ManyToOne()
    @JoinColumn(name = "t_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

    public Player() {
    }

    public Player(String name, Position position, int age, String college
            , Team team
    ) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.college = college;
        this.team = team;
    }
}
