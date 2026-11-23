package edu.gtu.cse443.gherkingen.model;

import java.util.Objects;

/**
 * Argument checks shared by the model classes.
 */
final class Checks {

    private Checks() {
    }

    static String requireNonBlank(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    static String requireSingleLine(String value, String name) {
        requireNonBlank(value, name);
        if (value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            throw new IllegalArgumentException(name + " must be a single line");
        }
        return value;
    }
}
