package edu.gtu.cse443.gherkingen.model;

/**
 * Severity of a validation finding. Only an {@link #ERROR} makes a requirement invalid;
 * a {@link #WARNING} (e.g. "compound requirement", FR-36) is only reported.
 */
public enum Severity {
    ERROR,
    WARNING
}
