package edu.gtu.cse443.gherkingen.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a {@link Scenario} step by step (Builder pattern, NFR-21).
 *
 * <pre>{@code
 * Scenario s = ScenarioBuilder.forRequirement(req)
 *         .title("The user enters a wrong password")
 *         .given("the user is on the login page")
 *         .when("the user enters a wrong password")
 *         .then("the system displays an error message")
 *         .build();
 * }</pre>
 *
 * <p>Every scenario is tagged with the ID of its requirement (FR-23) and its
 * kind (FR-24); {@link #tag(String)} adds further tags such as
 * {@code "@llm-normalized"} (FR-57).</p>
 */
public final class ScenarioBuilder {

    private static final Pattern TAG = Pattern.compile("@\\S+");
    private static final Pattern PLACEHOLDER = Pattern.compile("<([^<>]+)>");

    private final Requirement requirement;
    private final List<Step> steps = new ArrayList<>();
    private final Set<String> extraTags = new LinkedHashSet<>();
    private String title;
    private ScenarioKind kind = ScenarioKind.POSITIVE;
    private ExamplesTable examples;

    private ScenarioBuilder(Requirement requirement) {
        this.requirement = Objects.requireNonNull(requirement, "requirement");
    }

    public static ScenarioBuilder forRequirement(Requirement requirement) {
        return new ScenarioBuilder(requirement);
    }

    public ScenarioBuilder title(String title) {
        this.title = title;
        return this;
    }

    /** The kind of scenario; {@link ScenarioKind#POSITIVE} if not set. */
    public ScenarioBuilder kind(ScenarioKind kind) {
        this.kind = Objects.requireNonNull(kind, "kind");
        return this;
    }

    public ScenarioBuilder given(String text) {
        return step(StepKeyword.GIVEN, text, null);
    }

    public ScenarioBuilder when(String text) {
        return step(StepKeyword.WHEN, text, null);
    }

    public ScenarioBuilder then(String text) {
        return step(StepKeyword.THEN, text, null);
    }

    /** Adds a step with the same keyword as the previous step. */
    public ScenarioBuilder and(String text) {
        if (steps.isEmpty()) {
            throw new IllegalStateException("a scenario cannot start with And");
        }
        return step(steps.get(steps.size() - 1).keyword(), text, null);
    }

    /** Adds the step an EARS clause maps to (FR-14, FR-15, FR-16). */
    public ScenarioBuilder step(Clause clause, String text) {
        Objects.requireNonNull(clause, "clause");
        return step(clause.kind().stepKeyword(), text, clause);
    }

    public ScenarioBuilder step(StepKeyword keyword, String text, Clause sourceClause) {
        steps.add(new Step(keyword, text, sourceClause));
        return this;
    }

    /** Adds a tag such as {@code "@llm-normalized"}. */
    public ScenarioBuilder tag(String tag) {
        Objects.requireNonNull(tag, "tag");
        if (!TAG.matcher(tag).matches()) {
            throw new IllegalArgumentException("a tag starts with @ and has no spaces: \"" + tag + "\"");
        }
        extraTags.add(tag);
        return this;
    }

    /** Makes the result a {@link ScenarioOutline} with this Examples table (FR-29). */
    public ScenarioBuilder examples(ExamplesTable examples) {
        this.examples = Objects.requireNonNull(examples, "examples");
        return this;
    }

    /**
     * Creates the scenario.
     *
     * @throws IllegalStateException if the title or a Then step is missing, or a
     *                               placeholder of an outline has no column
     */
    public Scenario build() {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("the scenario of " + requirement.requirementId() + " has no title");
        }
        if (steps.stream().noneMatch(s -> s.keyword() == StepKeyword.THEN)) {
            throw new IllegalStateException("scenario \"" + title + "\" has no Then step");
        }
        Set<String> tags = new LinkedHashSet<>();
        tags.add("@" + requirement.requirementId());
        tags.add(kind.tag());
        tags.addAll(extraTags);
        List<String> tagList = List.copyOf(tags);
        if (examples == null) {
            return new Scenario(title, tagList, kind, steps, requirement);
        }
        checkPlaceholders();
        return new ScenarioOutline(title, tagList, kind, steps, requirement, examples);
    }

    private void checkPlaceholders() {
        List<String> texts = new ArrayList<>();
        texts.add(title);
        steps.forEach(s -> texts.add(s.text()));
        for (String text : texts) {
            Matcher m = PLACEHOLDER.matcher(text);
            while (m.find()) {
                if (!examples.header().contains(m.group(1))) {
                    throw new IllegalStateException("placeholder <" + m.group(1)
                            + "> has no column in the Examples table " + examples.header());
                }
            }
        }
    }
}
