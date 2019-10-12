package darian.saric.rznulab1.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Player {
    @Id
    @GeneratedValue
    @Column(name = "p_id")
    private Long id;
    private String name;
    private Position position;
    private int age;
    private String college;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "t_id")
    private Team team;

    public Player() {
    }

    public Player(String name, Position position, int age, String college, Team team) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.college = college;
        this.team = team;
    }
}
