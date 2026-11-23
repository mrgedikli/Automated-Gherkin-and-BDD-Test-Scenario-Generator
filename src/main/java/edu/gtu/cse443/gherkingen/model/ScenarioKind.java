package edu.gtu.cse443.gherkingen.model;

/**
 * Kind of a generated scenario, written as a tag (FR-24).
 */
public enum ScenarioKind {
    POSITIVE("@positive"),
    NEGATIVE("@negative"),
    BOUNDARY("@boundary");

    private final String tag;

    ScenarioKind(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
