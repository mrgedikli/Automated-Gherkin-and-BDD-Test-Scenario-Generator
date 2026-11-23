package edu.gtu.cse443.gherkingen.model;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A requirement written in EARS syntax, split into its ordered clauses.
 *
 * <p>The clauses are stored as parsed, so an incomplete requirement (e.g. one
 * without a "shall" clause) can still be loaded and then reported by validation
 * (FR-32).</p>
 */
public class EarsRequirement extends Requirement {

    private final List<Clause> clauses;

    public EarsRequirement(String requirementId, String text, int lineNumber, String featureName,
                           List<Clause> clauses) {
        super(requirementId, text, lineNumber, featureName);
        Objects.requireNonNull(clauses, "clauses");
        if (clauses.isEmpty()) {
            throw new IllegalArgumentException("an EARS requirement has at least one clause");
        }
        this.clauses = List.copyOf(clauses);
    }

    @Override
    public InputFormat inputFormat() {
        return InputFormat.EARS;
    }

    /** All clauses in the order they appear in the sentence. */
    public List<Clause> clauses() {
        return clauses;
    }

    public List<Clause> clausesOf(ClauseKind kind) {
        return clauses.stream().filter(c -> c.kind() == kind).toList();
    }

    /** The "While", "Where", "When" and "If" clauses, in order. */
    public List<Clause> preconditionClauses() {
        return clauses.stream().filter(c -> !c.kind().isResponse()).toList();
    }

    /** The "shall" clauses, in order. */
    public List<Clause> responseClauses() {
        return clausesOf(ClauseKind.SHALL);
    }

    /** False when the "shall" clause is missing (FR-32). */
    public boolean hasResponse() {
        return !responseClauses().isEmpty();
    }

    /** True when there is more than one "shall" clause (FR-36). */
    public boolean isCompound() {
        return responseClauses().size() > 1;
    }

    /**
     * The EARS patterns this requirement combines (FR-13). A requirement with
     * only a response clause is {@link EarsPattern#UBIQUITOUS}.
     */
    public Set<EarsPattern> patterns() {
        Set<EarsPattern> patterns = EnumSet.noneOf(EarsPattern.class);
        for (Clause clause : clauses) {
            clause.kind().pattern().ifPresent(patterns::add);
        }
        if (patterns.isEmpty()) {
            patterns.add(EarsPattern.UBIQUITOUS);
        }
        return patterns;
    }

    public boolean isUbiquitous() {
        return patterns().contains(EarsPattern.UBIQUITOUS);
    }
}
