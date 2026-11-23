package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Objects;

/**
 * A generated Gherkin scenario. Scenarios are immutable and are created with
 * {@link ScenarioBuilder}.
 */
public class Scenario {

    private final String title;
    private final List<String> tags;
    private final ScenarioKind kind;
    private final List<Step> steps;
    private final Requirement derivedFrom;

    Scenario(String title, List<String> tags, ScenarioKind kind, List<Step> steps, Requirement derivedFrom) {
        this.title = Checks.requireSingleLine(title, "title").strip();
        this.tags = List.copyOf(tags);
        this.kind = Objects.requireNonNull(kind, "kind");
        this.steps = List.copyOf(steps);
        this.derivedFrom = Objects.requireNonNull(derivedFrom, "derivedFrom");
        if (this.tags.isEmpty()) {
            throw new IllegalArgumentException("a scenario has at least one tag");
        }
        if (this.steps.stream().noneMatch(s -> s.keyword() == StepKeyword.THEN)) {
            throw new IllegalArgumentException("scenario \"" + title + "\" has no Then step");
        }
    }

    public String title() {
        return title;
    }

    /** Tags in the order they are written, e.g. {@code ["@REQ-7", "@positive"]}. */
    public List<String> tags() {
        return tags;
    }

    public ScenarioKind kind() {
        return kind;
    }

    public List<Step> steps() {
        return steps;
    }

    /** The requirement this scenario was generated from (FR-23). */
    public Requirement derivedFrom() {
        return derivedFrom;
    }

    public boolean isOutline() {
        return false;
    }

    /** The Given steps at the start of the scenario, candidates for a Background (FR-30). */
    public List<Step> leadingGivenSteps() {
        int count = 0;
        while (count < steps.size() && steps.get(count).keyword() == StepKeyword.GIVEN) {
            count++;
        }
        return steps.subList(0, count);
    }

    /** A copy with another title, e.g. with a sequence number appended (FR-27). */
    public Scenario withTitle(String newTitle) {
        return copy(newTitle, steps);
    }

    /** A copy without its first {@code count} steps, after they moved to the Background (FR-30). */
    public Scenario withoutFirstSteps(int count) {
        if (count < 0 || count > steps.size()) {
            throw new IllegalArgumentException("cannot remove " + count + " of " + steps.size() + " steps");
        }
        return copy(title, steps.subList(count, steps.size()));
    }

    /** Creates a copy of the same class with the given title and steps. */
    protected Scenario copy(String newTitle, List<Step> newSteps) {
        return new Scenario(newTitle, tags, kind, newSteps, derivedFrom);
    }

    @Override
    public String toString() {
        return "Scenario[" + title + ", " + tags + "]";
    }
}
