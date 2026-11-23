package edu.gtu.cse443.gherkingen.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class FeatureTest {

    private final EarsRequirement req = new EarsRequirement("REQ-7",
            "While the account is active, when the user enters a wrong password,"
                    + " the system shall display an error message.",
            2, "Account security",
            List.of(new Clause(ClauseKind.WHILE, "the account is active"),
                    new Clause(ClauseKind.WHEN, "the user enters a wrong password"),
                    new Clause(ClauseKind.SHALL, "display an error message")));

    private Scenario scenario() {
        return ScenarioBuilder.forRequirement(req).title("The user enters a wrong password")
                .when("the user enters a wrong password").then("the system displays an error message")
                .build();
    }

    @Test
    void fileNameFollowsFr46() {
        Feature f = new Feature("Account security", "", List.of(scenario()));
        assertEquals("account_security.feature", f.fileName(OutputFormat.GHERKIN));
        assertEquals("account_security.md", f.fileName(OutputFormat.MARKDOWN));
        assertEquals("account_security.json", f.fileName(OutputFormat.JSON));
        assertEquals(OutputFormat.GHERKIN, OutputFormat.defaultFormat());
    }

    @Test
    void fileNameDoesNotDependOnTheDefaultLocale() {
        Locale saved = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            Feature f = new Feature("LOGIN Page", "", List.of(scenario()));
            assertEquals("login_page.feature", f.fileName(OutputFormat.GHERKIN));
        } finally {
            Locale.setDefault(saved);
        }
    }

    @Test
    void fileNameDropsCharactersNotAllowedInFileNames() {
        Feature f = new Feature("Pay: \"card\" / cash?", "", List.of(scenario()));
        assertEquals("pay_card_cash.feature", f.fileName(OutputFormat.GHERKIN));
    }

    @Test
    void featureHoldsBackgroundAndScenarios() {
        Background bg = new Background(List.of(new Step(StepKeyword.GIVEN, "the account is active")));
        Feature f = new Feature("Account security", "", bg, List.of(scenario()));
        assertEquals(bg, f.background().orElseThrow());
        assertEquals(1, f.scenarios().size());
        assertThrows(IllegalArgumentException.class, () -> new Feature("Empty", "", List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new Background(List.of(new Step(StepKeyword.WHEN, "x"))));
    }

    @Test
    void errorReportListsEntriesByLineNumber() {
        EarsRequirement first = new EarsRequirement("REQ-1", "When x, the system shall y.", 1, null,
                List.of(new Clause(ClauseKind.WHEN, "x"), new Clause(ClauseKind.SHALL, "y")));
        ErrorReport report = new ErrorReport(Instant.EPOCH);
        report.add(new ValidationError(req, "compound requirement", "", "", Severity.WARNING));
        report.add(new ValidationError(first, "missing response clause",
                "When <trigger>, the system shall <response>.", "add a shall clause", Severity.ERROR));
        assertEquals(List.of(1, 2), report.entries().stream().map(ValidationError::lineNumber).toList());
        assertEquals(1, report.errors().size());
        assertEquals(1, report.warnings().size());
        assertTrue(report.hasErrors());
        assertTrue(report.savedPath().isEmpty());
        report.markSavedTo(Path.of("report.txt"));
        assertEquals(Path.of("report.txt"), report.savedPath().orElseThrow());
    }

    @Test
    void requirementStatusTransitions() {
        assertTrue(RequirementStatus.LOADED.canMoveTo(RequirementStatus.NORMALIZED));
        assertTrue(RequirementStatus.NORMALIZED.canMoveTo(RequirementStatus.INVALID));
        assertEquals(false, RequirementStatus.NORMALIZED.canMoveTo(RequirementStatus.TRANSLATED));
        assertEquals(false, RequirementStatus.TRANSLATED.canMoveTo(RequirementStatus.VALID));
    }
}
