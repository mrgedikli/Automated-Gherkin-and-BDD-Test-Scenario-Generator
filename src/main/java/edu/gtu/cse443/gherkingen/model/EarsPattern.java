package edu.gtu.cse443.gherkingen.model;

/**
 * The five EARS sentence patterns (FR-13).
 */
public enum EarsPattern {
    /** "The system shall ..." */
    UBIQUITOUS,
    /** "When ..., the system shall ..." */
    EVENT_DRIVEN,
    /** "While ..., the system shall ..." */
    STATE_DRIVEN,
    /** "If ..., then the system shall ..." */
    UNWANTED_BEHAVIOUR,
    /** "Where ..., the system shall ..." */
    OPTIONAL_FEATURE
}
