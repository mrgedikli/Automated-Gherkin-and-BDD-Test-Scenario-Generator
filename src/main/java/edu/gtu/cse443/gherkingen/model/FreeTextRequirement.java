package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;
import java.util.Optional;

/**
 * A requirement that is neither EARS nor a user story (FR-12). It can only be
 * translated after the LLM Service has rewritten it as EARS and the Test
 * Engineer has accepted the result (UC-9).
 */
public class FreeTextRequirement extends Requirement {

    private EarsRequirement normalized;
    private Boolean accepted;

    public FreeTextRequirement(String requirementId, String text, int lineNumber, String featureName) {
        super(requirementId, text, lineNumber, featureName);
    }

    @Override
    public InputFormat inputFormat() {
        return InputFormat.FREE_TEXT;
    }

    /**
     * Stores the EARS requirement returned by the LLM Service (FR-53). It must
     * keep the ID and line number of this requirement.
     */
    public void recordNormalization(EarsRequirement result) {
        Objects.requireNonNull(result, "result");
        if (!result.requirementId().equals(requirementId()) || result.lineNumber() != lineNumber()) {
            throw new IllegalArgumentException("the normalized requirement must keep ID "
                    + requirementId() + " and line " + lineNumber());
        }
        if (accepted != null) {
            throw new IllegalStateException("the normalization of " + requirementId() + " was already decided");
        }
        this.normalized = result;
    }

    public Optional<EarsRequirement> normalized() {
        return Optional.ofNullable(normalized);
    }

    public Optional<String> normalizedText() {
        return normalized().map(Requirement::text);
    }

    /** The Test Engineer's decision; empty while it is not made (FR-55). */
    public Optional<Boolean> accepted() {
        return Optional.ofNullable(accepted);
    }

    /** The Test Engineer accepts the normalized text (FR-55). */
    public void acceptNormalization() {
        if (normalized == null) {
            throw new IllegalStateException("no normalized text to accept for " + requirementId());
        }
        moveTo(RequirementStatus.NORMALIZED);
        accepted = Boolean.TRUE;
    }

    /** The Test Engineer rejects the normalized text (FR-56). */
    public void rejectNormalization() {
        if (normalized == null) {
            throw new IllegalStateException("no normalized text to reject for " + requirementId());
        }
        moveTo(RequirementStatus.INVALID);
        accepted = Boolean.FALSE;
    }
}
