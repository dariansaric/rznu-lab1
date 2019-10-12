package darian.saric.rznulab1.model;

public enum Position {
    // todo: preimenovati
    QB("QB"),
    RB("RB"),
    WR("WR"),
    C("C"),
    RT("RT"),
    LT("LT"),
    FB("FB"),
    RG("RG"),
    LG("LG"),
    K("K"),
    P("P")
    // todo: obrana
    ;

    private String name;

    Position(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
