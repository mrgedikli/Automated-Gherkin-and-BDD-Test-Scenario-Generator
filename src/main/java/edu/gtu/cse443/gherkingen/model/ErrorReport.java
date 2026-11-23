package edu.gtu.cse443.gherkingen.model;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The validation findings of one generation session (UC-5).
 */
public class ErrorReport {

    private final Instant createdAt;
    private final List<ValidationError> entries = new ArrayList<>();
    private Path savedPath;

    /** The creation time is passed in so that output stays reproducible (NFR-7). */
    public ErrorReport(Instant createdAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void add(ValidationError entry) {
        entries.add(Objects.requireNonNull(entry, "entry"));
    }

    /** All entries ordered by line number; entries on the same line keep their order (FR-40). */
    public List<ValidationError> entries() {
        return entries.stream()
                .sorted(Comparator.comparingInt(ValidationError::lineNumber))
                .toList();
    }

    public List<ValidationError> errors() {
        return entries().stream().filter(ValidationError::isError).toList();
    }

    public List<ValidationError> warnings() {
        return entries().stream().filter(e -> !e.isError()).toList();
    }

    public boolean hasErrors() {
        return entries.stream().anyMatch(ValidationError::isError);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public Optional<Path> savedPath() {
        return Optional.ofNullable(savedPath);
    }

    /** Records where the report was saved (FR-41). */
    public void markSavedTo(Path path) {
        this.savedPath = Objects.requireNonNull(path, "path");
    }
}
