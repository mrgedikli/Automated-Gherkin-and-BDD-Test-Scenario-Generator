package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Objects;

/**
 * An acceptance criterion of a user story: consecutive Given/When/Then/And
 * lines that follow the story in the file (FR-7).
 *
 * @param stepTexts the lines as written, including their keyword
 */
public record AcceptanceCriterion(List<String> stepTexts) {

    public AcceptanceCriterion {
        Objects.requireNonNull(stepTexts, "stepTexts");
        if (stepTexts.isEmpty()) {
            throw new IllegalArgumentException("an acceptance criterion has at least one step");
        }
        stepTexts.forEach(s -> Checks.requireSingleLine(s, "stepText"));
        stepTexts = List.copyOf(stepTexts);
    }
}
