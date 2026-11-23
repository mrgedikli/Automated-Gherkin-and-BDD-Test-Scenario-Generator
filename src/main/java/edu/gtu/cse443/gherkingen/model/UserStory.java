package edu.gtu.cse443.gherkingen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A requirement written as "As a [role], I want [goal], so that [benefit]".
 *
 * <p>Role and goal may be empty when the story is incomplete; validation then
 * marks it invalid (FR-34).</p>
 */
public class UserStory extends Requirement {

    private final String role;
    private final String goal;
    private final String benefit;
    private final List<AcceptanceCriterion> acceptanceCriteria = new ArrayList<>();

    public UserStory(String requirementId, String text, int lineNumber, String featureName,
                     String role, String goal, String benefit) {
        super(requirementId, text, lineNumber, featureName);
        this.role = Objects.requireNonNull(role, "role").strip();
        this.goal = Objects.requireNonNull(goal, "goal").strip();
        this.benefit = benefit == null || benefit.isBlank() ? null : benefit.strip();
    }

    @Override
    public InputFormat inputFormat() {
        return InputFormat.USER_STORY;
    }

    public String role() {
        return role;
    }

    public String goal() {
        return goal;
    }

    public Optional<String> benefit() {
        return Optional.ofNullable(benefit);
    }

    /** True when both the role and the goal are given (FR-34). */
    public boolean isComplete() {
        return !role.isEmpty() && !goal.isEmpty();
    }

    /** Adds the next acceptance criterion read from the file (FR-7). */
    public void addAcceptanceCriterion(AcceptanceCriterion criterion) {
        acceptanceCriteria.add(Objects.requireNonNull(criterion, "criterion"));
    }

    public List<AcceptanceCriterion> acceptanceCriteria() {
        return Collections.unmodifiableList(acceptanceCriteria);
    }
}
