package darian.saric.rznulab1.util;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(Long id) {
        super("Could not find team with id: " + id);
    }
}
