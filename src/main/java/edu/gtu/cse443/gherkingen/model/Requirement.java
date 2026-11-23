package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;
import java.util.Optional;

/**
 * One requirement read from the requirements file (FR-3).
 *
 * <p>A requirement is an entity: two requirements with the same ID are still
 * different objects (a duplicate ID is a validation error, FR-35). Its status
 * follows the state machine of Figure 11; illegal transitions throw
 * {@link IllegalStateException}.</p>
 */
public abstract class Requirement {

    private final String requirementId;
    private final String text;
    private final int lineNumber;
    private final String featureName;
    private RequirementStatus status = RequirementStatus.LOADED;

    /**
     * @param requirementId the ID from the file (FR-4) or {@code "L<line>"} (FR-5)
     * @param text          the requirement text without the ID prefix
     * @param lineNumber    1-based line number in the requirements file
     * @param featureName   name from the preceding "Feature:" line (FR-6), or {@code null}
     */
    protected Requirement(String requirementId, String text, int lineNumber, String featureName) {
        this.requirementId = Checks.requireNonBlank(requirementId, "requirementId");
        this.text = Checks.requireNonBlank(text, "text");
        if (lineNumber < 1) {
            throw new IllegalArgumentException("lineNumber must be at least 1: " + lineNumber);
        }
        this.lineNumber = lineNumber;
        this.featureName = featureName == null || featureName.isBlank() ? null : featureName;
    }

    /** The input format this requirement is written in. */
    public abstract InputFormat inputFormat();

    public String requirementId() {
        return requirementId;
    }

    public String text() {
        return text;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public Optional<String> featureName() {
        return Optional.ofNullable(featureName);
    }

    public RequirementStatus status() {
        return status;
    }

    public boolean isValid() {
        return status == RequirementStatus.VALID;
    }

    public boolean isInvalid() {
        return status == RequirementStatus.INVALID;
    }

    /** Validation passed (FR-31). */
    public void markValid() {
        moveTo(RequirementStatus.VALID);
    }

    /** Validation or normalization failed (FR-32 ... FR-35, FR-54, FR-56, FR-58, FR-59). */
    public void markInvalid() {
        moveTo(RequirementStatus.INVALID);
    }

    /** Scenarios were generated from this requirement. */
    public void markTranslated() {
        moveTo(RequirementStatus.TRANSLATED);
    }

    protected final void moveTo(RequirementStatus target) {
        Objects.requireNonNull(target, "target");
        if (!status.canMoveTo(target)) {
            throw new IllegalStateException("Requirement " + requirementId
                    + " cannot move from " + status + " to " + target);
        }
        status = target;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + requirementId + ", line " + lineNumber
                + ", " + status + "]";
    }
}
