package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Objects;

/**
 * A scenario with {@code <placeholders>} and an Examples table, used for
 * boundary scenarios (FR-29).
 */
public class ScenarioOutline extends Scenario {

    private final ExamplesTable examples;

    ScenarioOutline(String title, List<String> tags, ScenarioKind kind, List<Step> steps,
                    Requirement derivedFrom, ExamplesTable examples) {
        super(title, tags, kind, steps, derivedFrom);
        this.examples = Objects.requireNonNull(examples, "examples");
    }

    public ExamplesTable examples() {
        return examples;
    }

    @Override
    public boolean isOutline() {
        return true;
    }

    @Override
    protected Scenario copy(String newTitle, List<Step> newSteps) {
        return new ScenarioOutline(newTitle, tags(), kind(), newSteps, derivedFrom(), examples);
    }

    @Override
    public String toString() {
        return "ScenarioOutline[" + title() + ", " + tags() + "]";
    }
}
