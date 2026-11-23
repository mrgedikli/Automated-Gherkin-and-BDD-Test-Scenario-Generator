package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;
import java.util.Optional;

/**
 * One Given, When or Then step of a scenario or background.
 *
 * <p>A step stores its real keyword. Whether it is written as "And" depends on
 * the step before it (FR-17), see {@link #displayKeyword(Step)}.</p>
 */
public final class Step {

    private final StepKeyword keyword;
    private final String text;
    private final Clause sourceClause;

    public Step(StepKeyword keyword, String text) {
        this(keyword, text, null);
    }

    /**
     * @param sourceClause the EARS clause the step was derived from, or {@code null}
     */
    public Step(StepKeyword keyword, String text, Clause sourceClause) {
        Objects.requireNonNull(keyword, "keyword");
        if (keyword == StepKeyword.AND) {
            throw new IllegalArgumentException(
                    "a step stores GIVEN, WHEN or THEN; AND is only used when it is written");
        }
        this.keyword = keyword;
        this.text = Checks.requireSingleLine(text, "text").strip();
        this.sourceClause = sourceClause;
    }

    public StepKeyword keyword() {
        return keyword;
    }

    public String text() {
        return text;
    }

    public Optional<Clause> sourceClause() {
        return Optional.ofNullable(sourceClause);
    }

    /**
     * The keyword to write for this step: {@link StepKeyword#AND} when the
     * previous step has the same keyword (FR-17).
     *
     * @param previous the step written before this one, or {@code null} for the first step
     */
    public StepKeyword displayKeyword(Step previous) {
        return previous != null && previous.keyword == keyword ? StepKeyword.AND : keyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Step)) {
            return false;
        }
        Step other = (Step) o;
        return keyword == other.keyword && text.equals(other.text)
                && Objects.equals(sourceClause, other.sourceClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, text, sourceClause);
    }

    @Override
    public String toString() {
        return keyword.text() + " " + text;
    }
}
