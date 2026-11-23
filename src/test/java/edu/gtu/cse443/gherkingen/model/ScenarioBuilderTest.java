package edu.gtu.cse443.gherkingen.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class ScenarioBuilderTest {

    private final EarsRequirement req9 = new EarsRequirement("REQ-9",
            "While the account is active, if the user enters a wrong password three times,"
                    + " then the system shall lock the account.",
            3, "Account security",
            List.of(new Clause(ClauseKind.WHILE, "the account is active"),
                    new Clause(ClauseKind.IF, "the user enters a wrong password three times"),
                    new Clause(ClauseKind.SHALL, "lock the account")));

    @Test
    void buildsAScenarioWithRequirementAndKindTags() {
        Scenario s = ScenarioBuilder.forRequirement(req9)
                .title("The user enters a wrong password three times")
                .given("the account is active")
                .when("the user enters a wrong password three times")
                .then("the system locks the account")
                .build();
        assertEquals(List.of("@REQ-9", "@positive"), s.tags());
        assertEquals(ScenarioKind.POSITIVE, s.kind());
        assertEquals(3, s.steps().size());
        assertSame(req9, s.derivedFrom());
        assertFalse(s.isOutline());
    }

    @Test
    void clausesMapToTheirStepKeywords() {
        ScenarioBuilder b = ScenarioBuilder.forRequirement(req9).title("t");
        for (Clause c : req9.clauses()) {
            b.step(c, c.text());
        }
        Scenario s = b.build();
        assertEquals(List.of(StepKeyword.GIVEN, StepKeyword.WHEN, StepKeyword.THEN),
                s.steps().stream().map(Step::keyword).toList());
        assertEquals(Optional.of(req9.clauses().get(1)), s.steps().get(1).sourceClause());
    }

    @Test
    void repeatedKeywordIsWrittenAsAnd() {
        Scenario s = ScenarioBuilder.forRequirement(req9).title("t")
                .given("a").and("b").when("c").then("d").then("e")
                .build();
        List<Step> steps = s.steps();
        assertEquals(StepKeyword.GIVEN, steps.get(1).keyword());
        assertEquals(StepKeyword.GIVEN, steps.get(0).displayKeyword(null));
        assertEquals(StepKeyword.AND, steps.get(1).displayKeyword(steps.get(0)));
        assertEquals(StepKeyword.WHEN, steps.get(2).displayKeyword(steps.get(1)));
        assertEquals(StepKeyword.AND, steps.get(4).displayKeyword(steps.get(3)));
    }

    @Test
    void extraTagsFollowTheStandardTags() {
        Scenario s = ScenarioBuilder.forRequirement(req9).title("t")
                .kind(ScenarioKind.NEGATIVE).tag("@llm-normalized").tag("@REQ-9")
                .when("x").then("y").build();
        assertEquals(List.of("@REQ-9", "@negative", "@llm-normalized"), s.tags());
        assertThrows(IllegalArgumentException.class, () -> ScenarioBuilder.forRequirement(req9).tag("no at"));
    }

    @Test
    void scenarioNeedsATitleAndAThenStep() {
        assertThrows(IllegalStateException.class,
                () -> ScenarioBuilder.forRequirement(req9).when("x").then("y").build());
        assertThrows(IllegalStateException.class,
                () -> ScenarioBuilder.forRequirement(req9).title("t").given("x").when("y").build());
        assertThrows(IllegalStateException.class,
                () -> ScenarioBuilder.forRequirement(req9).title("t").and("x"));
    }

    @Test
    void buildsTheBoundaryOutlineOfTheExpectedSample() {
        ExamplesTable table = new ExamplesTable(List.of("count", "outcome"), List.of(
                List.of("2", "does not lock the account"),
                List.of("3", "locks the account"),
                List.of("4", "locks the account")));
        Scenario s = ScenarioBuilder.forRequirement(req9)
                .title("The user enters a wrong password <count> times")
                .kind(ScenarioKind.BOUNDARY)
                .when("the user enters a wrong password <count> times")
                .then("the system <outcome>")
                .examples(table)
                .build();
        ScenarioOutline outline = assertInstanceOf(ScenarioOutline.class, s);
        assertTrue(outline.isOutline());
        assertEquals(List.of("@REQ-9", "@boundary"), outline.tags());
        assertEquals(3, outline.examples().rows().size());
    }

    @Test
    void outlinePlaceholdersNeedAColumn() {
        ExamplesTable table = new ExamplesTable(List.of("count"), List.of(List.of("3")));
        assertThrows(IllegalStateException.class, () -> ScenarioBuilder.forRequirement(req9)
                .title("t").when("<count> times").then("the system <outcome>").examples(table).build());
    }

    @Test
    void examplesRowsMustMatchTheHeader() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExamplesTable(List.of("a", "b"), List.of(List.of("1"))));
        assertThrows(IllegalArgumentException.class,
                () -> new ExamplesTable(List.of("a"), List.of()));
    }

    @Test
    void stepCannotStoreAnd() {
        assertThrows(IllegalArgumentException.class, () -> new Step(StepKeyword.AND, "x"));
        assertEquals("Then the system locks the account",
                new Step(StepKeyword.THEN, "the system locks the account").toString());
    }

    @Test
    void copiesKeepTheScenarioClass() {
        Scenario s = ScenarioBuilder.forRequirement(req9).title("Lock")
                .given("the account is active").when("x").then("y").build();
        assertEquals(List.of(new Step(StepKeyword.GIVEN, "the account is active")), s.leadingGivenSteps());
        Scenario rest = s.withoutFirstSteps(1);
        assertEquals(2, rest.steps().size());
        assertEquals(s.tags(), rest.tags());
        assertEquals("Lock 2", s.withTitle("Lock 2").title());
        assertThrows(IllegalArgumentException.class, () -> s.withoutFirstSteps(3));
    }
}
