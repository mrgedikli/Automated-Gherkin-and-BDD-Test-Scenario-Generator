package edu.gtu.cse443.gherkingen;

/**
 * Entry point of the command "gherkingen".
 *
 * <p>The generator is not implemented yet. The commands and options follow
 * Mock-up 1 of the Requirements Analysis Document
 * (docs/deliverable1-rad).</p>
 */
public final class GherkinGen {

    static final String USAGE = String.join(System.lineSeparator(),
            "Usage: gherkingen generate <file> [options]",
            "       gherkingen gui",
            "Run \"gherkingen --help\" for the list of options.",
            "(Not implemented yet - see docs/deliverable1-rad.)");

    private GherkinGen() {
    }

    public static void main(String[] args) {
        System.out.println(USAGE);
    }
}
