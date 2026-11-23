package edu.gtu.cse443.gherkingen.model;

/**
 * Format of the generated output files (FR-51, FR-52).
 */
public enum OutputFormat {
    GHERKIN(".feature"),
    MARKDOWN(".md"),
    JSON(".json");

    private final String extension;

    OutputFormat(String extension) {
        this.extension = extension;
    }

    /** File extension including the leading dot, e.g. {@code ".feature"}. */
    public String extension() {
        return extension;
    }

    /** The format used when the Test Engineer selects none (FR-52). */
    public static OutputFormat defaultFormat() {
        return GHERKIN;
    }
}
