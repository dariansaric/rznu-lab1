package darian.saric.rznulab1.web;

import darian.saric.rznulab1.model.Position;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

@Component
@Data
public class PlayerResource extends ResourceSupport {
    private String name;
    private Position position;
    private int age;
    private String college;
    private String team;
    //    private Link self;
    private Link all;


}
