package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;

/**
 * One clause of an EARS requirement, e.g. WHEN "the user clicks Log out".
 *
 * @param kind the leading keyword of the clause
 * @param text the clause without its leading keyword
 */
public record Clause(ClauseKind kind, String text) {

    public Clause {
        Objects.requireNonNull(kind, "kind");
        Checks.requireSingleLine(text, "text");
    }
}
