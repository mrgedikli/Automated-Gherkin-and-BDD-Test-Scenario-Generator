package edu.gtu.cse443.gherkingen.model;

/**
 * Life cycle of a requirement (state machine "Requirement", Figure 11 of the RAD).
 */
public enum RequirementStatus {
    LOADED,
    NORMALIZED,
    VALID,
    INVALID,
    TRANSLATED;

    /** Whether the state machine allows a transition from this state to {@code target}. */
    public boolean canMoveTo(RequirementStatus target) {
        switch (this) {
            case LOADED:
                return target == NORMALIZED || target == VALID || target == INVALID;
            case NORMALIZED:
                return target == VALID || target == INVALID;
            case VALID:
                return target == TRANSLATED;
            default:
                return false;
        }
    }

    /** True for the states in which the requirement stays until the session ends. */
    public boolean isFinal() {
        return this == INVALID || this == TRANSLATED;
    }
}
