package darian.saric.rznulab1.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Player {
    private @Id
    @GeneratedValue
    Long id;
    private String name;
    private Position position;
    private int age;
    private String college;

    public Player() {
    }

    public Player(String name, Position position, int age, String college) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.college = college;
    }
}
