package edu.gtu.cse443.gherkingen.model;

import java.util.Optional;

/**
 * Gherkin step keywords. {@link #AND} is only used when a step is written
 * (FR-17); a {@link Step} always holds GIVEN, WHEN or THEN.
 */
public enum StepKeyword {
    GIVEN("Given"),
    WHEN("When"),
    THEN("Then"),
    AND("And");

    private final String text;

    StepKeyword(String text) {
        this.text = text;
    }

    /** The keyword as written in a feature file, e.g. {@code "Given"}. */
    public String text() {
        return text;
    }

    /** Finds the keyword written as {@code word} (case-sensitive, as in Gherkin). */
    public static Optional<StepKeyword> fromText(String word) {
        for (StepKeyword keyword : values()) {
            if (keyword.text.equals(word)) {
                return Optional.of(keyword);
            }
        }
        return Optional.empty();
    }
}
