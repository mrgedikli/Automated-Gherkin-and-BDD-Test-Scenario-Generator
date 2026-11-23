package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A Gherkin feature: the unit written to one output file (FR-25, FR-45).
 */
public final class Feature {

    private final String title;
    private final String description;
    private final Background background;
    private final List<Scenario> scenarios;

    public Feature(String title, String description, List<Scenario> scenarios) {
        this(title, description, null, scenarios);
    }

    /**
     * @param description free text under the title, may be empty (FR-20)
     * @param background  the shared Given steps (FR-30), or {@code null}
     */
    public Feature(String title, String description, Background background, List<Scenario> scenarios) {
        this.title = Checks.requireSingleLine(title, "title").strip();
        this.description = Objects.requireNonNull(description, "description");
        this.background = background;
        Objects.requireNonNull(scenarios, "scenarios");
        if (scenarios.isEmpty()) {
            throw new IllegalArgumentException("feature \"" + title + "\" has no scenario");
        }
        this.scenarios = List.copyOf(scenarios);
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Optional<Background> background() {
        return Optional.ofNullable(background);
    }

    public List<Scenario> scenarios() {
        return scenarios;
    }

    /**
     * The output file name: the title in lower case with spaces replaced by
     * underscores, plus the extension of the format (FR-46). Characters that
     * are not allowed in file names are dropped.
     */
    public String fileName(OutputFormat format) {
        Objects.requireNonNull(format, "format");
        String name = title.toLowerCase(Locale.ROOT)
                .replaceAll("[\\\\/:*?\"<>|]", "")
                .strip()
                .replaceAll("\\s+", "_");
        if (name.isEmpty()) {
            name = "feature";
        }
        return name + format.extension();
    }

    @Override
    public String toString() {
        return "Feature[" + title + ", " + scenarios.size() + " scenarios]";
    }
}
