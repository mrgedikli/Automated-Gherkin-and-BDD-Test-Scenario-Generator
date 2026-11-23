package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;

/**
 * One entry of the error report (FR-39).
 *
 * @param requirement     the requirement the finding is about
 * @param reason          e.g. "missing response clause"
 * @param expectedPattern the closest expected pattern (FR-33), or an empty string
 * @param hint            how to correct the requirement, or an empty string
 * @param severity        {@link Severity#ERROR} or {@link Severity#WARNING}
 */
public record ValidationError(Requirement requirement, String reason, String expectedPattern,
                              String hint, Severity severity) {

    public ValidationError {
        Objects.requireNonNull(requirement, "requirement");
        Checks.requireNonBlank(reason, "reason");
        Objects.requireNonNull(expectedPattern, "expectedPattern");
        Objects.requireNonNull(hint, "hint");
        Objects.requireNonNull(severity, "severity");
    }

    public int lineNumber() {
        return requirement.lineNumber();
    }

    public String requirementId() {
        return requirement.requirementId();
    }

    public String requirementText() {
        return requirement.text();
    }

    public boolean isError() {
        return severity == Severity.ERROR;
    }
}
