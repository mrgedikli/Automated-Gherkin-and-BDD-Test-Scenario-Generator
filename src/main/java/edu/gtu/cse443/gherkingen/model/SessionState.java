package edu.gtu.cse443.gherkingen.model;

/**
 * State of a generation session (attribute {@code state} of GenerationSession,
 * Figure 2; enumeration of Figure 3; state machine "GenerationSession",
 * Figure 12 of the RAD, Issue 1.1).
 *
 * <p>The sub-states of the composite state Validating (checking requirements,
 * normalizing free text) are not separate values: the session is
 * {@link #VALIDATING} in both.</p>
 */
public enum SessionState {
    LOADING,
    VALIDATING,
    GENERATING,
    PREVIEWING,
    EXPORTING,
    WAITING_FOR_CONFIRMATION,
    FINISHED,
    FAILED;

    /** The state a new session starts in ("generate command"). */
    public static SessionState initial() {
        return LOADING;
    }

    /** Whether the state machine allows a transition from this state to {@code target}. */
    public boolean canMoveTo(SessionState target) {
        switch (this) {
            case LOADING:
                // requirements read / file unreadable
                return target == VALIDATING || target == FAILED;
            case VALIDATING:
                // at least one valid requirement / no valid requirement
                return target == GENERATING || target == FAILED;
            case GENERATING:
                // features built
                return target == PREVIEWING;
            case PREVIEWING:
                // export confirmed / [preview only]
                return target == EXPORTING || target == FINISHED;
            case EXPORTING:
                // file already exists / all files processed
                return target == WAITING_FOR_CONFIRMATION || target == FINISHED;
            case WAITING_FOR_CONFIRMATION:
                // answer given
                return target == EXPORTING;
            default:
                return false;
        }
    }

    /** True for the states in which the session ends. */
    public boolean isFinal() {
        return this == FINISHED || this == FAILED;
    }
}
