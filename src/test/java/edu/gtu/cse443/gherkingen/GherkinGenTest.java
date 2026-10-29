package edu.gtu.cse443.gherkingen;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GherkinGenTest {

    @Test
    void usageNamesTheGenerateCommand() {
        assertTrue(GherkinGen.USAGE.contains("gherkingen generate"));
    }
}
