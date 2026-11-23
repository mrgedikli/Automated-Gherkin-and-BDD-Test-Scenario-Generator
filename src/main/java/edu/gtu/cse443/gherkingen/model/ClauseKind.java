package edu.gtu.cse443.gherkingen.model;

import java.util.Optional;

/**
 * Kind of a clause in an EARS requirement, with the Gherkin step it maps to
 * (FR-14, FR-15, FR-16) and the EARS pattern it signals.
 */
public enum ClauseKind {
    WHILE(StepKeyword.GIVEN, EarsPattern.STATE_DRIVEN),
    WHERE(StepKeyword.GIVEN, EarsPattern.OPTIONAL_FEATURE),
    WHEN(StepKeyword.WHEN, EarsPattern.EVENT_DRIVEN),
    IF(StepKeyword.WHEN, EarsPattern.UNWANTED_BEHAVIOUR),
    SHALL(StepKeyword.THEN, null);

    private final StepKeyword stepKeyword;
    private final EarsPattern pattern;

    ClauseKind(StepKeyword stepKeyword, EarsPattern pattern) {
        this.stepKeyword = stepKeyword;
        this.pattern = pattern;
    }

    /** Keyword of the Gherkin step this clause is translated into. */
    public StepKeyword stepKeyword() {
        return stepKeyword;
    }

    /**
     * The EARS pattern signalled by this clause; empty for the response
     * clause, which every pattern has.
     */
    public Optional<EarsPattern> pattern() {
        return Optional.ofNullable(pattern);
    }

    /** True for the "shall" response clause. */
    public boolean isResponse() {
        return this == SHALL;
    }
}
