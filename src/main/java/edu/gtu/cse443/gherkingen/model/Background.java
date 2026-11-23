package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Objects;

/**
 * The Given steps shared by all scenarios of a feature (FR-30).
 */
public final class Background {

    private final List<Step> steps;

    public Background(List<Step> steps) {
        Objects.requireNonNull(steps, "steps");
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("a background has at least one step");
        }
        for (Step step : steps) {
            if (step.keyword() != StepKeyword.GIVEN) {
                throw new IllegalArgumentException("a background holds only Given steps: " + step);
            }
        }
        this.steps = List.copyOf(steps);
    }

    public List<Step> steps() {
        return steps;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Background && steps.equals(((Background) o).steps);
    }

    @Override
    public int hashCode() {
        return steps.hashCode();
    }

    @Override
    public String toString() {
        return "Background" + steps;
    }
}
