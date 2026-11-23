package edu.gtu.cse443.gherkingen.model;

import java.util.List;
import java.util.Objects;

/**
 * The Examples table of a scenario outline (FR-29).
 *
 * @param header column names, used as {@code <name>} placeholders in the steps
 * @param rows   one value per column in each row
 */
public record ExamplesTable(List<String> header, List<List<String>> rows) {

    public ExamplesTable {
        Objects.requireNonNull(header, "header");
        Objects.requireNonNull(rows, "rows");
        if (header.isEmpty()) {
            throw new IllegalArgumentException("an examples table has at least one column");
        }
        header.forEach(h -> Checks.requireSingleLine(h, "column name"));
        if (header.stream().distinct().count() != header.size()) {
            throw new IllegalArgumentException("column names must be unique: " + header);
        }
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("an examples table has at least one row");
        }
        for (List<String> row : rows) {
            if (row.size() != header.size()) {
                throw new IllegalArgumentException("row " + row + " does not have "
                        + header.size() + " values");
            }
        }
        header = List.copyOf(header);
        rows = rows.stream().map(List::copyOf).toList();
    }
}
